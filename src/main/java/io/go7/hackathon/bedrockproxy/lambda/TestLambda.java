package io.go7.hackathon.bedrockproxy.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.go7.hackathon.bedrockproxy.service.OfferQueryServiceImpl;
import java.util.Arrays;
import java.util.Collections;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

public class TestLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final OfferQueryServiceImpl offerQueryService = new OfferQueryServiceImpl();

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
    String userMessage = request.getBody();

    if (true) {
      APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
      responseEvent.setStatusCode(200);
      responseEvent.setBody(userMessage);

      return responseEvent;
    }

    BedrockRuntimeClient client = BedrockRuntimeClient.builder().region(Region.US_EAST_1).build();

    String modelId = "anthropic.claude-3-sonnet-20240229-v1:0";

    String systemMessage = "Provide of sample IATA codes in format just string";

    ContentBlock userContentBlock = ContentBlock.builder().text(systemMessage).build();

    Message userMsg = Message.builder().role("user").content(Collections.singletonList(userContentBlock)).build();

    InferenceConfiguration parameters = InferenceConfiguration.builder().maxTokens(100).temperature(0.9f).topP(0.5f).build();

//    offerQueryService.getOfferQueryResponse()

    ConverseRequest converseRequest = ConverseRequest
        .builder()
        .modelId(modelId)
        .messages(Arrays.asList(userMsg))
        .inferenceConfig(parameters)
        .build();

    ConverseResponse response = client.converse(converseRequest);

    String responseText = response.output().message().content().get(0).text();

    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
    responseEvent.setStatusCode(200);
    responseEvent.setBody(responseText);

    return responseEvent;
  }
}

