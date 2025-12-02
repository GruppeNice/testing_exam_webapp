package com.testing_exam_webapp.model.mysql;

import com.testing_exam_webapp.model.entity_bases.PatientBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient extends PatientBase {
    @Id
    private UUID patientId;
    @ManyToOne
    private Ward ward;
    @ManyToOne
    private Hospital hospital;
    @ManyToMany
    private Set<Diagnosis> diagnosis;

}
