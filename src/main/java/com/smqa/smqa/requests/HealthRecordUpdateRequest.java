package com.smqa.smqa.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HealthRecordUpdateRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("prescription")
    private String prescription;

    @JsonProperty("allergies")
    private String allergies;

    @JsonProperty("period")
    private String period;

    @JsonProperty("doctorName")
    private String doctorName;

    @JsonProperty("feedback")
    private String feedback;

    @JsonProperty("recordsUrl")
    private String recordsUrl;

    // Getters and setters

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

    public String getProscriptions() {
        return prescription;
    }

    public void setProscriptions(String proscriptions) {
        this.prescription = proscriptions;
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
}
