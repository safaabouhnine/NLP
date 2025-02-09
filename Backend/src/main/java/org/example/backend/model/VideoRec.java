package org.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "videos_rec")
public class VideoRec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVR;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "VideoLink", nullable = false)
    private String videoLink;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    // Getters et setters
    public Long getIdVR() {
        return idVR;
    }

    public void setIdVR(Long idVR) {
        this.idVR = idVR;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}