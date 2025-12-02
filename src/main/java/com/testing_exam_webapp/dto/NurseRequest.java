package com.example.hospital_db_backend.dto;

import com.example.hospital_db_backend.model.types.NurseSpecialityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NurseRequest {
    @NotBlank(message = "Nurse name is required")
    private String nurseName;
    
    @NotNull(message = "Speciality is required")
    private NurseSpecialityType speciality;
    
    private UUID wardId;
}

