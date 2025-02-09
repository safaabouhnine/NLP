package org.example.backend.repository;

import org.example.backend.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findByUserId(Long userId); // Définir la méthode ici

}
