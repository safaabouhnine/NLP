package org.example.backend.repository;

import org.example.backend.model.NLP_analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface NLP_analysisRepository extends JpaRepository<NLP_analysis, Long> {
    List<NLP_analysis> findByStudentId(Long studentId);
    @Query("SELECT n FROM NLP_analysis n WHERE n.student.id = :studentId ORDER BY n.timestamp DESC")
    Optional<NLP_analysis> findLastByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT n FROM NLP_analysis n ORDER BY n.id DESC LIMIT 1")
    NLP_analysis findLastInserted();

    NLP_analysis findTopByStudent_IdOrderByTimestampDesc(Long userId);


}
