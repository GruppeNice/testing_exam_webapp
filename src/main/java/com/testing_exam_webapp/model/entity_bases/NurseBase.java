package com.testing_exam_webapp.model.entity_bases;

import com.testing_exam_webapp.model.types.NurseSpecialityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class NurseBase {
    protected String nurseName;
    @Enumerated(EnumType.STRING)
    protected NurseSpecialityType speciality;
}
