package com.smqa.smqa.appointments;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByUserEmail(String userEmail);
}
