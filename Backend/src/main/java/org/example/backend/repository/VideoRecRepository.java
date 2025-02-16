package org.example.backend.repository;

import org.example.backend.model.VideoRec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRecRepository extends JpaRepository<VideoRec, Long> {
    @Query("SELECT v FROM VideoRec v WHERE LOWER(v.title) LIKE %:keyword% OR LOWER(v.description) LIKE %:keyword%")
    List<VideoRec> findVideosForRelaxation(@Param("keyword") String keyword);
}