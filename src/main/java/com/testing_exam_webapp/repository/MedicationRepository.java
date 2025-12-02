package com.example.hospital_db_backend.repository;

import com.example.hospital_db_backend.model.mysql.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicationRepository extends JpaRepository<Medication, UUID> {
}

