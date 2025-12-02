package com.example.hospital_db_backend.controller;

import com.example.hospital_db_backend.dto.HospitalRequest;
import com.example.hospital_db_backend.model.mysql.Hospital;
import com.example.hospital_db_backend.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Hospital>> getHospitals() {
        List<Hospital> hospitals = hospitalService.getHospitals();
        if(hospitals.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(hospitals, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Hospital> getHospitalById(@PathVariable UUID id) {
        Hospital hospital = hospitalService.getHospitalById(id);
        return new ResponseEntity<>(hospital, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hospital> createHospital(@Valid @RequestBody HospitalRequest request) {
        Hospital hospital = hospitalService.createHospital(request);
        return new ResponseEntity<>(hospital, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hospital> updateHospital(@PathVariable UUID id, @Valid @RequestBody HospitalRequest request) {
        Hospital hospital = hospitalService.updateHospital(id, request);
        return new ResponseEntity<>(hospital, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHospital(@PathVariable UUID id) {
        hospitalService.deleteHospital(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

