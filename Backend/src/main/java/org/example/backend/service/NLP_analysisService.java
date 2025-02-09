package org.example.backend.service;


import org.example.backend.model.Conversation;
import org.example.backend.model.NLP_analysis;
import org.example.backend.model.User;
import org.example.backend.repository.NLP_analysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class NLP_analysisService {

    @Autowired
    private NLP_analysisRepository nlpAnalysisRepository;

    @Autowired
    private RestTemplate restTemplate;

    public NLP_analysis analyzeText(String text, User student, Conversation conversation) {
        // URL du service Python
        String apiUrl = "http://localhost:5000/analyze";

        // Préparer la requête
        Map<String, String> request = new HashMap<>();
        request.put("text", text);

        // Envoyer la requête POST
        Map<String, Object> response = restTemplate.postForObject(apiUrl, request, Map.class);

        // Récupérer les résultats de l'analyse
        String feelings = (String) response.get("feelings");
        String stressLevel = (String) response.get("stressLevel");

        String chatbotApiUrl = "http://localhost:5173/chat";
        Map<String, String> chatbotRequest = new HashMap<>();
        chatbotRequest.put("text", text);

        Map<String, Object> chatbotResponse = restTemplate.postForObject(chatbotApiUrl, chatbotRequest, Map.class);
        String chatbotMessage = (String) chatbotResponse.get("chatbot_response");
        // Créer une nouvelle entité NLPAnalysis
        NLP_analysis analysis = new NLP_analysis();
        analysis.setFeelings(feelings);
        analysis.setStressLevel(stressLevel);
        analysis.setChatbotResponse(chatbotMessage);
        analysis.setTimestamp(LocalDateTime.now());
        analysis.setStudent(student);
        analysis.setConversation(conversation);

        // Sauvegarder dans la base de données
        return nlpAnalysisRepository.save(analysis);
    }
}