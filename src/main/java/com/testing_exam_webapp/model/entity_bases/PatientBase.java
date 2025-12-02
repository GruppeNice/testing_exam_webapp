package com.testing_exam_webapp.model.entity_bases;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
public abstract class PatientBase {
    protected String patientName;
    protected LocalDate dateOfBirth;
    protected String gender;
}
