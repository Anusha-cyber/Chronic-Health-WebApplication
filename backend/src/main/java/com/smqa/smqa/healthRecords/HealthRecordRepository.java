package com.smqa.smqa.healthRecords;

import com.smqa.smqa.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HealthRecordRepository extends MongoRepository<HealthRecord, String> {
    // Custom queries or methods can be added here if needed
    List<HealthRecord> findByUser(User user);
}