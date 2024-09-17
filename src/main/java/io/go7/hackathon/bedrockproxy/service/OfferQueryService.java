package io.go7.hackathon.bedrockproxy.service;

import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;

public interface OfferQueryService {

    OfferQueryResponse getOfferQueryResponse(String query);
}
