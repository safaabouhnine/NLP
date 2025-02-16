package org.example.backend.controller;

import org.example.backend.model.Conversation;
import org.example.backend.repository.ConversationRepository;
import org.example.backend.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "http://localhost:5173")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;
    @Autowired
    private ConversationRepository conversationRepository;

    @PostMapping("/save")
    public ResponseEntity<Conversation> saveConversation(@RequestBody Map<String, Object> requestBody) {
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime").toString());
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime").toString());
        String status = requestBody.get("status").toString();
        String messages = requestBody.get("messages").toString();

        Conversation conversation = conversationService.saveConversation(startTime, endTime, status, messages);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping
    public ResponseEntity<List<Conversation>> getAllConversations() {
        List<Conversation> conversations = conversationService.getAllConversations();
        return ResponseEntity.ok(conversations);
    }
    @GetMapping("/latest")
    public ResponseEntity<Conversation> getLatestConversation() {
        System.out.println("🔍 Récupération de la dernière conversation pour l'utilisateur ID: ");

        Optional<Conversation> lastConversation = conversationRepository.findLastConversation();
        if (lastConversation.isPresent()) {
            return ResponseEntity.ok(lastConversation.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
