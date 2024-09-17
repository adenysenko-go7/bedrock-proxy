package io.go7.hackathon.bedrockproxy.service;

import io.go7.commons.flight.AirportCode;
import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;
import io.go7.hackathon.bedrockproxy.utils.BedrockHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Service
public class OfferQueryServiceImpl implements OfferQueryService {

    public static final String MODEL_ID = "anthropic.claude-3-sonnet-20240229-v1:0";
    public static final String WRAPPER =
            "Get from the text IATA airport codes of the cities and dates and return only json as an example { departure: departure, arrival:arrival, departure_date: departure date} Text: %s ";

    @Override
    public OfferQueryResponse getOfferQueryResponse(String query) {


        try {

            var response = BedrockHelper.invokeModel(MODEL_ID, WRAPPER.formatted(query));

            var json = response.substring(response.indexOf("{"));
            json = json.substring(0, json.indexOf("}") + 1);

            var mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(json, Map.class);

            var offerQueryResponse = new OfferQueryResponse();

            if (map.containsKey("departure")) {
                offerQueryResponse.setDeparture(AirportCode.of(map.get("departure").toString()));
            }

            if (map.containsKey("arrival")) {
                offerQueryResponse.getArrival().add(AirportCode.of(map.get("arrival").toString()));
            }

            if (map.containsKey("departure_date")) {
                offerQueryResponse.setDepartureDate(LocalDate.parse(map.get("departure_date").toString()));
            }

            offerQueryResponse.setFinalResult(true);
            return offerQueryResponse;

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            OfferQueryResponse offerQueryResponse = new OfferQueryResponse();
            offerQueryResponse.setFinalResult(false);
            offerQueryResponse.setRequestHint("try another request");
            return offerQueryResponse;
        }

    }
}
