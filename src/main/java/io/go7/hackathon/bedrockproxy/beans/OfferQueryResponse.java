package io.go7.hackathon.bedrockproxy.beans;

import io.go7.commons.flight.AirportCode;
import io.go7.commons.flight.PassengerTypeGroup;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OfferQueryResponse {

      private AirportCode departure;
      private List<AirportCode> arrival = new ArrayList<>();
      private LocalDate departureDate;
      private LocalDate returnDate;
      private List<PassengerQuantity> passengers = new ArrayList<>();

      private boolean finalResult;
      private String requestHint;

      private List<String> news;
      private List<String> weather;

}
