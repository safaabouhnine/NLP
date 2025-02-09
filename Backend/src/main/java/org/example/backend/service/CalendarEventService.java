package org.example.backend.service;

import org.example.backend.model.CalendarEvent;
import org.example.backend.repository.CalendarEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;

    public CalendarEventService(CalendarEventRepository calendarEventRepository) {
        this.calendarEventRepository = calendarEventRepository;
    }

    // Récupérer tous les événements d'un utilisateur
    public List<CalendarEvent> getEventsByUser(Long userId) {
        return calendarEventRepository.findByUserId(userId);
    }

    // Ajouter un nouvel événement
    public CalendarEvent addEvent(CalendarEvent event) {
        return calendarEventRepository.save(event);
    }

    // Supprimer un événement par ID
    public void deleteEvent(Long eventId) {
        calendarEventRepository.deleteById(eventId);
    }
}