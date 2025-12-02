package com.testing_exam_webapp.mysql;

import com.testing_exam_webapp.model.entity_bases.DiagnosisBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "diagnosis")
public class Diagnosis extends DiagnosisBase {
    @Id
    private UUID diagnosisId;
    @ManyToOne
    private Doctor doctor;
}
