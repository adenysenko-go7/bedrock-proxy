package io.go7.hackathon.bedrockproxy.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

class CohereNewsServiceImplTest {

    private CohereNewsService cohereNewsService = new CohereNewsServiceImpl();

    @Test
    void newsRetrieval() {
        String[] airports = new String[] {
                "LHR", "CDG", "FRA", "AMS", "MAD", "BCN", "MXP", "FCO", "DUB", "VIE",
                "CPH", "OSL", "ARN", "ZRH", "BRU", "ATH", "HEL", "WAW", "PRG", "BUD",
                "LGW", "STN", "MAN", "LTN", "SXF", "BER", "TXL", "MUC", "DUS", "GVA",
                "LIS", "OTP", "BTS", "RIX", "TLL", "VNO", "LJU", "ZAG", "BEG", "SOF",
                "SKG", "HER", "MLA", "TIA", "KRK", "KEF", "EDI", "GLA", "NCE", "LYS",
                "BVA" };

        Random random = new Random();
        LocalDate date = LocalDate.now().plusDays(random.nextInt(1, 100));

        OfferQueryResponse offerQueryResponse = new OfferQueryResponse();
        offerQueryResponse.setFinalResult(true);

        List<String> recommendations = cohereNewsService.getRecommendations(airports[random.nextInt(0, 50)], date);

        assert !recommendations.isEmpty();
    }
}