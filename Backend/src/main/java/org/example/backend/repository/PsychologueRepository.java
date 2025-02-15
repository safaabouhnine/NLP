package org.example.backend.repository;

import org.example.backend.model.Psychologue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PsychologueRepository extends JpaRepository<Psychologue, Long> {
    @Query("SELECT p FROM Psychologue p WHERE p.disponibilite IS NOT EMPTY")
    List<Psychologue> findAvailablePsychologues();
    List<Psychologue> findAll();
}