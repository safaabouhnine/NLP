package org.example.backend.service;

import org.example.backend.model.*;
import org.example.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final PsychologueRepository psychologueRepository;

    public MeetingService(MeetingRepository meetingRepository, PsychologueRepository psychologueRepository) {
        this.meetingRepository = meetingRepository;
        this.psychologueRepository = psychologueRepository;
    }

    private String generateGoogleMeetLink() {
        return "https://meet.google.com/random-link"; // Remplacez avec une vraie API si nécessaire
    }
    public List<Psychologue> getAvailablePsychologists() {
        return psychologueRepository.findAll(); // Ajoutez une condition pour la disponibilité si nécessaire
    }

    public Meeting scheduleMeeting(Long studentId, Long psychologueId, LocalDateTime dateTime, User student) {
        Optional<Psychologue> psychologue = psychologueRepository.findById(psychologueId);

        if (psychologue.isEmpty()) {
            throw new RuntimeException("Psychologue non trouvé");
        }

        Meeting meeting = new Meeting();
        meeting.setStudent(student);
        meeting.setPsychologue(psychologue.get());
        meeting.setMeetLink(generateGoogleMeetLink());
        meeting.setDateTime(dateTime);
        meeting.setStatus("Scheduled");

        return meetingRepository.save(meeting);
    }
}