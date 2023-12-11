package com.smqa.smqa.statementBased;

import com.smqa.smqa.JwtTokenService.JwtTokenProvider;
import com.smqa.smqa.appointments.Appointment;
import com.smqa.smqa.appointments.AppointmentRepository;
import com.smqa.smqa.appointments.AppointmentService;
import com.smqa.smqa.appointments.AppointmentStatus;
import com.smqa.smqa.datatypes.Role;
import com.smqa.smqa.healthRecords.HealthRecord;
import com.smqa.smqa.healthRecords.HealthRecordRepository;
import com.smqa.smqa.healthRecords.HealthRecordService;
import com.smqa.smqa.requests.HealthRecordUpdateRequest;
import com.smqa.smqa.user.User;
import com.smqa.smqa.user.UserRepository;
import com.smqa.smqa.user.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringBootTest
public class StatementBasedTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private HealthRecordRepository healthRecordRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    @InjectMocks
    private HealthRecordService healthRecordService;

    @InjectMocks
    private UserService userService;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void updateAppointmentStatus() {
        Appointment existingAppointment = new Appointment("user@example1.com", LocalDateTime.now(), AppointmentStatus.PROCESSING,LocalDateTime.now());
        existingAppointment.setStatus(AppointmentStatus.PROCESSING);
        when(appointmentRepository.findById("appointmentId")).thenReturn(java.util.Optional.of(existingAppointment));
        appointmentService.updateAppointmentStatus("appointmentId", AppointmentStatus.APPROVED);
        verify(appointmentRepository).save(any(Appointment.class));
        assertEquals(AppointmentStatus.APPROVED, existingAppointment.getStatus());
        assertNotNull(existingAppointment.getTimestamp());
    }

    @Test
    void updateUserDetails_UserExists() {
        // Mock user data
        String email = "test@example.com";
        String originalAddress = "123 Main St";
        String originalPhone = "555-1234";
        LocalDate originalDob = LocalDate.of(1990, 1, 1);
        User user = new User(email, "John Doe", originalAddress, originalPhone, "hashedPassword", originalDob, Role.USER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        String successMessage = userService.updateUserDetails(email, "456 New St", "555-5678", LocalDate.of(1995, 5, 5));
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(user);
        assertEquals("User updated successfully", successMessage);
        assertEquals("456 New St", user.getAddress());
        assertEquals("555-5678", user.getPhone());
        assertEquals(LocalDate.of(1995, 5, 5), user.getDob());
    }

    @Test
    void updateHealthRecordById() {
        String recordId = "123";
        HealthRecordUpdateRequest updateRequest = new HealthRecordUpdateRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");

        HealthRecord existingRecord = new HealthRecord("test", "","100","100","test", "test", "test", "test", "user@gmail.com", "testDoc", "", LocalDateTime.now());
        when(healthRecordRepository.findById(recordId)).thenReturn(Optional.of(existingRecord));
        when(healthRecordRepository.save(existingRecord)).thenReturn(existingRecord);  // Mock the save operation to return the existing record
        HealthRecord updatedRecord = healthRecordService.updateHealthRecordById(recordId, updateRequest);
        assertEquals(updateRequest.getTitle(), updatedRecord.getTitle());
        assertEquals(updateRequest.getDescription(), updatedRecord.getDescription());

    }

}
