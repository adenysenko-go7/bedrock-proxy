package io.go7.hackathon.bedrockproxy.service;

import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Random;

class OfferQueryServiceImplTest {

    private OfferQueryServiceImpl offerQueryService = new OfferQueryServiceImpl();

//    @Test
    void getOfferQueryResponse() {

        String query = "Find me offers from Berlin to Barcelona at the end of October";
        System.out.println(offerQueryService.getOfferQueryResponse(query));
    }

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
        offerQueryService.fillNews(offerQueryResponse, airports[random.nextInt(0, 50)], date);

        assert !offerQueryResponse.getNews().isEmpty();
    }
}