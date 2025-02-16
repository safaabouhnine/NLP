package org.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


import java.time.LocalDateTime;
@Entity
@Table(name = "nlp_analysis")
@JsonIgnoreProperties({"conversation"})
public class NLP_analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User student;

    private String feelings;
    private String stressLevel;
    private LocalDateTime timestamp;

    @Column(columnDefinition = "TEXT")
    private String chatbotResponse;

    @ManyToOne
    @JsonIgnore
    private Conversation conversation;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long idNLP) {
        this.id = idNLP;
    }

    public String getFeelings() {
        return feelings;
    }

    public void setFeelings(String feelings) {
        this.feelings = feelings;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(String stressLevel) {
        this.stressLevel = stressLevel;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getChatbotResponse() {
        return chatbotResponse;
    }

    public void setChatbotResponse(String chatbotResponse) {
        this.chatbotResponse = chatbotResponse;
    }
}
