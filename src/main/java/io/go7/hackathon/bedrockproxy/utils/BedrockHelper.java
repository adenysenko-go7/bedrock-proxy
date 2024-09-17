package io.go7.hackathon.bedrockproxy.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentResponseHandler;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseRetrieveAndGenerateConfiguration;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateConfiguration;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateInput;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateType;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.DocumentFormat;
import software.amazon.awssdk.services.bedrockruntime.model.DocumentSource;
import software.amazon.awssdk.services.bedrockruntime.model.ImageBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ImageFormat;
import software.amazon.awssdk.services.bedrockruntime.model.ImageSource;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamResponseHandler;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamResponseHandler.Visitor;
import software.amazon.awssdk.services.bedrockruntime.model.Message;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.AnalyzeIdRequest;
import software.amazon.awssdk.services.textract.model.AnalyzeIdResponse;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.IdentityDocument;
import software.amazon.awssdk.services.textract.model.TextractException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/*
 * This class contains basic methods for invoking AWS Bedrock generative AI model.
 * It supports knowledge bases, invoking the model with streaming, invoking hte model without streaming
 */
public class BedrockHelper {
    /*
    method to invoke AWS Bedrock model and get the response without streaming. 
    Parameters:
    ModelId: The ID of the Bedrock model to invoke.
    Body: The input to the model as a JSON string.
    Returns:
    The response from the model as a JSON string.
    */
    public static String invokeModel(String modelId, String prompt) {
        

            // Create a Bedrock Runtime client in the AWS Region you want to use.
            // Replace the DefaultCredentialsProvider with your preferred credentials provider.

        // Set the model ID, e.g., Claude 3 Haiku.
            
    
            // The InvokeModel API uses the model's native payload.
            // Learn more about the available inference parameters and response fields at:
            // https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-anthropic-claude-messages.html
            var nativeRequestTemplate = """
                    {
                        "anthropic_version": "bedrock-2023-05-31",
                        "max_tokens": 512,
                        "temperature": 0.5,
                        "messages": [{
                            "role": "user",
                            "content": "{{prompt}}"
                        }]
                    }""";
    
            // Define the prompt for the model.
           

            // Embed the prompt in the model's native request payload.
            String nativeRequest = nativeRequestTemplate.replace("{{prompt}}", prompt);

            try(BedrockRuntimeClient client = initClient()) {
                // Encode and send the request to the Bedrock Runtime.
                InvokeModelResponse response = client.invokeModel(request -> request
                        .body(SdkBytes.fromUtf8String(nativeRequest))
                        .modelId(modelId)
                );
    
                // Decode the response body.
                var responseBody = new JSONObject(response.body().asUtf8String());
    
                // Retrieve the generated text from the model's response.

                return new JSONPointer("/content/0/text").queryFrom(responseBody).toString();
    
            } 
            catch (SdkClientException e) 
            {
                System.err.printf("ERROR: Can't invoke '%s'. Reason: %s", modelId, e.getMessage());
                throw new RuntimeException(e);
            }
    }

    private static BedrockRuntimeClient initClient() {
        BedrockRuntimeClient client = BedrockRuntimeClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                            "AKIAX2DZEWJ54LNY5TTL", "FWPHleWlRcwTs1NwTkiNJPzbm0kvJOq6Xra8alhM")))
            .region(Region.US_EAST_1)
            .build();
        return client;
    }


    /*
     method to invoke AWS Bedrock model and get the response with streaming.
     Parameters:
     ModelId: The ID of the Bedrock model to invoke.
     Body: The input to the model as a JSON string.
     Returns:
     The response from the model as a stream of JSON strings.
     */
    public static String invokeModelWithStream(String modelId, String prompt) {
        // Create a Bedrock Runtime client in the AWS Region you want to use.
        // Replace the DefaultCredentialsProvider with your preferred credentials provider.
        var client = BedrockRuntimeAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .build();

  

        // The InvokeModelWithResponseStream API uses the model's native payload.
        // Learn more about the available inference parameters and response fields at:
        // https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-anthropic-claude-messages.html
        var nativeRequestTemplate = """
                {
                    "anthropic_version": "bedrock-2023-05-31",
                    "max_tokens": 15120,
                    "temperature": 0.5,
                    "messages": [{
                        "role": "user",
                        "content": "{{prompt}}"
                    }]
                }""";

        
        // Embed the prompt in the model's native request payload.
        String nativeRequest = nativeRequestTemplate.replace("{{prompt}}", prompt);

        // Create a request with the model ID and the model's native request payload.
        var request = InvokeModelWithResponseStreamRequest.builder()
                .body(SdkBytes.fromUtf8String(nativeRequest))
                .modelId(modelId)
                .build();

        // Prepare a buffer to accumulate the generated response text.
        var completeResponseTextBuffer = new StringBuilder();

        // Prepare a handler to extract, accumulate, and print the response text in real-time.
        var responseStreamHandler = InvokeModelWithResponseStreamResponseHandler.builder()
                .subscriber(Visitor.builder().onChunk(chunk -> {
                    var response = new JSONObject(chunk.bytes().asUtf8String());

                    // Extract and print the text from the content blocks.
                    if (Objects.equals(response.getString("type"), "content_block_delta")) {
                        var text = new JSONPointer("/delta/text").queryFrom(response);
                        System.out.print(text);

                        // Append the text to the response text buffer.
                        completeResponseTextBuffer.append(text);
                    }
                }).build()).build();

        try {
            // Send the request and wait for the handler to process the response.
            client.invokeModelWithResponseStream(request, responseStreamHandler).get();

            // Return the complete response text.
            return completeResponseTextBuffer.toString();

        } catch (ExecutionException | InterruptedException e) {
            System.err.printf("Can't invoke '%s': %s", modelId, e.getCause().getMessage());
            throw new RuntimeException(e);
        }
    }

   
    //method to invoke Bedrock Sonnet model with an image and text
    public static String converseApi(String modelId,String imagePath) throws IOException {


        // Create a Bedrock Runtime client in the AWS Region you want to use.
        // Replace the DefaultCredentialsProvider with your preferred credentials provider.
        var client = BedrockRuntimeClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        
        // Create the input text and embed it in a message object with the user role.
        var inputText = "Describe the content of the image.";

        File fi = new File(imagePath);
        byte[] fileContent = Files.readAllBytes(fi.toPath());

        Collection<ContentBlock> col = new ArrayList<ContentBlock>();

        col.add(ContentBlock.fromImage(ImageBlock.builder()
                .source(ImageSource.builder()
                    .bytes(SdkBytes.fromByteArray(fileContent))
                    .build())
                .format(ImageFormat.JPEG)
                .build()));
        col.add(ContentBlock.fromText(inputText));     

        var message = Message.builder()
                .content(col)
                .role(ConversationRole.USER)
                .build();


        try {
            // Send the message with a basic inference configuration.
            ConverseResponse response = client.converse(request -> request
                    .modelId(modelId)
                    .messages(message)
                    .inferenceConfig(config -> config
                            .maxTokens(1024)
                            .temperature(0.5F)
                            .topP(0.9F)));

            // Retrieve the generated text from Bedrock's response object.
            var responseText = response.output().message().content().get(0).text();
            System.out.println("\n"+responseText);

            return responseText;

        } catch (SdkClientException e) {
            System.err.printf("ERROR: Can't invoke '%s'. Reason: %s", modelId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //function to invoke AWS Bedrock Agent
    public static String invokeAgent(String prompt,
                                     String agentId, 
                                     String agentAlisId,
                                     String sessionId) throws ExecutionException, InterruptedException {
        
        BedrockAgentRuntimeAsyncClient client  = BedrockAgentRuntimeAsyncClient.builder().build();

        var completeResponseTextBuffer = new StringBuilder();
    
        var handler = InvokeAgentResponseHandler.builder()
                .subscriber(InvokeAgentResponseHandler.Visitor.builder()
                        .onChunk(chunk -> completeResponseTextBuffer.append(chunk.bytes().asUtf8String()))
                        .build())
                .build();
    
        var request = InvokeAgentRequest.builder()
                .agentId(agentId)
                .agentAliasId(agentAlisId)
                .sessionId(sessionId)
                .inputText(prompt)
                .build();
    
        client.invokeAgent(request, handler).get();
    
        String response =  completeResponseTextBuffer.toString();
        System.out.println(response);
        return response;
    }

    //Real-time document insight
    public static String documentInsight(String filePath,String modelId,String command) throws IOException {
        var client = BedrockRuntimeClient.builder().region(Region.US_EAST_1).build();

        
        var fileContent = SdkBytes.fromByteArray(Files.readAllBytes(Paths.get(filePath)));

        var textMessage = ContentBlock.fromText(command);
        var document = ContentBlock.fromDocument(doc -> doc.name("document")
                .format(DocumentFormat.PDF)
                .source(DocumentSource.fromBytes(fileContent)));

        var response = client.converse(req -> req
                .modelId(modelId)
                .messages(message -> message
                        .role(ConversationRole.USER)
                        .content(textMessage, document)));

        return response.output().message().content().get(0).text();    
    }

    public static String queryKnowledgeBase(String kbId,String text,String modelArn) throws InterruptedException, ExecutionException
    {
        BedrockAgentRuntimeAsyncClient client  = BedrockAgentRuntimeAsyncClient.builder().build();
        return client.retrieveAndGenerate(RetrieveAndGenerateRequest.builder()
                .input(RetrieveAndGenerateInput.builder()
                        .text(text)
                        .build())  
                .retrieveAndGenerateConfiguration(RetrieveAndGenerateConfiguration.builder()
                        .knowledgeBaseConfiguration(KnowledgeBaseRetrieveAndGenerateConfiguration.builder()
                                        .knowledgeBaseId(kbId)
                                        .modelArn(modelArn)
                                        .build() )                       
                        .type(RetrieveAndGenerateType.KNOWLEDGE_BASE)
                        .build())              
                .build())
        .get().output().text();
    }

 

    
    public static void analyzeIdWithTextract( String filePath) throws IOException {

        TextractClient textractClient = null;
        try {
                textractClient= TextractClient.builder()
                .build();
           
                // Create a Document object and import image
            Document myDoc = Document.builder()
                    .bytes(SdkBytes.fromByteArray(Files.readAllBytes(Paths.get(filePath))))
                    .build();
            
            AnalyzeIdRequest analyzeIdRequest = AnalyzeIdRequest.builder()
                    .documentPages(myDoc).build();
            
            AnalyzeIdResponse analyzeId = textractClient.analyzeID(analyzeIdRequest);
            
           // System.out.println(analyzeExpense.toString());          
            List<IdentityDocument> Docs = analyzeId.identityDocuments();
            for (IdentityDocument doc: Docs) {
               doc.identityDocumentFields().forEach((field)->{
                  System.out.println(field.toString());
               });;
            }
            
            
        } 
        catch (TextractException e) {

            System.err.println(e.getMessage());
            
        }
        finally
        {
                textractClient.close();
        }
    }



    public static ArrayList<String> invokeCohere(String prompt) {
        var nativeRequestTemplate = """
                {
                    "message": "{{prompt}}",
                    "temperature": 1,
                    "return_prompt": true
                }""";


        String nativeRequest = nativeRequestTemplate.replace("{{prompt}}", prompt);

        try(BedrockRuntimeClient client = initClient()) {

            InvokeModelResponse response = client.invokeModel(request -> request
                    .body(SdkBytes.fromUtf8String(nativeRequest))
                    .modelId("cohere.command-r-plus-v1:0")
            );

            var responseBody = new JSONObject(response.body().asUtf8String());

            var text = new JSONPointer("/text").queryFrom(responseBody).toString();

            String jsonText = text.substring(text.indexOf("```json") + 7, text.lastIndexOf("```"));
            JSONArray array = new JSONArray(jsonText);
            ArrayList<String> strings = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++) {
                String string = array.getString(i);
                strings.add(string);
            }

            return strings;

        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
    }
}
