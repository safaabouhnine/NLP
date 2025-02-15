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
    private final PsychologueService psychologueService;

    public RecommendationService(NLP_analysisRepository nlpAnalysisRepository,
                                 AdviceService adviceService,
                                 YouTubeService youTubeService,
                                 MeetingService meetingService,
                                 VideoRecRepository videoRecRepository,
                                 PsychologueService psychologueService) {
        this.nlpAnalysisRepository = nlpAnalysisRepository;
        this.adviceService = adviceService;
        this.youTubeService = youTubeService;
        this.meetingService = meetingService;
        this.videoRecRepository = videoRecRepository;
        this.psychologueService = psychologueService;
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
                List<Advice> advices = adviceService.getAdvicesByUserId(userId);
                recommendation.put("advices", advices);
                break;

            case "medium":
                // Générer et sauvegarder des vidéos dynamiques pour un stress moyen
                List<VideoRec> generatedVideos = youTubeService.saveVideosForStress("relaxation", 5); // Ex. 5 vidéos
                recommendation.put("videos", generatedVideos);
                break;

            case "high":
                List<Psychologue> psychologues = psychologueService.getPsychologues(); // Assurez-vous que cette méthode existe
                if (!psychologues.isEmpty()) {
                    Psychologue selectedPsychologue = psychologues.get(0); // Exemple : sélection du premier psychologue
                    LocalDateTime startTime = LocalDateTime.now().plusDays(1);

                    User student = new User();
                    student.setId(userId); // Utilisez le setter

                    Meeting meeting = meetingService.scheduleMeeting(userId, selectedPsychologue.getIdP(), startTime, student);
                    recommendation.put("meeting", meeting);
                } else {
                    recommendation.put("error", "Aucun psychologue disponible");
                }
                break;


            default:
                recommendation.put("error", "Niveau de stress non valide");
        }

        return recommendation;
    }
}