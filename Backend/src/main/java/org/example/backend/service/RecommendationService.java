package org.example.backend.service;

import org.example.backend.model.*;
import org.example.backend.repository.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecommendationService {

    private final NLP_analysisRepository nlpAnalysisRepository;
    private final AdviceService adviceService;
    private final YouTubeService youTubeService;
    private final MeetingService meetingService;
    private final VideoRecRepository videoRecRepository;

    public RecommendationService(NLP_analysisRepository nlpAnalysisRepository,
                                 AdviceService adviceService,
                                 YouTubeService youTubeService,
                                 MeetingService meetingService,
                                 VideoRecRepository videoRecRepository) {
        this.nlpAnalysisRepository = nlpAnalysisRepository;
        this.adviceService = adviceService;
        this.youTubeService = youTubeService;
        this.meetingService = meetingService;
        this.videoRecRepository = videoRecRepository;
    }

    public Map<String, Object> generateRecommendation(Long nlpAnalysisId, Long userId, Long psychologueId) {
        Map<String, Object> recommendation = new HashMap<>();

        // Récupérer l'analyse NLP
        NLP_analysis nlpAnalysis = nlpAnalysisRepository.findById(nlpAnalysisId)
                .orElseThrow(() -> new RuntimeException("NLP Analysis not found"));

        String stressLevel = nlpAnalysis.getStressLevel();

        switch (stressLevel.toLowerCase()) {
            case "low":
                // Récupérer des conseils pour un stress faible
                List<Advice> advices = adviceService.getAllAdvices();
                recommendation.put("advices", advices);
                break;

            case "medium":
                // Générer et sauvegarder des vidéos dynamiques pour un stress moyen
                List<VideoRec> generatedVideos = youTubeService.saveVideosForStress("relaxation", 5); // Ex. 5 vidéos
                recommendation.put("videos", generatedVideos);
                break;

            case "high":
                // Planifier une réunion pour un stress élevé
                if (psychologueId != null) {
                    LocalDateTime startTime = LocalDateTime.now().plusDays(1);
                    User student = new User();
                    try {
                        // Injecter l'ID dans l'objet User via réflexion
                        Field idField = User.class.getDeclaredField("id");
                        idField.setAccessible(true);
                        idField.set(student, userId);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException("Erreur lors de l'injection de l'ID dans User", e);
                    }
                    Meeting meeting = meetingService.scheduleMeeting(userId, psychologueId, startTime, student);
                    recommendation.put("meeting", meeting);
                } else {
                    recommendation.put("error", "Psychologue ID requis pour un stress élevé");
                }
                break;

            default:
                recommendation.put("error", "Niveau de stress non valide");
        }

        return recommendation;
    }
}