package com.example.hospital_db_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class PrescriptionRequest {
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private UUID patientId;
    
    private UUID doctorId;
    
    private UUID medicationId;
}

