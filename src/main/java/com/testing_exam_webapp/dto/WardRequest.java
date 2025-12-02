package com.example.hospital_db_backend.dto;

import com.example.hospital_db_backend.model.types.WardType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WardRequest {
    @NotNull(message = "Ward type is required")
    private WardType type;
    
    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Max capacity must be at least 1")
    private int maxCapacity;
}

