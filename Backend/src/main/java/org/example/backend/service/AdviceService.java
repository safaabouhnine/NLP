package org.example.backend.service;

import org.example.backend.model.Advice;
import org.example.backend.repository.AdviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdviceService {

    private final AdviceRepository adviceRepository;

    public AdviceService(AdviceRepository adviceRepository) {
        this.adviceRepository = adviceRepository;
    }

    public List<Advice> getAllAdvices() {
        return adviceRepository.findAll();
    }

    public List<Advice> getAdvicesByUserId(Long userId) {
        return adviceRepository.findByUser_Id(userId);
    }

    // 🔔 Méthode pour sauvegarder un conseil en base de données
    public Advice saveAdvice(Advice advice) {
        return adviceRepository.save(advice);
    }
}