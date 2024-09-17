package io.go7.hackathon.bedrockproxy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;

import org.junit.jupiter.api.Test;

class OfferQueryServiceImplTest {

    private OfferQueryServiceImpl offerQueryService = new OfferQueryServiceImpl();

    @Test
    void getOfferQueryResponse1() {

        String query = "Find me offers from Berlin to Barcelona at the end of October";
        OfferQueryResponse offerQueryResponse = offerQueryService.getOfferQueryResponse(query);
        System.out.println(offerQueryResponse);

        assertNotNull(offerQueryResponse.getDepartureDate());
        assertNotNull(offerQueryResponse.getArrival());
        assertNotNull(offerQueryResponse.getDeparture());
        assertNotEquals(offerQueryResponse.getArrival(), offerQueryResponse.getDeparture());
        assertEquals(1, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("ADT")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("CHD")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("INF")).findFirst().orElseThrow().getQuantity());

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
        assertEquals(1, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("ADT")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("CHD")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("INF")).findFirst().orElseThrow().getQuantity());

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
        assertEquals(1, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("ADT")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("CHD")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("INF")).findFirst().orElseThrow().getQuantity());

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
        assertEquals(2, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("ADT")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("CHD")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("INF")).findFirst().orElseThrow().getQuantity());

    }

    @Test
    void getOfferQueryResponse5() {

        String query = "want to go somewhere nice and warm soon from Berlin";
        OfferQueryResponse offerQueryResponse = offerQueryService.getOfferQueryResponse(query);
        System.out.println(offerQueryResponse);

        assertNotNull(offerQueryResponse.getDepartureDate());
        assertNotNull(offerQueryResponse.getArrival());
        assertNotNull(offerQueryResponse.getDeparture());
        assertNotEquals(offerQueryResponse.getArrival(), offerQueryResponse.getDeparture());
        assertEquals(1, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("ADT")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("CHD")).findFirst().orElseThrow().getQuantity());
        assertEquals(0, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("INF")).findFirst().orElseThrow().getQuantity());

    }

    @Test
    void getOfferQueryResponse6() {

        String query = "I wanna flight with my wife and 2 sons and one new born to the ocean next November. We live in Madrid";
        OfferQueryResponse offerQueryResponse = offerQueryService.getOfferQueryResponse(query);
        System.out.println(offerQueryResponse);

        assertNotNull(offerQueryResponse.getDepartureDate());
        assertNotNull(offerQueryResponse.getArrival());
        assertNotNull(offerQueryResponse.getDeparture());
        assertNotEquals(offerQueryResponse.getArrival(), offerQueryResponse.getDeparture());
        assertEquals(2, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("ADT")).findFirst().orElseThrow().getQuantity());
        assertEquals(2, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("CHD")).findFirst().orElseThrow().getQuantity());
        assertEquals(1, offerQueryResponse.getPassengers().stream().filter(pq->pq.getType().equals("INF")).findFirst().orElseThrow().getQuantity());

    }

}