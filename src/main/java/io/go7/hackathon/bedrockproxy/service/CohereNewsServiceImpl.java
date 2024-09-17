package io.go7.hackathon.bedrockproxy.service;

import io.go7.hackathon.bedrockproxy.utils.BedrockHelper;

import java.time.LocalDate;
import java.util.List;

public class CohereNewsServiceImpl implements CohereNewsService {

    private final static String PROMPT =
            "short recommendations for travelers flying to {{AIRPORT}} on {{DATE}}, formatted as a json array of strings - include weather forecast, relevant piece of recent news, events and suggested activities, warn of peak dates or better destinations, safety concerns; use emojis, Celsius degrees, cite sources";

    @Override
    public List<String> getRecommendations(String location, LocalDate date) {
        return BedrockHelper.invokeCohere(PROMPT.replace("{{AIRPORT}}", location)
                .replace("{{DATE}}", date.toString()));
    }
}
