package com.example.hospital_db_backend.controller;

import com.example.hospital_db_backend.dto.WardRequest;
import com.example.hospital_db_backend.model.mysql.Ward;
import com.example.hospital_db_backend.service.WardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wards")
public class WardController {

    private final WardService wardService;

    public WardController(WardService wardService) {
        this.wardService = wardService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Ward>> getWards() {
        List<Ward> wards = wardService.getWards();
        if(wards.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(wards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Ward> getWardById(@PathVariable UUID id) {
        Ward ward = wardService.getWardById(id);
        return new ResponseEntity<>(ward, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ward> createWard(@Valid @RequestBody WardRequest request) {
        Ward ward = wardService.createWard(request);
        return new ResponseEntity<>(ward, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ward> updateWard(@PathVariable UUID id, @Valid @RequestBody WardRequest request) {
        Ward ward = wardService.updateWard(id, request);
        return new ResponseEntity<>(ward, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWard(@PathVariable UUID id) {
        wardService.deleteWard(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

