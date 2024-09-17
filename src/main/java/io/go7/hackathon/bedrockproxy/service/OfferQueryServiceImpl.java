package io.go7.hackathon.bedrockproxy.service;

import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;
import io.go7.hackathon.bedrockproxy.beans.PassengerQuantity;
import io.go7.hackathon.bedrockproxy.utils.BedrockHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.utils.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
//@Service
public class OfferQueryServiceImpl implements OfferQueryService {

    public static final String MODEL_ID = "anthropic.claude-3-5-sonnet-20240620-v1:0";
    public static final String WRAPPER =
            "Get from the text IATA airport codes of departure and arrival (if no suggest by the nearest from request) "
                    + "and dates or approximate dates (use year 2024 if not set) "
                    + "and return json as an example "
                    + "{ departure: departure, arrival:arrival, departure_date: date, return_date: date } "
                    + "Text: %s ";

    public static final String WRAPPER_DATE =
            "Get from the text nearest date (use year 2024 if not set) and "
                    + "return json as an example { departure_date: date} "
                    + "Text: %s ";

    public static final String WRAPPER_ARRIVAL =
            "Find nearest most suitable IATA airport code for destination, not departure, based on landmarks in text"
                    + "return json as an example {  arrival:arrival} "
                    + "Text: %s ";

    @Override
    public OfferQueryResponse getOfferQueryResponse(String query) {


        try {

            var response = BedrockHelper.invokeModel(MODEL_ID, WRAPPER.formatted(query));

            Map<String, Object> map = parseResponse(response);

            var offerQueryResponse = new OfferQueryResponse();
            offerQueryResponse.setExplanation(getExplanation(response));

            fillDeparture(map, offerQueryResponse);

            fillArrival(map, offerQueryResponse);

            fillDepartureDate(map, offerQueryResponse);

            fillReturnDate(map, offerQueryResponse);


            if (offerQueryResponse.getPassengers().isEmpty()) {
                offerQueryResponse.getPassengers().add(new PassengerQuantity("ADT", 1));
            }

            if (offerQueryResponse.getDepartureDate() == null) {
                tryToFillDateOneMoreTime(query, offerQueryResponse);
            }

            if (offerQueryResponse.getArrival() == null) {
                tryToFillArrivalOneMoreTime(query, offerQueryResponse);
            }

            if (mandatoryDataFilled(offerQueryResponse)) {
                offerQueryResponse.setFinalResult(true);
            } else {
                generateHint(offerQueryResponse);
            }

            if (StringUtils.isNotBlank(offerQueryResponse.getArrival())
                    && offerQueryResponse.getDepartureDate() != null && offerQueryResponse.isFinalResult()) {
                fillNews(offerQueryResponse, offerQueryResponse.getArrival(), offerQueryResponse.getDepartureDate());
            }

            return offerQueryResponse;

        } catch (Exception e) {
            log.error(e.getMessage());
            OfferQueryResponse offerQueryResponse = new OfferQueryResponse();
            offerQueryResponse.setFinalResult(false);
            offerQueryResponse.setRequestHint("Please, try another request");
            return offerQueryResponse;
        }

    }

    private void fillReturnDate(Map<String, Object> map, OfferQueryResponse offerQueryResponse) {
        try {
            if (map.containsKey("return_date")) {
                offerQueryResponse.setReturnDate(LocalDate.parse(map.get("return_date").toString()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void fillNews(OfferQueryResponse offerQueryResponse, String location, LocalDate date) {
        String prompt = "short recommendations for travelers flying to {{AIRPORT}} on {{DATE}}, formatted as a json array of strings - include weather forecast, mention a relevant piece of recent news, warn of peak dates or better destinations, safety concerns, events and suggested activities, use emojis";

        List<String> news = BedrockHelper.invokeCohere(prompt.replace("{{AIRPORT}}", location)
                .replace("{{DATE}}", date.toString()));

        offerQueryResponse.setNews(news);
    }

    private void tryToFillDateOneMoreTime(String query, OfferQueryResponse offerQueryResponse) throws JsonProcessingException {
        var response = BedrockHelper.invokeModel(MODEL_ID, WRAPPER_DATE.formatted(query));
        Map<String, Object> map = parseResponse(response);
        fillDepartureDate(map, offerQueryResponse);
    }

    private void tryToFillArrivalOneMoreTime(String query, OfferQueryResponse offerQueryResponse) throws JsonProcessingException {
        var response = BedrockHelper.invokeModel(MODEL_ID, WRAPPER_ARRIVAL.formatted(query));
        Map<String, Object> map = parseResponse(response);
        fillArrival(map, offerQueryResponse);
    }

    private Map<String, Object> parseResponse(String response) throws JsonProcessingException {
        var json = response.substring(response.indexOf("{"));
        json = json.substring(0, json.indexOf("}") + 1);

        var mapper = new ObjectMapper();

        Map<String, Object> map = mapper.readValue(json, Map.class);
        return map;
    }

    private String getExplanation(String response) {
        try {
            if (response.contains("Explanation:")) {
                return response.substring(response.indexOf("Explanation:") + 12);
            } else if (response.contains("Notes:")) {
                return response.substring(response.indexOf("Notes:") + 6);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    private void fillDepartureDate(Map<String, Object> map, OfferQueryResponse offerQueryResponse) {
        try {
            if (map.containsKey("departure_date")) {
                offerQueryResponse.setDepartureDate(LocalDate.parse(map.get("departure_date").toString()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void fillArrival(Map<String, Object> map, OfferQueryResponse offerQueryResponse) {
        try {
            if (map.containsKey("arrival")) {
                offerQueryResponse.setArrival(map.get("arrival").toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void fillDeparture(Map<String, Object> map, OfferQueryResponse offerQueryResponse) {
        try {
            if (map.containsKey("departure")) {
                offerQueryResponse.setDeparture(map.get("departure").toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void generateHint(OfferQueryResponse offerQueryResponse) {
        if (offerQueryResponse.getDeparture() == null
                && offerQueryResponse.getArrival() == null
                && offerQueryResponse.getDepartureDate() == null) {
            offerQueryResponse.setRequestHint("Please provide information about your departure and arrival locations and date for your trip");
        } else if (offerQueryResponse.getDeparture() == null && offerQueryResponse.getArrival() == null) {
            offerQueryResponse.setRequestHint("Please provide information about your departure and arrival locations");
        } else if (offerQueryResponse.getDepartureDate() == null && offerQueryResponse.getArrival() == null) {
            offerQueryResponse.setRequestHint("Please provide information about your arrival location and trip date");
        } else if (offerQueryResponse.getDepartureDate() == null && offerQueryResponse.getDepartureDate() == null) {
            offerQueryResponse.setRequestHint("Please provide information about your departure location and trip date");
        } else if (offerQueryResponse.getDeparture() == null) {
            offerQueryResponse.setRequestHint("Please provide information about your departure location");
        } else if (offerQueryResponse.getArrival() == null) {
            offerQueryResponse.setRequestHint("Please provide information about your arrival location");
        } else if (offerQueryResponse.getDepartureDate() == null) {
            offerQueryResponse.setRequestHint("Please provide information about your trip date");
        }

    }

    private boolean mandatoryDataFilled(OfferQueryResponse offerQueryResponse) {
        return !offerQueryResponse.getPassengers().isEmpty()
                && offerQueryResponse.getDeparture() != null
                && offerQueryResponse.getArrival() != null
                && offerQueryResponse.getDepartureDate() != null;
    }
}
