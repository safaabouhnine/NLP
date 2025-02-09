package org.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ida;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "L'utilisateur est obligatoire")
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @NotNull(message = "La question est obligatoire")
    private Question question;

    @NotNull(message = "La valeur de la réponse ne peut pas être nulle")
    @Min(value = 1, message = "La valeur doit être au minimum 1")
    @Max(value = 6, message = "La valeur doit être au maximum 6")
    private Integer value;

    // Getters et Setters

    public Integer getIda() {
        return ida;
    }

    public void setIda(Integer ida) {
        this.ida = ida;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
