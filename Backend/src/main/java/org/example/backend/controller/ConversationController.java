package org.example.backend.controller;

import org.example.backend.model.Conversation;
import org.example.backend.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

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
}
