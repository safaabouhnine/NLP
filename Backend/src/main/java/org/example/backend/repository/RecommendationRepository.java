package org.example.backend.repository;

import org.example.backend.model.Recommendation;
import org.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    // Récupérer les recommandations d'un utilisateur spécifique
    Optional<Recommendation> findByUser_IdAndType(Long userId, String type);


    List<Recommendation> findByUser_Id(Long userId);
}
