package io.go7.hackathon.bedrockproxy.service;

import io.go7.commons.flight.AirportCode;
import io.go7.commons.flight.PassengerTypeGroup;
import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;
import io.go7.hackathon.bedrockproxy.beans.PassengerQuantity;
import io.go7.hackathon.bedrockproxy.utils.BedrockHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
//@Service
public class OfferQueryServiceImpl implements OfferQueryService {

    public static final String MODEL_ID = "anthropic.claude-3-sonnet-20240229-v1:0";
    public static final String WRAPPER =
            "Get from the text IATA airport codes of the cities "
                    + "and dates or approximate dates "
                    + "and return json as an example "
                    + "{ departure: departure, arrival:arrival, departure_date: date} "
                    + "Text: %s ";

    public static final String WRAPPER_DATE =
            "Get from the text nearest date and "
                    + "return json as an example { departure: departure, arrival:arrival, departure_date: date} "
                    + "Text: %s ";

    @Override
    public OfferQueryResponse getOfferQueryResponse(String query) {


        try {

            var response = BedrockHelper.invokeModel(MODEL_ID, WRAPPER.formatted(query));

            Map<String, Object> map = parseResponse(response);

            var offerQueryResponse = new OfferQueryResponse();

            fillDeparture(map, offerQueryResponse);

            fillArrival(map, offerQueryResponse);

            fillDepartureDate(map, offerQueryResponse);

            if (offerQueryResponse.getPassengers().isEmpty()) {
                offerQueryResponse.getPassengers().add(new PassengerQuantity(PassengerTypeGroup.ADULT, 1));
            }

            if(offerQueryResponse.getDepartureDate() == null) {
                var date_response = BedrockHelper.invokeModel(MODEL_ID, WRAPPER_DATE.formatted(query));
                Map<String, Object> date_map = parseResponse(date_response);
                fillDepartureDate(date_map, offerQueryResponse);
            }

            if (mandatoryDataFilled(offerQueryResponse)) {
                offerQueryResponse.setFinalResult(true);
            } else {
                generateHint(offerQueryResponse);
            }
            return offerQueryResponse;

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            OfferQueryResponse offerQueryResponse = new OfferQueryResponse();
            offerQueryResponse.setFinalResult(false);
            offerQueryResponse.setRequestHint("Please, try another request");
            return offerQueryResponse;
        }

    }

    private Map<String, Object> parseResponse(String response) throws JsonProcessingException {
        var json = response.substring(response.indexOf("{"));
        json = json.substring(0, json.indexOf("}") + 1);

        var mapper = new ObjectMapper();

        Map<String, Object> map = mapper.readValue(json, Map.class);
        return map;
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
                offerQueryResponse.getArrival().add(AirportCode.of(map.get("arrival").toString()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void fillDeparture(Map<String, Object> map, OfferQueryResponse offerQueryResponse) {
        try {
            if (map.containsKey("departure")) {
                offerQueryResponse.setDeparture(AirportCode.of(map.get("departure").toString()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void generateHint(OfferQueryResponse offerQueryResponse) {
        if (offerQueryResponse.getDeparture() == null
                && offerQueryResponse.getArrival().isEmpty()
                && offerQueryResponse.getDepartureDate() == null) {
            offerQueryResponse.setRequestHint("Please provide information about where your departure and arrival places and date for your trip");
        } else if (offerQueryResponse.getDeparture() == null && offerQueryResponse.getArrival().isEmpty()) {
            offerQueryResponse.setRequestHint("Please provide information about where your departure and arrival places are");
        } else if (offerQueryResponse.getDepartureDate() == null && offerQueryResponse.getArrival().isEmpty()) {
            offerQueryResponse.setRequestHint("Please provide information about where your arrival place and trip date");
        } else if (offerQueryResponse.getDepartureDate() == null && offerQueryResponse.getDepartureDate() == null) {
            offerQueryResponse.setRequestHint("Please provide information about where your departure and trip date");
        } else if (offerQueryResponse.getDeparture() == null) {
            offerQueryResponse.setRequestHint("Please provide information about where your departure place");
        } else if (offerQueryResponse.getArrival().isEmpty()) {
            offerQueryResponse.setRequestHint("Please provide information about where your arrival place");
        } else if (offerQueryResponse.getDepartureDate() == null) {
            offerQueryResponse.setRequestHint("Please provide information about where your trip date");
        }

    }

    private boolean mandatoryDataFilled(OfferQueryResponse offerQueryResponse) {
        return !offerQueryResponse.getPassengers().isEmpty()
                && offerQueryResponse.getDeparture() != null
                && ! offerQueryResponse.getArrival().isEmpty()
                && offerQueryResponse.getDepartureDate() != null;
    }
}
