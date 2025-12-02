package com.testing_exam_webapp.repository;

import com.testing_exam_webapp.model.mysql.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
}

