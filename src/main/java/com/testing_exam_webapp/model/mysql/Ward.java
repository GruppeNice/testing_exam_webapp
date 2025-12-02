package com.testing_exam_webapp.model.mysql;

import com.testing_exam_webapp.model.entity_bases.WardBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "wards")
public class Ward extends WardBase {
    @Id
    private UUID wardId;
}
