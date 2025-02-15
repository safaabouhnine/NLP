package org.example.backend.controller;

import org.example.backend.model.Conversation;
import org.example.backend.model.NLP_analysis;
import org.example.backend.model.User;
import org.example.backend.repository.ConversationRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.NLP_analysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        String text = requestBody.get("text");
        Long studentId = Long.parseLong(requestBody.get("studentId"));
        Long conversationId = Long.parseLong(requestBody.get("conversationId"));

        User student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Étudiant introuvable"));
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() -> new RuntimeException("Conversation introuvable"));

        NLP_analysis analysis = nlpAnalysisService.analyzeText(text, student, conversation);

        return ResponseEntity.ok(analysis);
    }

    @GetMapping
    public ResponseEntity<List<NLP_analysis>> getNlpAnalysesByUser(@RequestParam Long userId) {
        try {
            List<NLP_analysis> analyses = nlpAnalysisService.getAnalysesByUserId(userId);
            return ResponseEntity.ok(analyses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
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
