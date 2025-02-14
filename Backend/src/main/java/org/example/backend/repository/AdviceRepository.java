package org.example.backend.repository;

import org.example.backend.model.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdviceRepository extends JpaRepository<Advice, Long> {
    @Query(value = "SELECT a FROM Advice a WHERE a.user.id = :userId")
    List<Advice> findByUserId(@Param("userId") Long userId);

}