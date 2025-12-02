package com.example.hospital_db_backend.controller;

import com.example.hospital_db_backend.dto.MedicationRequest;
import com.example.hospital_db_backend.model.mysql.Medication;
import com.example.hospital_db_backend.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Medication>> getMedications() {
        List<Medication> medications = medicationService.getMedications();
        if(medications.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Medication> getMedicationById(@PathVariable UUID id) {
        Medication medication = medicationService.getMedicationById(id);
        return new ResponseEntity<>(medication, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Medication> createMedication(@Valid @RequestBody MedicationRequest request) {
        Medication medication = medicationService.createMedication(request);
        return new ResponseEntity<>(medication, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Medication> updateMedication(@PathVariable UUID id, @Valid @RequestBody MedicationRequest request) {
        Medication medication = medicationService.updateMedication(id, request);
        return new ResponseEntity<>(medication, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedication(@PathVariable UUID id) {
        medicationService.deleteMedication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

