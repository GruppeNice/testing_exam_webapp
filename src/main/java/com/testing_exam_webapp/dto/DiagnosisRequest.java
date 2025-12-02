package com.example.hospital_db_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DiagnosisRequest {
    @NotNull(message = "Diagnosis date is required")
    private LocalDate diagnosisDate;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private UUID doctorId;
}

