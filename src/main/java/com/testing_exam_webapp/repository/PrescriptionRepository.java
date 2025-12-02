package com.testing_exam_webapp.repository;

import com.testing_exam_webapp.model.mysql.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {
}

