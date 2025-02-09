package org.example.backend.service;

import org.example.backend.model.Conversation;
import org.example.backend.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    // Sauvegarder une conversation
    public Conversation saveConversation(LocalDateTime startTime, LocalDateTime endTime, String status, String messages) {
        Conversation conversation = new Conversation();
        conversation.setStartTime(startTime);
        conversation.setEndTime(endTime);
        conversation.setStatus(status);
        conversation.setMessages(messages);
        return conversationRepository.save(conversation);
    }

    // Récupérer les conversations d'un utilisateur
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }
}
