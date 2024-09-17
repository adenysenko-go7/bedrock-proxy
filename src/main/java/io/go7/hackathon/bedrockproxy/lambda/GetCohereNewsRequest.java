package io.go7.hackathon.bedrockproxy.lambda;

import java.time.LocalDate;
import lombok.Data;

@Data
public class GetCohereNewsRequest {
  private String destination;
  private LocalDate date;
}
