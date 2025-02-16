
package org.example.backend.controller;

import org.example.backend.model.NLP_analysis;
import org.example.backend.repository.NLP_analysisRepository;
import org.example.backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final NLP_analysisRepository nlpAnalysisRepository;
    public RecommendationController(RecommendationService recommendationService , NLP_analysisRepository nlpAnalysisRepository) {
        this.recommendationService = recommendationService;
        this.nlpAnalysisRepository = nlpAnalysisRepository;
    }

    @GetMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateRecommendations(
            @RequestParam Long nlpAnalysisId,
            @RequestParam Long userId,
            @RequestParam(required = false) Long psychologueId) {
        try {
            System.out.println("🔹 Appel de /generate avec NLP Analysis ID=" + nlpAnalysisId + " et userId=" + userId);
            NLP_analysis nlpAnalysis = nlpAnalysisRepository.findById(nlpAnalysisId)
                    .orElseThrow(() -> new RuntimeException("❌ NLP Analysis non trouvé"));
            System.out.println("✅ Analyse NLP récupérée: " + nlpAnalysis.getId() + " - Stress Level: " + nlpAnalysis.getStressLevel());
            Map<String, Object> recommendations = recommendationService.generateRecommendation(nlpAnalysisId, userId, psychologueId);

            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
