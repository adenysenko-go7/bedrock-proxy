package io.go7.hackathon.bedrockproxy.beans;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class OfferQueryResponse {

      private String departure;
      private String arrival;
      private LocalDate departureDate;
      private LocalDate returnDate;
      private List<PassengerQuantity> passengers = new ArrayList<>();

      private boolean finalResult;
      private String requestHint;

      private List<String> news;

      private String explanation;


}
