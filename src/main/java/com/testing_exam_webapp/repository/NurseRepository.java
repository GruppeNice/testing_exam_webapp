package com.testing_exam_webapp.repository;

import com.testing_exam_webapp.model.mysql.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NurseRepository extends JpaRepository<Nurse, UUID> {
}

