package com.smqa.smqa.appointments;

import com.smqa.smqa.datatypes.Role;
import com.smqa.smqa.user.User;
import com.smqa.smqa.user.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AppointmentService appointmentService;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void getAllAppointments() {
        Appointment appointment1 = new Appointment("user@example1.com", LocalDateTime.now(),AppointmentStatus.PROCESSING,LocalDateTime.now());
        Appointment appointment2 = new Appointment("user@example1.com", LocalDateTime.now(),AppointmentStatus.PROCESSING,LocalDateTime.now());

        List<Appointment> expectedAppointments = Arrays.asList(appointment1, appointment2);
        when(appointmentRepository.findAll()).thenReturn(expectedAppointments);
        List<Appointment> actualAppointments = appointmentService.getAllAppointments();
        assertEquals(expectedAppointments.size(), actualAppointments.size());

    }



    @Test
    public void updateAppointmentStatus() {
        Appointment existingAppointment = new Appointment("user@example1.com", LocalDateTime.now(),AppointmentStatus.PROCESSING,LocalDateTime.now());
        existingAppointment.setStatus(AppointmentStatus.PROCESSING);
        when(appointmentRepository.findById("appointmentId")).thenReturn(java.util.Optional.of(existingAppointment));
        appointmentService.updateAppointmentStatus("appointmentId", AppointmentStatus.APPROVED);
        verify(appointmentRepository).save(any(Appointment.class));
        assertEquals(AppointmentStatus.APPROVED, existingAppointment.getStatus());
        assertNotNull(existingAppointment.getTimestamp());
    }

    @Test
    public void getAllAppointmentsByUserEmail() {

        String userEmail = "user@example.com";
        Appointment appointment1 = new Appointment(userEmail, LocalDateTime.now(), AppointmentStatus.PROCESSING, LocalDateTime.now());
        Appointment appointment2 = new Appointment(userEmail, LocalDateTime.now(), AppointmentStatus.PROCESSING, LocalDateTime.now());
        when(appointmentRepository.findByUserEmail(userEmail)).thenReturn(Arrays.asList(appointment1, appointment2));
        List<Appointment> actualAppointments = appointmentService.getAllAppointmentsByUserEmail(userEmail);
        verify(appointmentRepository).findByUserEmail(userEmail);
        List<Appointment> expectedAppointments = Arrays.asList(appointment1, appointment2);
        assertEquals(expectedAppointments.size(), actualAppointments.size());
        assertEquals(expectedAppointments, actualAppointments);
    }


    @Test
    public void createAppointment() {
        // Mock user email
        String userEmail = "user@example.com";
        User mockUser = new User("user@example.com", "John Doe", "Address", "1234567890", "password", LocalDate.now(), Role.USER);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        LocalDateTime date = LocalDateTime.now();
        Appointment createdAppointment = appointmentService.createAppointment(userEmail, date);
        verify(userRepository).findByEmail(userEmail);
        verify(appointmentRepository).save(any(Appointment.class));


    }
    @Test
    void createEmergencyAppointment_ValidUserEmailAndDate_ReturnsSavedAppointment() {
        // Arrange
        String userEmail = "user@example.com";
        User mockUser = new User("user@example.com", "John Doe", "Address", "1234567890", "password", LocalDate.now(), Role.USER);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        LocalDateTime date = LocalDateTime.now();
        Appointment createdAppointment = appointmentService.createEmergencyAppointment(userEmail, date);
        verify(userRepository).findByEmail(userEmail);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void updateAppointmentDate_ValidAppointmentId_UpdateDateAndTimestamp() {
        // Arrange
        String appointmentId = "123";
        LocalDateTime newDate = LocalDateTime.now();
        AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
        UserRepository userRepo = mock(UserRepository.class);
        Appointment existingAppointment = new Appointment();
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(existingAppointment);
        AppointmentService newService = new AppointmentService(appointmentRepository,userRepo);
        newService.updateAppointmentDate(appointmentId, newDate);
        verify(appointmentRepository).findById(appointmentId);
        verify(appointmentRepository).save(existingAppointment);
        assertEquals(newDate, existingAppointment.getDate());
        assertNotNull(existingAppointment.getTimestamp());

    }



}