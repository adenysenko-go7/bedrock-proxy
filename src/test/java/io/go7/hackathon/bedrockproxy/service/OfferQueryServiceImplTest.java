package io.go7.hackathon.bedrockproxy.service;

import org.junit.jupiter.api.Test;

class OfferQueryServiceImplTest {

    private OfferQueryServiceImpl offerQueryService = new OfferQueryServiceImpl();

    @Test
    void getOfferQueryResponse() {

        String query = "Find me offers from Berlin to Barcelona at the end of October";
        System.out.println(offerQueryService.getOfferQueryResponse(query));
    }
}