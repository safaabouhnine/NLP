package org.example.backend.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idC;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
//ajouter message
@Lob
private String messages;
    @OneToMany(mappedBy = "conversation")
    private List<NLP_analysis> nlpAnalyses;

    public Long getIdC() {
        return idC;
    }

    public void setIdC(Long idC) {
        this.idC = idC;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NLP_analysis> getNlpAnalyses() {
        return nlpAnalyses;
    }

    public void setNlpAnalyses(List<NLP_analysis> nlpAnalyses) {
        this.nlpAnalyses = nlpAnalyses;
    }
// Getters et Setters
public String getMessages() {
    return messages;
}

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
