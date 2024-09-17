package io.go7.hackathon.bedrockproxy.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.go7.hackathon.bedrockproxy.utils.BedrockHelper;

@RestController
@RequestMapping("/offer-query")
public class OfferQueryResource {

    public static final String MODEL_ID = "anthropic.claude-3-sonnet-20240229-v1:0";

    @GetMapping("/echo")
    public String echo() {
        return "echo";
    }

    @GetMapping("/{prompt}")
    public String getPromptResult(@PathVariable String prompt) {
        return BedrockHelper.invokeModel(MODEL_ID, prompt);
    }

    @PostMapping()
    public String postPrompt(@RequestBody String prompt) {
        return BedrockHelper.invokeModel(MODEL_ID, prompt);
    }

}
