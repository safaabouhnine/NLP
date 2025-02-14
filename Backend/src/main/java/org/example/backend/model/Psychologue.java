package org.example.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "psychologue")
public class Psychologue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idP;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @ElementCollection
    private List<String> disponibilite;

    @Column(name = "PhoneNumber", nullable = false)
    private String phoneNumber;

    // Getters et setters
    public Long getIdP() {
        return idP;
    }

    public void setIdP(Long idP) {
        this.idP = idP;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(List<String> disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}