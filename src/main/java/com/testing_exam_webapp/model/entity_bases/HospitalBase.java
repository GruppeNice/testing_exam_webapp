package com.testing_exam_webapp.entity_bases;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class HospitalBase {
    protected String hospitalName;
    protected String address;
    protected String city;
}
