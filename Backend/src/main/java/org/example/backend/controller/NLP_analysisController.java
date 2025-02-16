package org.example.backend.controller;

import org.example.backend.model.Conversation;
import org.example.backend.model.NLP_analysis;
import org.example.backend.model.User;
import org.example.backend.repository.ConversationRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.NLP_analysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/nlp")
public class NLP_analysisController {

    @Autowired
    private NLP_analysisService nlpAnalysisService;
    @Autowired
    private UserRepository studentRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    @PostMapping("/analyze")
    public ResponseEntity<NLP_analysis> analyzeText(@RequestBody Map<String, String> requestBody) {
        System.out.println("🚀 Requête reçue sur /analyze !");
        System.out.println("📩 Payload reçu: " + requestBody);

        if (requestBody == null || requestBody.isEmpty()) {
            System.out.println("❌ ERREUR : Le payload est vide !");
            return ResponseEntity.badRequest().build();
        }

        String text = requestBody.get("text");
        if (text == null || text.trim().isEmpty()) {
            System.out.println("❌ ERREUR : Aucun texte fourni !");
            return ResponseEntity.badRequest().build();
        }

        Long studentId;
        try {
            studentId = Long.parseLong(requestBody.get("studentId"));
            System.out.println("✅ Student ID récupéré : " + studentId);
        } catch (NumberFormatException e) {
            System.out.println("❌ ERREUR : Student ID invalide !");
            return ResponseEntity.badRequest().build();
        }

        // 📌 Récupération de l'étudiant
        User student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("❌ Étudiant introuvable !"));

        // 📌 Récupération de la conversation
        Long conversationId;
        try {
            conversationId = Long.parseLong(requestBody.get("conversationId"));
            System.out.println("✅ Conversation ID reçu du front: " + conversationId);
        } catch (NumberFormatException e) {
            System.out.println("❌ ERREUR : Conversation ID invalide !");
            return ResponseEntity.badRequest().build();
        }

        Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
        if (conversationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Conversation conversation = conversationOpt.get();
        System.out.println("✅ Utilisation de la conversation ID: " + conversation.getIdC());

        // 📌 Lancer l'analyse NLP et stocker en base
        NLP_analysis analysis = nlpAnalysisService.analyzeText(text, student, conversation);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/latest/{userId}")
    public ResponseEntity<NLP_analysis> getLatestNLPAnalysis(@PathVariable Long userId) {
        NLP_analysis latestAnalysis = nlpAnalysisService.findLatestByUserId(userId);
        if (latestAnalysis != null) {
            return ResponseEntity.ok(latestAnalysis);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

