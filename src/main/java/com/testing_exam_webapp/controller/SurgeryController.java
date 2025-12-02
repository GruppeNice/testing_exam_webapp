package com.example.hospital_db_backend.controller;

import com.example.hospital_db_backend.dto.SurgeryRequest;
import com.example.hospital_db_backend.model.mysql.Surgery;
import com.example.hospital_db_backend.service.SurgeryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surgeries")
public class SurgeryController {

    private final SurgeryService surgeryService;

    public SurgeryController(SurgeryService surgeryService) {
        this.surgeryService = surgeryService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Surgery>> getSurgeries() {
        List<Surgery> surgeries = surgeryService.getSurgeries();
        if(surgeries.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(surgeries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Surgery> getSurgeryById(@PathVariable UUID id) {
        Surgery surgery = surgeryService.getSurgeryById(id);
        return new ResponseEntity<>(surgery, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Surgery> createSurgery(@Valid @RequestBody SurgeryRequest request) {
        Surgery surgery = surgeryService.createSurgery(request);
        return new ResponseEntity<>(surgery, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Surgery> updateSurgery(@PathVariable UUID id, @Valid @RequestBody SurgeryRequest request) {
        Surgery surgery = surgeryService.updateSurgery(id, request);
        return new ResponseEntity<>(surgery, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSurgery(@PathVariable UUID id) {
        surgeryService.deleteSurgery(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

