package org.example.backend.service;

import org.example.backend.model.*;
import org.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
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
    private final CalendarEventService calendarEventService;
    private final CalendarEventRepository calendarEventRepository;
    @Autowired
    private RecommendationRepository recommendationRepository;
    public RecommendationService(NLP_analysisRepository nlpAnalysisRepository,
                                 AdviceService adviceService,
                                 YouTubeService youTubeService,
                                 MeetingService meetingService,
                                 VideoRecRepository videoRecRepository,
                                 PsychologueService psychologueService,
                                 CalendarEventService calendarEventService,
                                 CalendarEventRepository calendarEventRepository) {
        this.nlpAnalysisRepository = nlpAnalysisRepository;
        this.adviceService = adviceService;
        this.youTubeService = youTubeService;
        this.meetingService = meetingService;
        this.videoRecRepository = videoRecRepository;
        this.psychologueService = psychologueService;
        this.calendarEventService = calendarEventService;
        this.calendarEventRepository = calendarEventRepository;
    }

    public void saveRecommendation(Long userId, String stressLevel) {
        System.out.println("📌 Début de l'enregistrement de la recommandation pour User ID: " + userId);
        String typeRec;
        String description;

        switch (stressLevel.toLowerCase()) {
            case "high":
                typeRec = "Meeting";
                description = "Rencontre avec un psychologue.";
                break;
            case "medium":
                typeRec = "Video";
                description = "Regardez une vidéo de relaxation.";
                break;
            default:
                typeRec = "Advice";
                description = "Consultez des conseils pour réduire votre stress.";
                break;
        }

        // Vérifier si l'utilisateur a bien une NLP_analysis
        NLP_analysis lastAnalysis = nlpAnalysisRepository.findTopByStudent_IdOrderByTimestampDesc(userId);
        if (lastAnalysis == null || lastAnalysis.getStudent() == null) {
            System.out.println("❌ L'utilisateur n'a pas d'analyse NLP associée !");
            return;
        }
        Optional<Recommendation> existingRec = recommendationRepository.findByUser_IdAndType(lastAnalysis.getStudent().getId(), typeRec);

        if (existingRec.isPresent()) {
            System.out.println("⚠️ Recommandation déjà existante pour cet utilisateur et ce type, pas besoin d'enregistrer.");
            return;
        }

        // Créer et enregistrer la recommandation
        Recommendation recommendation = new Recommendation();
        recommendation.setUser(lastAnalysis.getStudent()); // Associe l'utilisateur trouvé via NLP Analysis
        recommendation.setType(typeRec);
        recommendation.setDescription(description);
        recommendation.setViewed(false); // Par défaut, elle n'a pas été vue
        recommendation.setDateRec(LocalDate.now()); // Date actuelle

        // Création de l'événement lié
        CalendarEvent event = new CalendarEvent();
        event.setTitle("Recommandation: " + typeRec);
        event.setDescription(description);
        event.setStartTime(LocalDateTime.now().plusDays(1));
        event.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
        event.setEventType(EventType.valueOf(typeRec.toUpperCase()));
        event.setUser(lastAnalysis.getStudent());

        CalendarEvent savedEvent = calendarEventService.addEvent(event);

        // Liaison entre la recommandation et l'événement
        recommendation.setCalendarEvent(savedEvent);

        try {
            recommendationRepository.save(recommendation);
            recommendationRepository.flush();
            System.out.println("✅ Étape 3 : Recommandation enregistrée avec succès !");
        } catch (Exception e) {
            System.out.println("❌ ERREUR: Impossible d'enregistrer la recommandation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, Object> generateRecommendation(Long nlpAnalysisId, Long userId, Long psychologueId) {
        System.out.println("🔹 Début de la génération des recommandations pour l'utilisateur ID: " + userId);
        Map<String, Object> recommendation = new HashMap<>();

        // Récupérer l'analyse NLP
        NLP_analysis nlpAnalysis = nlpAnalysisRepository.findById(nlpAnalysisId)
                .orElseThrow(() -> new RuntimeException("NLP Analysis not found"));
        System.out.println("✅ Analyse NLP récupérée: " + nlpAnalysis.getId());
        String stressLevel = nlpAnalysis.getStressLevel();
        System.out.println("📌 Niveau de stress: " + stressLevel);

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

                // 🔔 Répartir les vidéos sur plusieurs jours (ex: une vidéo par jour)
                LocalDateTime startDate = LocalDateTime.now().plusDays(1); // Commence demain

                // 🔔 Enregistrer chaque vidéo comme une recommandation et créer des événements
                for (int i = 0; i < generatedVideos.size(); i++) {
                    VideoRec video = generatedVideos.get(i);

                    // Crée la recommandation
                    Recommendation recommendationVideo = new Recommendation();
                    recommendationVideo.setUser(nlpAnalysis.getStudent());
                    recommendationVideo.setType("Video");
                    recommendationVideo.setDescription("Regardez cette vidéo de relaxation.");
                    recommendationVideo.setViewed(false);
                    recommendationVideo.setDateRec(LocalDate.now().plusDays(i)); // Date différente pour chaque vidéo
                    recommendationRepository.save(recommendationVideo);

                    // 🔹 Crée l'événement pour cette vidéo avec une date décalée
                    CalendarEvent event = new CalendarEvent();
                    event.setTitle("Vidéo recommandée : " + video.getTitle());
                    event.setDescription("Regardez la vidéo : " + video.getTitle());
                    event.setStartTime(startDate.plusDays(i).withHour(17).withMinute(0)); // Chaque vidéo un jour après l'autre à 17h
                    event.setEndTime(startDate.plusDays(i).withHour(17).withMinute(30));  // Durée de 30 minutes
                    event.setEventType(EventType.VIDEO_RECOMMENDATION);
                    event.setUser(nlpAnalysis.getStudent());
                    event.setLink(video.getVideoLink());
                    calendarEventRepository.save(event);

                    // Lier l'événement à la recommandation
                    recommendationVideo.setCalendarEvent(event);
                    recommendationRepository.save(recommendationVideo); // Mise à jour avec l'événement lié
                }
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

    public List<Recommendation> getRecommendationsByUser(Long userId) {
        return recommendationRepository.findByUser_Id(userId);
    }


}