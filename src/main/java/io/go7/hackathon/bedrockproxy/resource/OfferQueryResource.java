package io.go7.hackathon.bedrockproxy.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;
import io.go7.hackathon.bedrockproxy.service.OfferQueryService;
import io.go7.hackathon.bedrockproxy.utils.BedrockHelper;

@RestController
@RequestMapping("/offer-query")
@RequiredArgsConstructor
public class OfferQueryResource {

    private final OfferQueryService offerQueryService;


    @GetMapping("/echo")
    public String echo() {
        return "echo";
    }

    @GetMapping("/{prompt}")
    public OfferQueryResponse getPromptResult(@PathVariable String prompt) {
        return offerQueryService.getOfferQueryResponse(prompt);
    }

    @PostMapping()
    public OfferQueryResponse postPrompt(@RequestBody String prompt) {
        return offerQueryService.getOfferQueryResponse(prompt);
    }

}
