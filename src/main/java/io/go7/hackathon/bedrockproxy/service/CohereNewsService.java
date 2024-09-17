package io.go7.hackathon.bedrockproxy.service;

import java.time.LocalDate;
import java.util.List;

public interface CohereNewsService {
    List<String> getRecommendations(String locations, LocalDate date);
}
