package org.example.backend.repository;

import org.example.backend.model.VideoRec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRecRepository extends JpaRepository<VideoRec, Long> {
}