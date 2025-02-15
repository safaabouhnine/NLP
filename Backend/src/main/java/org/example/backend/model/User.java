package org.example.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("firstName")
    @Column(name = "firstname", nullable = false)
    @NotNull(message = "First name is required")
    @Size(min = 2, message = "First name must have at least 2 characters")
    private String firstName;

    @JsonProperty("lastName")
    @Column(name = "lastname", nullable = false)
    @NotNull(message = "Last name is required")
    @Size(min = 2, message = "Last name must have at least 2 characters")
    private String lastName;

    @JsonProperty("phoneNumber")
    @Column(name = "phonenumber", nullable = false)
    @NotNull(message = "Phone number is required")
    @Pattern(
            regexp = "^(05|06|07)[0-9]{8}$",
            message = "Phone number must start with 05, 06, or 07 and have exactly 10 digits"
    )
    private String phoneNumber;

    @JsonProperty("email")
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotNull(message = "Email is required")
    private String email;


    @JsonProperty("birthDate")
    @Column(name = "birthdate")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @AssertTrue(message = "Age must be at least 18 years")
    public boolean isAtLeast18YearsOld() {
        if (birthDate == null) {
            return false;
        }
        return Period.between(birthDate, LocalDate.now()).getYears() >= 15;
    }

    @JsonProperty("gender")
    @Column(name = "gender")
    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be Male or Female")
    private String gender;

    @JsonProperty("educationLevel")
    @Column(name = "educationlevel")
    @NotNull(message = "Education level is required")
    @Size(min = 2, max = 50, message = "Education level must be between 2 and 50 characters")
    private String educationLevel;

    @JsonProperty("university")
    @Column(name = "university")
    @NotNull(message = "University is required")
    @Size(min = 2, max = 100, message = "University name must be between 2 and 100 characters")
    private String university;

    @JsonProperty("password")
    @Column(name = "password", nullable = false)
    @NotNull(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$",
            message = "Password must contain at least 8 characters, one uppercase, one lowercase, one number, and one special character"
    )
    private String password;

    @JsonProperty("questionnaireCompleted")
    @Column(name = "questionnaire_completed", nullable = false)
    private Boolean questionnaireCompleted = false;



    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User() {
        // Constructeur sans argument (obligatoire pour JPA)
    }
    public User(Long id) {
        this.id = id;
    }
    public User(String firstName, String lastName, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public Long getId() {
        return id;

    }

    public Boolean getQuestionnaireCompleted() {
        return questionnaireCompleted;
    }

    public void setQuestionnaireCompleted(Boolean questionnaireCompleted) {
        this.questionnaireCompleted = questionnaireCompleted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(@NotNull(message = "First name is required") @Size(min = 2, message = "First name must have at least 2 characters") String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NotNull(message = "Last name is required") @Size(min = 2, message = "Last name must have at least 2 characters") String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(@NotNull(message = "Phone number is required") @Pattern(
            regexp = "^(05|06|07)[0-9]{8}$",
            message = "Phone number must start with 05, 06, or 07 and have exactly 10 digits"
    ) String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @Past(message = "Birth date must be in the past") LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@Past(message = "Birth date must be in the past") LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setEmail(@Email(message = "Invalid email format") @NotNull(message = "Email is required") String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^(Male|Female)$", message = "Gender must be Male or Female") String getGender() {
        return gender;
    }

    public void setGender(@Pattern(regexp = "^(Male|Female)$", message = "Gender must be Male or Female") String gender) {
        this.gender = gender;
    }

    public @NotNull(message = "Education level is required") @Size(min = 2, max = 50, message = "Education level must be between 2 and 50 characters") String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(@NotNull(message = "Education level is required") @Size(min = 2, max = 50, message = "Education level must be between 2 and 50 characters") String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public @NotNull(message = "University is required") @Size(min = 2, max = 100, message = "University name must be between 2 and 100 characters") String getUniversity() {
        return university;
    }

    public void setUniversity(@NotNull(message = "University is required") @Size(min = 2, max = 100, message = "University name must be between 2 and 100 characters") String university) {
        this.university = university;
    }
}
