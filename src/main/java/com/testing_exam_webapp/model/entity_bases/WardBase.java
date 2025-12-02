package com.testing_exam_webapp.entity_bases;

import com.testing_exam_webapp.model.types.WardType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class WardBase {
    @Enumerated(EnumType.STRING)
    protected WardType type;
    protected int maxCapacity;
}
