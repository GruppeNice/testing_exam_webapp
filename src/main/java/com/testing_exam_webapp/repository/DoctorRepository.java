package com.testing_exam_webapp.repository;

import com.testing_exam_webapp.model.mysql.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
}

