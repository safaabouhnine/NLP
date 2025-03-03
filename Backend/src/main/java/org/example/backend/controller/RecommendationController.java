
package org.example.backend.controller;

import org.example.backend.model.CalendarEvent;
import org.example.backend.model.NLP_analysis;
import org.example.backend.model.Recommendation;
import org.example.backend.repository.NLP_analysisRepository;
import org.example.backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("/user/{userId}/with-events")
    public ResponseEntity<List<Map<String, Object>>> getUserRecommendationsWithEvents(@PathVariable Long userId) {
        try {

            List<Recommendation> recommendations = recommendationService.getRecommendationsByUser(userId);
            if (recommendations.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList()); // Retourne une liste vide au lieu d'une erreur
            }
            List<Map<String, Object>> response = recommendations.stream().map(rec -> {
                Map<String, Object> recMap = new HashMap<>();
                recMap.put("id", rec.getIdR());
                recMap.put("type", rec.getType());
                recMap.put("description", rec.getDescription());
                recMap.put("viewed", rec.getViewed());
                recMap.put("dateRec", rec.getDateRec());

                // Ajoute les informations de l'événement lié (si existe)
                if (rec.getCalendarEvent() != null) {
                    Map<String, Object> eventMap = new HashMap<>();
                    eventMap.put("eventId", rec.getCalendarEvent().getId());
                    eventMap.put("title", rec.getCalendarEvent().getTitle());
                    eventMap.put("start", rec.getCalendarEvent().getStartTime());
                    eventMap.put("end", rec.getCalendarEvent().getEndTime());
                    eventMap.put("type", rec.getCalendarEvent().getEventType().toString());
                    eventMap.put("description", rec.getCalendarEvent().getDescription());
                    recMap.put("event", eventMap);
                }

                return recMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la récupération des recommandations : " + e.getMessage());
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

}
