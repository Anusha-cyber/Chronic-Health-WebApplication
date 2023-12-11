package com.smqa.smqa.branching;

import com.smqa.smqa.JwtTokenService.JwtTokenProvider;
import com.smqa.smqa.datatypes.Role;
import com.smqa.smqa.healthRecords.HealthRecord;
import com.smqa.smqa.healthRecords.HealthRecordRepository;
import com.smqa.smqa.healthRecords.HealthRecordService;
import com.smqa.smqa.requests.HealthRecordUpdateRequest;
import com.smqa.smqa.user.User;
import com.smqa.smqa.user.UserRepository;
import com.smqa.smqa.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class BranchingTest {
    @Mock
    private HealthRecordRepository healthRecordRepository;
    @InjectMocks
    private HealthRecordService healthRecordService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;
    @Test
    void updateIfNotNullAndNotEmpty_NotEmptyValue_CallsSetter() {
        // Arrange
        String newValue = "new value";
        Consumer<String> setterMock = mock(Consumer.class);

        // Act
        healthRecordService.updateIfNotNullAndNotEmpty(newValue, setterMock);
        verify(setterMock, times(1)).accept(newValue);
    }
    @Test
    void updateIfNotNull_NegatedCondition() {
        HealthRecordUpdateRequest updateRequest = new HealthRecordUpdateRequest();
        updateRequest.setTitle("New Title");
        HealthRecord healthRecord = new HealthRecord("test", "","100","100","test", "test", "test", "test", "user@gmail.com", "testDoc", "", LocalDateTime.now());
        healthRecord.setTitle("Old Title");

        healthRecordService.updateIfNotNull(null, healthRecord::setTitle);

        assertEquals("Old Title", healthRecord.getTitle());
    }
    @Test
    void getUserHealthRecordVitals_NullUserEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> healthRecordService.getUserHealthRecordVitals(null));
    }

    @Test
    void loginUser_NullOrEmptyPassword() {
        String email = "test@example.com";
        String nullPassword = null;
        String emptyPassword = "";
        User user = new User(email, "John Doe", "123 Main St", "555-1234", passwordEncoder.encode("correctPassword"), LocalDate.of(1990, 1, 1), Role.USER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> userService.loginUser(email, nullPassword));
        assertThrows(RuntimeException.class, () -> userService.loginUser(email, emptyPassword));
        verify(userRepository, never()).findByEmail(any());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtTokenProvider, never()).generateToken(any(), any());
    }

    @Test
    void registerUser_WeakPassword() {
        assertThrows(RuntimeException.class, () -> userService.registerUser("test@example.com", "John Doe", "123 Main St", "1234567890", "weak", LocalDate.of(1990, 1, 1), Role.USER));
    }
    @Test
    void registerUser_NullParameters() {
        assertThrows(RuntimeException.class, () -> userService.registerUser(null, "John Doe", "123 Main St", "1234567890", "password123", LocalDate.of(1990, 1, 1), Role.USER));
    }

    @Test
    void mapToHealthRecordDetailsDto_ValidHealthRecord_ReturnsMapWithValues() {
        // Arrange
        HealthRecord healthRecord = new HealthRecord("test", "","100","100","test", "test", "test", "test", "user@gmail.com", "testDoc", "", LocalDateTime.now());
        healthRecord.setBp("120/80");
        healthRecord.setDiabetic("123");
        healthRecord.setTimestamp(LocalDateTime.of(2023, 12, 31, 12, 0));

        // Act
        Map<String, String> result = healthRecordService.mapToHealthRecordDetailsDto(healthRecord);

        // Assert
        assertNotNull(result);
        assertEquals("120/80", result.get("bp"));
        assertEquals("123", result.get("diabetic"));
        assertEquals("2023-12-31T12:00", result.get("timestamp"));  // Adjust the expected timestamp format
    }

    @Test
    void mapToHealthRecordDetailsDto_NullHealthRecord_ReturnsEmptyMap() {
        // Act
        Map<String, String> result = healthRecordService.mapToHealthRecordDetailsDto(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
