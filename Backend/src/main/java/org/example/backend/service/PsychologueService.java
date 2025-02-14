package org.example.backend.service;

import org.example.backend.model.Psychologue;
import org.example.backend.repository.PsychologueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PsychologueService {
    private final PsychologueRepository psychologueRepository;

    public PsychologueService(PsychologueRepository psychologueRepository) {
        this.psychologueRepository = psychologueRepository;
    }

    public List<Psychologue> getAvailablePsychologuesForUser(Long userId) {
        // Logique pour retourner les psychologues basés sur la disponibilité
        return psychologueRepository.findAvailablePsychologues();
    }

    public List<Psychologue> getPsychologues() {
        return psychologueRepository.findAll(); // Récupérer tous les psychologues de la base de données
    }
}