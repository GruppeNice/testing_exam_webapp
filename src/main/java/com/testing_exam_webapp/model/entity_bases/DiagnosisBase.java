package com.testing_exam_webapp.entity_bases;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
public abstract class DiagnosisBase {
    protected LocalDate diagnosisDate;
    protected String description;
}
