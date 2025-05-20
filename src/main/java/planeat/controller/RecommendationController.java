package planeat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import planeat.dto.PreferenceRequest;
import planeat.dto.RecommendationResponse;
import planeat.service.RecommendationService;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public RecommendationResponse recommend(@RequestBody PreferenceRequest request) {
        return recommendationService.recommend(request);
    }
}