
package org.example.backend.controller;

import org.example.backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateRecommendations(
            @RequestParam Long nlpAnalysisId,
            @RequestParam Long userId,
            @RequestParam(required = false) Long psychologueId) {
        try {
            Map<String, Object> recommendations = recommendationService.generateRecommendation(nlpAnalysisId, userId, psychologueId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
