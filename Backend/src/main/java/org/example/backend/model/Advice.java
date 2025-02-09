package org.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "advices")
public class Advice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAd;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    // Getters et setters
    public Long getIdAd() {
        return idAd;
    }

    public void setIdAd(Long idAd) {
        this.idAd = idAd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}