package org.example.backend.repository;

import org.example.backend.model.Psychologue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsychologueRepository extends JpaRepository<Psychologue, Long> {
}