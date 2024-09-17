package io.go7.hackathon.bedrockproxy.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.go7.hackathon.bedrockproxy.service.CohereNewsService;
import io.go7.hackathon.bedrockproxy.service.CohereNewsServiceImpl;
import java.util.List;

public class GetCohereNewsLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


  private static final CohereNewsService cohereNewsService = new CohereNewsServiceImpl();

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
    GetCohereNewsRequest getCohereNewsRequest = JsonUtils.readJson(request.getBody(), GetCohereNewsRequest.class);

    System.out.println("locations: " + getCohereNewsRequest.getDestination());
    System.out.println("date: " + getCohereNewsRequest.getDate());

    List<String> recommendations = cohereNewsService.getRecommendations(getCohereNewsRequest.getDestination(), getCohereNewsRequest.getDate());

    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
    responseEvent.setStatusCode(200);
    responseEvent.setBody(JsonUtils.toJson(recommendations));

    return responseEvent;
  }
}
