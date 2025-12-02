package com.testing_exam_webapp.repository;

import com.testing_exam_webapp.model.mysql.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
}
