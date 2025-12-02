package com.example.hospital_db_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class SurgeryRequest {
    @NotNull(message = "Surgery date is required")
    private LocalDate surgeryDate;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private UUID patientId;
    
    private UUID doctorId;
}

