package org.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.example.backend.util.JsonConverter;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idC;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    //ajouter message
//@Lob
//private String messages;
    @Convert(converter = JsonConverter.class) // ✅ Utilisation du convertisseur JSON
    @Column(columnDefinition = "TEXT") // ✅ Stockage sous forme de texte
    @JsonProperty("messages")
    private List<Map<String, Object>> messages;// ✅ Stocke le JSON sous forme de liste de messages

    @OneToMany(mappedBy = "conversation")
    @JsonIgnoreProperties("conversation")
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
    public List<Map<String, Object>> getMessages() {
        return messages;
    }

    public void setMessages(List<Map<String, Object>> messages) {
        this.messages = messages;
    }
}
