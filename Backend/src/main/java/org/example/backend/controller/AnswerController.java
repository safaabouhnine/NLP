package org.example.backend.controller;

import org.example.backend.model.Answer;
import org.example.backend.model.Question;
import org.example.backend.model.User;
import org.example.backend.repository.AnswerRepository;
import org.example.backend.repository.QuestionRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
//@RequestMapping("/api/answers")
//public class AnswerController {
//
//    private final AnswerRepository answerRepository;
//    private final UserRepository userRepository;
//    private final QuestionRepository questionRepository;
//
//    public AnswerController(AnswerRepository answerRepository, UserRepository userRepository, QuestionRepository questionRepository) {
//        this.answerRepository = answerRepository;
//        this.userRepository = userRepository;
//        this.questionRepository = questionRepository;
//    }
//
//    @PostMapping
//    public ResponseEntity<String> saveAnswers(@RequestBody List<Answer> answers) {
//        for (Answer answer : answers) {
//            // Récupère User et Question à partir de la base de données
//            User user = userRepository.findById(answer.getUser().getId())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            Question question = questionRepository.findById(answer.getQuestion().getIdq())
//                    .orElseThrow(() -> new RuntimeException("Question not found"));
//
//            // Associe les entités récupérées à l'objet Answer
//            answer.setUser(user);
//            answer.setQuestion(question);
//        }
//
//        // Sauvegarde toutes les réponses
//        answerRepository.saveAll(answers);
//
//        int totalScore = answers.stream()
//                .mapToInt(Answer::getValue)
//                .sum();
//
//        return ResponseEntity.ok("Réponses enregistrées avec succès. Score total : " + totalScore);
//    }
//}
@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public AnswerController(AnswerRepository answerRepository, UserRepository userRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> saveAnswers(@RequestBody List<Answer> answers) {
        for (Answer answer : answers) {
            User user = userRepository.findById(answer.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Question question = questionRepository.findById(answer.getQuestion().getIdq())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            answer.setUser(user);
            answer.setQuestion(question);
        }

        answerRepository.saveAll(answers);

        int totalScore = answers.stream()
                .mapToInt(Answer::getValue)
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Réponses enregistrées avec succès");
        response.put("totalScore", totalScore);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/stress-level")
    public ResponseEntity<Map<String, Object>> getStressLevel(@PathVariable Long userId) {
        // Trouver les réponses pour l'utilisateur donné
        List<Answer> userAnswers = answerRepository.findByUserId(userId);

        if (userAnswers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Aucun score trouvé pour cet utilisateur"));
        }

        // Calculer le score total
        int totalScore = userAnswers.stream()
                .mapToInt(Answer::getValue)
                .sum();

        // Retourner le score dans la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("stressLevel", totalScore);

        return ResponseEntity.ok(response);
    }

}
