package org.example.backend.repository;

import org.example.backend.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c ORDER BY c.startTime DESC LIMIT 1")
    Optional<Conversation> findLastConversation();


}