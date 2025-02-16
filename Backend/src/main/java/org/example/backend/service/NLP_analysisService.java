package org.example.backend.service;


import jakarta.transaction.Transactional;
import org.example.backend.model.*;
import org.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


//@Service
//public class NLP_analysisService {
//
//    @Autowired
//    private NLP_analysisRepository nlpAnalysisRepository;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    public NLP_analysis analyzeText(String text, User student, Conversation conversation) {
//        // URL du service Python
//        String apiUrl = "http://localhost:5000/analyze";
//
//        // Préparer la requête
//        Map<String, String> request = new HashMap<>();
//        request.put("text", text);
//
//        // Envoyer la requête POST
//        Map<String, Object> response = restTemplate.postForObject(apiUrl, request, Map.class);
//
//        // Récupérer les résultats de l'analyse
//        String feelings = (String) response.get("feelings");
//        String stressLevel = (String) response.get("stressLevel");
//
//        String chatbotApiUrl = "http://localhost:5173/chat";
//        Map<String, String> chatbotRequest = new HashMap<>();
//        chatbotRequest.put("text", text);
//
//        Map<String, Object> chatbotResponse = restTemplate.postForObject(chatbotApiUrl, chatbotRequest, Map.class);
//        String chatbotMessage = (String) chatbotResponse.get("chatbot_response");
//        // Créer une nouvelle entité NLPAnalysis
//        NLP_analysis analysis = new NLP_analysis();
//        analysis.setFeelings(feelings);
//        analysis.setStressLevel(stressLevel);
//        analysis.setChatbotResponse(chatbotMessage);
//        analysis.setTimestamp(LocalDateTime.now());
//        analysis.setStudent(student);
//        analysis.setConversation(conversation);
//
//        // Sauvegarder dans la base de données
//        return nlpAnalysisRepository.save(analysis);
//    }
//}
@Service
public class NLP_analysisService {

    @Autowired
    private NLP_analysisRepository nlpAnalysisRepository;
    @Autowired
    private MeetingService meetingService; // ✅ Injection du service de gestion des meetings
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private VideoRecRepository videoRecRepository; // ✅ Injection du repository pour les vidéos

    @Autowired
    private AdviceRepository adviceRepository; // ✅ Injection du repository pour les conseils

    @Autowired
    private RecommendationRepository recommendationRepository; // ✅ Injection du repository pour les recommandations
    @Autowired
    private RestTemplate restTemplate;
    public List<NLP_analysis> getAnalysesByUserId(Long userId) {
        return nlpAnalysisRepository.findByStudentId(userId);
    }
    @Transactional
    public NLP_analysis analyzeText(String text, User student, Conversation conversation) {
        try {
            System.out.println("🔹 Début de l'analyse NLP pour l'utilisateur: " + student.getId());
            System.out.println("📩 Texte reçu: " + text);

            // 📌 Récupérer la dernière conversation
            Optional<Conversation> lastConversation = conversationRepository.findLastConversation();
            if (lastConversation.isPresent()) {
                conversation = lastConversation.get();
                System.out.println("✅ Dernière conversation récupérée avec ID: " + conversation.getIdC());
            } else {
                System.out.println("⚠️ Aucune conversation trouvée, création d'une nouvelle...");
                conversation = new Conversation();
                conversation.setStartTime(LocalDateTime.now());
                conversation.setStatus("active");
                conversation = conversationRepository.save(conversation);
                System.out.println("✅ Nouvelle conversation créée avec ID: " + conversation.getIdC());
            }

            // 📌 Appel à l'API Flask
            String apiUrl = "http://localhost:5000/api/chat";
            Map<String, String> request = new HashMap<>();
            request.put("text", text);

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                System.out.println("❌ Erreur : réponse de Flask non valide !");
                return null;
            }

            Map<String, Object> responseBody = response.getBody();
            String feelings = (String) responseBody.get("feelings");
            String stressLevel = (String) responseBody.get("stress_level");
            String chatbotMessage = (String) responseBody.get("chatbot_response");

            System.out.println("✅ Feelings: " + feelings);
            System.out.println("✅ Stress Level: " + stressLevel);
            System.out.println("✅ Chatbot Response: " + chatbotMessage);

            // 📌 Création et stockage de l'analyse NLP
            NLP_analysis analysis = new NLP_analysis();
            analysis.setFeelings(feelings);
            analysis.setStressLevel(stressLevel);
            analysis.setChatbotResponse(chatbotMessage);
            analysis.setTimestamp(LocalDateTime.now());
            analysis.setStudent(student);
            analysis.setConversation(conversation);

            NLP_analysis savedAnalysis = nlpAnalysisRepository.save(analysis);
            nlpAnalysisRepository.flush();
            System.out.println("✅ NLP Analysis enregistrée avec ID: " + savedAnalysis.getId());
            recommendationService.saveRecommendation(student.getId(), stressLevel);
            return savedAnalysis;

        } catch (Exception e) {
            System.out.println("❌ Exception attrapée: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
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
            System.out.println("✅ Recommandations générées: " + recommendations);

            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    public NLP_analysis getLastInsertedNLPAnalysis() {
        try {
            return nlpAnalysisRepository.findLastInserted();
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            System.err.println("Error fetching last inserted NLP analysis: " + e.getMessage());
            throw new RuntimeException("Failed to fetch last inserted NLP analysis", e);
        }
    }
    public NLP_analysis findLatestByUserId(Long userId) {
        return nlpAnalysisRepository.findTopByStudent_IdOrderByTimestampDesc(userId);

    }


}
