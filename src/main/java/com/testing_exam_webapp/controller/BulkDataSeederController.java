package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.service.BulkDataSeederService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/seed")
@PreAuthorize("hasRole('ADMIN')")
public class BulkDataSeederController {

    private final BulkDataSeederService bulkDataSeederService;

    public BulkDataSeederController(BulkDataSeederService bulkDataSeederService) {
        this.bulkDataSeederService = bulkDataSeederService;
    }

    @PostMapping("/quick")
    public ResponseEntity<Map<String, Object>> seedQuickData() {
        // Seeds 50 hospitals, 50 patients, 50 doctors, 50 nurses, 100 appointments
        try {
            Map<String, Integer> results = bulkDataSeederService.seedBulkData(
                    100, 100, 100, 100, 200);
            
            return new ResponseEntity<>(new HashMap<>(results), HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage() != null ? e.getMessage() : "Unknown error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/large")
    public ResponseEntity<Map<String, Object>> seedLargeData() {
        // Seeds 500 hospitals, 500 patients, 500 doctors, 500 nurses, 1000 appointments
        try {
            Map<String, Integer> results = bulkDataSeederService.seedBulkData(
                    500, 500, 500, 500, 1000);
            
            return new ResponseEntity<>(new HashMap<>(results), HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage() != null ? e.getMessage() : "Unknown error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

