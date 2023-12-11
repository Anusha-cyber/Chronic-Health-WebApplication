package com.smqa.smqa.healthRecords;
import com.smqa.smqa.requests.HealthRecordUpdateRequest;
import com.smqa.smqa.user.User;
import com.smqa.smqa.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HealthRecordServiceTest {

    @Mock
    private HealthRecordRepository healthRecordRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HealthRecordService healthRecordService;

    @Test
    void getAllHealthRecords() {
        HealthRecord healthRecord1 = new HealthRecord("test", "","100","100","test", "test", "test", "test", "user@gmail.com", "testDoc", "", LocalDateTime.now());
        HealthRecord healthRecord2 = new HealthRecord("test", "","100","100","test", "test", "test", "test", "user@gmail.com", "testDoc", "", LocalDateTime.now());

        List<HealthRecord> expectedRecords = Arrays.asList(healthRecord1, healthRecord2);

        when(healthRecordRepository.findAll()).thenReturn(expectedRecords);

        List<HealthRecord> actualRecords = healthRecordService.getAllHealthRecords();

        assertEquals(expectedRecords.size(), actualRecords.size());
    }


    @Test
    void getHealthRecordsByEmail() {
        // Mock user
        String userEmail = "user@example1.com";
        User user = new User();
        user.setEmail(userEmail);

        HealthRecord healthRecord1 = new HealthRecord("test", "", "100", "100", "test", "test", "test", "test", "user@gmail.com", "testDoc", "", LocalDateTime.now());
        HealthRecord healthRecord2 = new HealthRecord("test", "", "100", "100", "test", "test", "test", "test", "user@gmail.com", "testDoc", "", LocalDateTime.now());

        List<HealthRecord> noRecords = Collections.emptyList();

        // Test Case 1: User has health records
        List<HealthRecord> expectedRecords = Arrays.asList(healthRecord1, healthRecord2);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(healthRecordRepository.findByUser(user)).thenReturn(expectedRecords);
        List<HealthRecord> actualRecords = healthRecordService.getHealthRecordsByEmail(userEmail);
        assertEquals(expectedRecords.size(), actualRecords.size());

        // Test Case 2: User has no health records
        when(healthRecordRepository.findByUser(user)).thenReturn(noRecords);
        actualRecords = healthRecordService.getHealthRecordsByEmail(userEmail);
        assertTrue(actualRecords.isEmpty());

        // Test Case 3: User not found
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> healthRecordService.getHealthRecordsByEmail(userEmail));
    }

    @Test
    void saveHealthRecord() {
        // Mock user
        String userEmail = "user@sample.com";
        User user = new User();
        user.setEmail(userEmail);
        HealthRecord healthRecord = new HealthRecord("test", "","100","100","test", "test", "test", user.getEmail(), "test", "testDoc", "", LocalDateTime.now());
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(healthRecordRepository.save(healthRecord)).thenReturn(healthRecord);
        HealthRecord savedHealthRecord = healthRecordService.saveHealthRecord(healthRecord);
        assertNotNull(savedHealthRecord);
    }

    @Test
    void saveHealthRecord_InvalidUser() {
        HealthRecord healthRecord = new HealthRecord("Test Title", "120/80", "No", "Description", "Prescription", "None", "1 month", "invalidUser@example.com", "Dr. Smith", "No feedback", "recordsUrl", LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> healthRecordService.saveHealthRecord(healthRecord));
    }

    @Test
    void updateHealthRecordById_RecordNotFound() {
        String recordId = "123";
        HealthRecordUpdateRequest updateRequest = new HealthRecordUpdateRequest();
        updateRequest.setTitle("Updated Title");
        when(healthRecordRepository.findById(recordId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> healthRecordService.updateHealthRecordById(recordId, updateRequest));
    }

    @Test
    void getUserHealthRecordVitals() {
        // Mock user
        String userEmail = "user@example.com";
        User user = new User();
        user.setEmail(userEmail);

        // Mock health records
        HealthRecord healthRecord1 = new HealthRecord("Test1", "120/80", "No", "Description1", "Prescription1",
                "Allergies1", "Period1", userEmail, "Doctor1", "Feedback1", "URL1", LocalDateTime.now());

        HealthRecord healthRecord2 = new HealthRecord("Test2", "130/90", "Yes", "Description2", "Prescription2",
                "Allergies2", "Period2", userEmail, "Doctor2", "Feedback2", "URL2", LocalDateTime.now());

        List<HealthRecord> healthRecords = Arrays.asList(healthRecord1, healthRecord2);

        // Mock repository calls
        when(userRepository.findByEmail(userEmail)).thenReturn(java.util.Optional.of(user));
        when(healthRecordRepository.findByUser(user)).thenReturn(healthRecords);

        // Call the service method
        List<Map<String, String>> result = healthRecordService.getUserHealthRecordVitals(userEmail);

        // Verify the result
        assertEquals(2, result.size());

        // Verify the content of the first map
        Map<String, String> firstRecord = result.get(0);
        assertEquals("120/80", firstRecord.get("bp"));
        assertEquals("No", firstRecord.get("diabetic"));

        // Verify the content of the second map
        Map<String, String> secondRecord = result.get(1);
        assertEquals("130/90", secondRecord.get("bp"));
        assertEquals("Yes", secondRecord.get("diabetic"));}


    @Test
    void getUserHealthRecordVitals_ValidUserEmail_ReturnsListOfRecords() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);
        HealthRecord healthRecord = new HealthRecord("test", "","100","100","test", "test", "test", "test", "user@gmail.com", "testDoc", "", LocalDateTime.now());
        healthRecord.setBp("120/80");
        healthRecord.setDiabetic("123");
        healthRecord.setTimestamp(LocalDateTime.of(2023, 12, 31, 12, 0));
        List<HealthRecord> healthRecords = Arrays.asList(healthRecord);

        when(userRepository.findByEmail(userEmail)).thenReturn(java.util.Optional.of(user));
        when(healthRecordRepository.findByUser(user)).thenReturn(healthRecords);

        // Act
        List<Map<String, String>> result = healthRecordService.getUserHealthRecordVitals(userEmail);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        Map<String, String> record = result.get(0);
        assertEquals("120/80", record.get("bp"));
        assertEquals("123", record.get("diabetic"));
        assertEquals("2023-12-31T12:00", record.get("timestamp"));  // Adjust the expected timestamp format
    }



    @Test
    void getUserHealthRecordVitals_UserNotFound_ThrowsRuntimeException() {
        // Arrange
        String userEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(java.util.Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> healthRecordService.getUserHealthRecordVitals(userEmail));
    }



}

