package com.smqa.smqa.healthRecords;

import com.smqa.smqa.requests.HealthRecordUpdateRequest;
import com.smqa.smqa.user.User;
import com.smqa.smqa.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final UserRepository userRepository;

    @Autowired
    public HealthRecordService(HealthRecordRepository healthRecordRepository, UserRepository userRepository) {
        this.healthRecordRepository = healthRecordRepository;
        this.userRepository = userRepository;
    }

    public List<HealthRecord> getAllHealthRecords() {
        return healthRecordRepository.findAll();
    }

    public List<HealthRecord> getHealthRecordsByEmail(String userEmail) {
        try {
            if (userEmail == null) {
                throw new IllegalArgumentException("User email cannot be null");
            }

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

            return healthRecordRepository.findByUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching health records: " + e.getMessage());
        }
    }
    public HealthRecord saveHealthRecord(HealthRecord healthRecord) {
        try {
            if (healthRecord == null) {
                throw new IllegalArgumentException("Health record cannot be null");
            }

            String userEmail = healthRecord.getEmail();
            if (userEmail == null || userEmail.isEmpty()) {
                throw new IllegalArgumentException("User email in health record cannot be null or empty");
            }

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

            healthRecord.setUser(user);
            healthRecord.setTimestamp(LocalDateTime.now());
            return healthRecordRepository.save(healthRecord);
        } catch (Exception e) {
            // Handle exceptions or rethrow as needed
            throw new RuntimeException("Error saving health record: " + e.getMessage());
        }
    }

    public HealthRecord updateHealthRecordById(String id, HealthRecordUpdateRequest updateRequest) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        HealthRecord healthRecord = healthRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Health record not found for ID: " + id));

        updateIfNotNull(updateRequest.getTitle(), healthRecord::setTitle);
        updateIfNotNull(updateRequest.getDescription(), healthRecord::setDescription);
        updateIfNotNullAndNotEmpty(updateRequest.getAllergies(), healthRecord::setAllergies);
        updateIfNotNull(updateRequest.getDoctorName(), healthRecord::setDoctorName);
        updateIfNotNull(updateRequest.getProscriptions(), healthRecord::setPrescription);
        updateIfNotNull(updateRequest.getRecordsUrl(), healthRecord::setRecordsUrl);
        updateIfNotNull(updateRequest.getFeedback(), healthRecord::setFeedback);
        updateIfNotNull(updateRequest.getPeriod(), healthRecord::setPeriod);
        updateIfNotNull(updateRequest.getBp(), healthRecord::setBp);
        updateIfNotNull(updateRequest.getDiabetic(), healthRecord::setDiabetic);

        healthRecord.setTimestamp(LocalDateTime.now());
        return healthRecordRepository.save(healthRecord);
    }

    public <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

    public void updateIfNotNullAndNotEmpty(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isEmpty()) {
            setter.accept(newValue);
        }
    }


    public List<Map<String, String>> getUserHealthRecordVitals(String userEmail) {
        if (userEmail == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

        List<HealthRecord> healthRecords = healthRecordRepository.findByUser(user);

        return healthRecords.stream()
                .map(this::mapToHealthRecordDetailsDto)
                .collect(Collectors.toList());
    }

    public Map<String, String> mapToHealthRecordDetailsDto(HealthRecord healthRecord) {
        Map<String, String> response = new HashMap<>();

        if (healthRecord != null) {
            response.put("bp", getValueOrEmpty(healthRecord.getBp()));
            response.put("diabetic", getValueOrEmpty(String.valueOf(healthRecord.getDiabetic())));

            LocalDateTime timestamp = healthRecord.getTimestamp();
            response.put("timestamp", timestamp != null ? timestamp.toString() : "");
        }

        return response;
    }

    private String getValueOrEmpty(String value) {
        return value != null && !value.equals("null") ? value : "";
    }

}
