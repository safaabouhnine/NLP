package org.example.backend.service;


import org.example.backend.model.Conversation;
import org.example.backend.model.NLP_analysis;
import org.example.backend.model.User;
import org.example.backend.repository.NLP_analysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RestTemplate restTemplate;
    public List<NLP_analysis> getAnalysesByUserId(Long userId) {
        return nlpAnalysisRepository.findByStudentId(userId);
    }
    public NLP_analysis analyzeText(String text, User student, Conversation conversation) {
        // URL de l'API Python
        String apiUrl = "http://localhost:5000/api/analyze";

        // Préparer la requête
        Map<String, String> request = new HashMap<>();
        request.put("text", text);

        // Envoyer la requête POST vers Flask
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();

            // Récupération des résultats de Flask
            String feelings = (String) responseBody.get("feelings");
            String stressLevel = (String) responseBody.get("stressLevel");
            String chatbotMessage = (String) responseBody.get("chatbot_response");

            // Création de l'objet NLP_analysis
            NLP_analysis analysis = new NLP_analysis();
            analysis.setFeelings(feelings);
            analysis.setStressLevel(stressLevel);
            analysis.setChatbotResponse(chatbotMessage);
            analysis.setTimestamp(LocalDateTime.now());
            analysis.setStudent(student);
            analysis.setConversation(conversation);

            // Sauvegarde en base de données
            return nlpAnalysisRepository.save(analysis);
        } else {
            throw new RuntimeException("Erreur lors de l'appel à l'API Flask");
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
