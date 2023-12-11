package com.smqa.smqa.healthRecords;

import com.smqa.smqa.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "healthRecords")
@Data

public class HealthRecord {

    @Id
    private String id;
    @NotBlank(message = "Title Required")

    private String title;
    @NotBlank(message = "BP Required")

    private String bp;
    @NotBlank(message = " Diabetic Required")

    private String diabetic;
    @NotBlank(message = "Description Required")

    private String description;
    @NotBlank(message = "Prescription Required")

    private String prescription;
    @NotBlank(message = "Allergies Required")
    private String allergies;
    private String period;
    @NotBlank(message = "Email Required")
    @Email(message = "Invalid email format")

    private String email;
    @NotBlank(message = "Doctor Name Required")
    private String doctorName;
    private String feedback;
    private String recordsUrl;
    @DBRef
    private User user;
    private LocalDateTime timestamp;


    public HealthRecord(String title, String bp, String diabetic, String description, String prescription, String allergies, String period, String email, String doctorName, String feedback, String recordsUrl,LocalDateTime timestamp) {
        this.title = title;
        this.bp = bp;
        this.diabetic = diabetic;
        this.description = description;
        this.prescription = prescription;
        this.allergies = allergies;
        this.period = period;
        this.email = email;
        this.doctorName = doctorName;
        this.feedback = feedback;
        this.recordsUrl = recordsUrl;
        this.timestamp= timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getBp() {
        return bp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getDiabetic() {
        return diabetic;
    }

    public void setDiabetic(String diabetic) {
        this.diabetic = diabetic;
    }

    public String getEmail() {
        return email;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRecordsUrl() {
        return recordsUrl;
    }

    public void setRecordsUrl(String recordsUrl) {
        this.recordsUrl = recordsUrl;
    }

    public User getUser() {
        return user;
    }
}
