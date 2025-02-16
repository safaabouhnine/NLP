package org.example.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.model.Conversation;
import org.example.backend.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;



@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    // Sauvegarder une conversation
    public Conversation saveConversation(LocalDateTime startTime, LocalDateTime endTime, String status, String messagesJson) {
        Conversation conversation = new Conversation();
        conversation.setStartTime(startTime);
        conversation.setEndTime(endTime);
        conversation.setStatus(status);

        try {
            // ✅ Convertir le JSON en List<Map<String, Object>>
            List<Map<String, Object>> messages = objectMapper.readValue(messagesJson, new TypeReference<>() {});
            conversation.setMessages(messages);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de conversion JSON → List<Map<String, Object>>", e);
        }

        return conversationRepository.save(conversation);
    }

    // Récupérer les conversations d'un utilisateur
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }
}
