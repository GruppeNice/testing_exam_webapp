package com.testing_exam_webapp.repository;

import com.testing_exam_webapp.model.mysql.Surgery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurgeryRepository extends JpaRepository<Surgery, UUID> {
}

