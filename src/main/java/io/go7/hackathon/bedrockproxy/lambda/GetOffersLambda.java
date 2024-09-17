package io.go7.hackathon.bedrockproxy.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;
import io.go7.hackathon.bedrockproxy.service.OfferQueryServiceImpl;
import io.go7.hackathon.bedrockproxy.utils.BedrockHelper;

public class GetOffersLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


  private static final OfferQueryServiceImpl offerQueryService = new OfferQueryServiceImpl();

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
    PromptRequest promptRequest = JsonUtils.readJson(request.getBody(), PromptRequest.class);

    System.out.println("Going to process prompt: " + promptRequest.getPrompt());

    OfferQueryResponse offerQueryResponse = offerQueryService.getOfferQueryResponse(promptRequest.getPrompt());

    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
    responseEvent.setStatusCode(200);
    responseEvent.setBody(JsonUtils.toJson(offerQueryResponse));

    return responseEvent;
  }
}

