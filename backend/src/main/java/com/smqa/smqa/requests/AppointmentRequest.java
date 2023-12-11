package com.smqa.smqa.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentRequest {



    @NotNull(message = "Date is required")
    @JsonProperty("date")
    private LocalDateTime date;


    public LocalDateTime getDate() {
        return date;
    }
}
