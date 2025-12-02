package com.testing_exam_webapp.model.entity_bases;

import com.testing_exam_webapp.model.types.AppointmentStatusType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
public abstract class AppointmentBase {
    protected LocalDate appointmentDate;
    protected String reason;
    @Enumerated(EnumType.STRING)
    protected AppointmentStatusType status;
}
