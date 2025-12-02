package com.testing_exam_webapp.model.entity_bases;

import com.testing_exam_webapp.model.types.DoctorSpecialityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class DoctorBase {
    protected String doctorName;
    @Enumerated(EnumType.STRING)
    protected DoctorSpecialityType speciality;
}
