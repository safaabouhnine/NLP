package org.example.backend.controller;

import org.example.backend.model.CalendarEvent;
import org.example.backend.service.CalendarEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calendar")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    // Endpoint pour récupérer tous les événements d'un utilisateur
    @GetMapping("/user/{userId}")
    public List<CalendarEvent> getEventsByUser(@PathVariable Long userId) {
        return calendarEventService.getEventsByUser(userId);
    }

    // Endpoint pour ajouter un nouvel événement
    @PostMapping("/add")
    public ResponseEntity<CalendarEvent> addEvent(@RequestBody CalendarEvent event) {
        return ResponseEntity.ok(calendarEventService.addEvent(event));
    }

    // Endpoint pour supprimer un événement
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        calendarEventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    // Nouveau endpoint pour récupérer les événements au format structuré pour le calendrier
    @GetMapping("/user/{userId}/calendar")
    public List<Map<String, Object>> getCalendarEventsByUser(@PathVariable Long userId) {
        List<CalendarEvent> events = calendarEventService.getEventsByUser(userId);
        return events.stream()
                .map(event -> {
                    Map<String, Object> eventMap = new HashMap<>();
                    eventMap.put("id", event.getId());
                    eventMap.put("title", event.getTitle());
                    eventMap.put("start", event.getStartTime());
                    eventMap.put("end", event.getEndTime());
                    eventMap.put("type", event.getEventType().toString());
                    eventMap.put("description", event.getDescription());
                    eventMap.put("link", event.getLink());
                    return eventMap;
                })
                .collect(Collectors.toList());
    }

}