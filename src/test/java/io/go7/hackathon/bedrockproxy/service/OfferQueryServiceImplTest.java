package io.go7.hackathon.bedrockproxy.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Random;

class OfferQueryServiceImplTest {

    private OfferQueryServiceImpl offerQueryService = new OfferQueryServiceImpl();

//    @Test
    void getOfferQueryResponse1() {

        String query = "Find me offers from Berlin to Barcelona at the end of October";
        OfferQueryResponse offerQueryResponse = offerQueryService.getOfferQueryResponse(query);
        System.out.println(offerQueryResponse);

        assertNotNull(offerQueryResponse.getDepartureDate());
        assertNotNull(offerQueryResponse.getArrival());
        assertNotNull(offerQueryResponse.getDeparture());
        assertNotEquals(offerQueryResponse.getArrival(), offerQueryResponse.getDeparture());

    }

    @Test
    void getOfferQueryResponse2() {

        String query = "Where can you fly for snowboarding cheaper in December from Warsaw?";
        OfferQueryResponse offerQueryResponse = offerQueryService.getOfferQueryResponse(query);
        System.out.println(offerQueryResponse);

        assertNotNull(offerQueryResponse.getDepartureDate());
        assertNotNull(offerQueryResponse.getArrival());
        assertNotNull(offerQueryResponse.getDeparture());
        assertNotEquals(offerQueryResponse.getArrival(), offerQueryResponse.getDeparture());

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
        offerQueryResponse.setFinalResult(true);

        offerQueryService.fillNews(offerQueryResponse, airports[random.nextInt(0, 50)], date);

        assert !offerQueryResponse.getNews().isEmpty();
    }

    @Test
    void getOfferQueryResponse3() {

        String query = "Find me flights to the ocean. I am in London. I want to fly on 21 November";
        OfferQueryResponse offerQueryResponse = offerQueryService.getOfferQueryResponse(query);
        System.out.println(offerQueryResponse);

        assertNotNull(offerQueryResponse.getDepartureDate());
        assertNotNull(offerQueryResponse.getArrival());
        assertNotNull(offerQueryResponse.getDeparture());
        assertNotEquals(offerQueryResponse.getArrival(), offerQueryResponse.getDeparture());

    }

    @Test
    void getOfferQueryResponse4() {

        String query = "Suggest me round trip ticket options for two adult passengers from Warsaw to Athens for October 2024";
        OfferQueryResponse offerQueryResponse = offerQueryService.getOfferQueryResponse(query);
        System.out.println(offerQueryResponse);

        assertNotNull(offerQueryResponse.getDepartureDate());
        assertNotNull(offerQueryResponse.getArrival());
        assertNotNull(offerQueryResponse.getDeparture());
        assertNotEquals(offerQueryResponse.getArrival(), offerQueryResponse.getDeparture());

    }

}