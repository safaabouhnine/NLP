package org.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;


@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idR;

    @ManyToOne
    @JoinColumn(name = "Std", nullable = false)
    private User user;

    @Column(name = "Type_Rec", nullable = false)
    private String type;

    @Column(name = "Description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "Viewed", nullable = false)
    private Boolean viewed = false;

    @Column(name = "Date_Rec", nullable = false)
    private LocalDate dateRec = LocalDate.now();

    @OneToOne
    @JoinColumn(name = "calendar_event_id") // Colonne de liaison dans la table 'recommendations'
    private CalendarEvent calendarEvent;


    // Getters et setters
    public Long getIdR() {
        return idR;
    }

    public void setIdR(Long idR) {
        this.idR = idR;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public LocalDate getDateRec() {
        return dateRec;
    }

    public void setDateRec(LocalDate dateRec) {
        this.dateRec = dateRec;
    }

    public CalendarEvent getCalendarEvent() {
        return calendarEvent;
    }

    public void setCalendarEvent(CalendarEvent calendarEvent) {
        this.calendarEvent = calendarEvent;
    }

}