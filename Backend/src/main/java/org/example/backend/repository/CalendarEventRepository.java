package org.example.backend.repository;

import org.example.backend.model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.backend.model.EventType;

import java.util.List;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    // Méthode pour trouver tous les événements d'un utilisateur
    List<CalendarEvent> findByUserId(Long userId);

    // Méthode pour trouver les événements par type (optionnelle)
    List<CalendarEvent> findByUserIdAndEventType(Long userId, EventType eventType);
}