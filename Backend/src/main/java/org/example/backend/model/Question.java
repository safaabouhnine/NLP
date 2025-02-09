package org.example.backend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idq;

    @Column(name = "question", nullable = false)
    @NotBlank(message = "La question ne peut pas être vide")
    @Size(min = 10, max = 255, message = "La question doit contenir entre 10 et 255 caractères")
    private String question;

    // Getters et Setters
    public Integer getIdq() {
        return idq;
    }

    public void setIdq(Integer idq) {
        this.idq = idq;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

