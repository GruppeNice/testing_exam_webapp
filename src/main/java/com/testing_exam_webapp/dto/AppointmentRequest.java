package com.testing_exam_webapp.dto;

import com.testing_exam_webapp.model.types.AppointmentStatusType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AppointmentRequest {
    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;
    
    private String reason;
    
    @NotNull(message = "Status is required")
    private AppointmentStatusType status;
    
    private UUID patientId;
    
    private UUID doctorId;
    
    private UUID nurseId;
}

