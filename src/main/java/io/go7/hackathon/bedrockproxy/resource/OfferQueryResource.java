package io.go7.hackathon.bedrockproxy.resource;

import lombok.RequiredArgsConstructor;

import io.go7.hackathon.bedrockproxy.beans.OfferQueryResponse;
import io.go7.hackathon.bedrockproxy.service.OfferQueryService;
import io.go7.hackathon.bedrockproxy.utils.BedrockHelper;

//@RestController
//@RequestMapping("/offer-query")
@RequiredArgsConstructor
public class OfferQueryResource {

    private final OfferQueryService offerQueryService;


//    @GetMapping("/echo")
    public String echo() {
        return "echo";
    }

//    @GetMapping("/{prompt}")
//    public OfferQueryResponse getPromptResult(@PathVariable String prompt) {
//        return offerQueryService.getOfferQueryResponse(prompt);
//    }
//
//    @PostMapping()
//    public OfferQueryResponse postPrompt(@RequestBody String prompt) {
//        return offerQueryService.getOfferQueryResponse(prompt);
//    }

}
