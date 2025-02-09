package org.example.backend.repository;

import org.example.backend.model.NLP_analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NLP_analysisRepository extends JpaRepository<NLP_analysis, Long> {
}
