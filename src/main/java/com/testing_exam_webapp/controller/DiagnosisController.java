package com.example.hospital_db_backend.controller;

import com.example.hospital_db_backend.dto.DiagnosisRequest;
import com.example.hospital_db_backend.model.mysql.Diagnosis;
import com.example.hospital_db_backend.service.DiagnosisService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Diagnosis>> getDiagnoses() {
        List<Diagnosis> diagnoses = diagnosisService.getDiagnoses();
        if(diagnoses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(diagnoses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Diagnosis> getDiagnosisById(@PathVariable UUID id) {
        Diagnosis diagnosis = diagnosisService.getDiagnosisById(id);
        return new ResponseEntity<>(diagnosis, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Diagnosis> createDiagnosis(@Valid @RequestBody DiagnosisRequest request) {
        Diagnosis diagnosis = diagnosisService.createDiagnosis(request);
        return new ResponseEntity<>(diagnosis, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Diagnosis> updateDiagnosis(@PathVariable UUID id, @Valid @RequestBody DiagnosisRequest request) {
        Diagnosis diagnosis = diagnosisService.updateDiagnosis(id, request);
        return new ResponseEntity<>(diagnosis, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDiagnosis(@PathVariable UUID id) {
        diagnosisService.deleteDiagnosis(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

