package org.example.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate; // Ajout du champ createdDate

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Relation avec User

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
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}