package com.testing_exam_webapp.repository;

import com.testing_exam_webapp.model.mysql.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicationRepository extends JpaRepository<Medication, UUID> {
}

