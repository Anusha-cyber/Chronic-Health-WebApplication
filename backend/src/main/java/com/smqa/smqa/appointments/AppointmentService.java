package com.smqa.smqa.appointments;


import com.smqa.smqa.user.User;
import com.smqa.smqa.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment createAppointment(String userEmail, LocalDateTime date) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setDate(date);
        appointment.setUserEmail(userEmail);
        appointment.setStatus(AppointmentStatus.PROCESSING);
        appointment.setTimestamp(LocalDateTime.now());

        return appointmentRepository.save(appointment);
    }
    public Appointment createEmergencyAppointment(String userEmail, LocalDateTime date) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setDate(date);
        appointment.setUserEmail(userEmail);
        appointment.setStatus(AppointmentStatus.EMERGENCY);
        appointment.setTimestamp(LocalDateTime.now());

        return appointmentRepository.save(appointment);
    }


    public void updateAppointmentStatus(String appointmentId, AppointmentStatus newStatus) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(newStatus);
            appointment.setTimestamp(LocalDateTime.now());
            appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Appointment not found for ID: " + appointmentId);
        }
    }

    public void updateAppointmentDate(String appointmentId, LocalDateTime date) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setDate(date);
            appointment.setTimestamp(LocalDateTime.now());
            appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Appointment not found for ID: " + appointmentId);
        }
    }


        public List<Appointment> getAllAppointmentsByUserEmail(String userEmail) {
            return appointmentRepository.findByUserEmail(userEmail);
        }


}
