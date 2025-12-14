package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.MedicationRequest;
import com.testing_exam_webapp.model.mysql.Medication;
import com.testing_exam_webapp.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test suite for MedicationController.
 * Tests HTTP endpoints, status codes, and response bodies for medication operations.
 */
@DisplayName("MedicationController Tests")
class MedicationControllerTest {

    private MedicationService medicationService;
    private MedicationController medicationController;
    private Medication testMedication;
    private MedicationRequest medicationRequest;

    @BeforeEach
    void setUp() {
        medicationService = mock(MedicationService.class);
        medicationController = new MedicationController(medicationService);
        
        testMedication = new Medication();
        testMedication.setMedicationId(UUID.randomUUID());
        testMedication.setMedicationName("Aspirin");
        testMedication.setDosage("100mg");
        
        medicationRequest = new MedicationRequest();
        medicationRequest.setMedicationName("Aspirin");
        medicationRequest.setDosage("100mg");
    }

    @Test
    @DisplayName("getMedications - With medications - Returns OK")
    void getMedications_WithMedications_ReturnsOk() {
        List<Medication> medications = Arrays.asList(testMedication);
        when(medicationService.getMedications()).thenReturn(medications);

        ResponseEntity<List<Medication>> response = medicationController.getMedications();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(medicationService, times(1)).getMedications();
    }

    @Test
    @DisplayName("getMedications - Empty list - Returns NO_CONTENT")
    void getMedications_EmptyList_ReturnsNoContent() {
        when(medicationService.getMedications()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Medication>> response = medicationController.getMedications();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(medicationService, times(1)).getMedications();
    }

    @Test
    @DisplayName("getMedicationById - Valid ID - Returns OK")
    void getMedicationById_ValidId_ReturnsOk() {
        UUID medicationId = testMedication.getMedicationId();
        when(medicationService.getMedicationById(medicationId)).thenReturn(testMedication);

        ResponseEntity<Medication> response = medicationController.getMedicationById(medicationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(medicationId, response.getBody().getMedicationId());
        verify(medicationService, times(1)).getMedicationById(medicationId);
    }

    @Test
    @DisplayName("createMedication - Valid request - Returns CREATED")
    void createMedication_ValidRequest_ReturnsCreated() {
        when(medicationService.createMedication(any(MedicationRequest.class))).thenReturn(testMedication);

        ResponseEntity<Medication> response = medicationController.createMedication(medicationRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(medicationService, times(1)).createMedication(medicationRequest);
    }

    @Test
    @DisplayName("updateMedication - Valid request - Returns OK")
    void updateMedication_ValidRequest_ReturnsOk() {
        UUID medicationId = testMedication.getMedicationId();
        when(medicationService.updateMedication(eq(medicationId), any(MedicationRequest.class))).thenReturn(testMedication);

        ResponseEntity<Medication> response = medicationController.updateMedication(medicationId, medicationRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(medicationService, times(1)).updateMedication(medicationId, medicationRequest);
    }

    @Test
    @DisplayName("deleteMedication - Valid ID - Returns NO_CONTENT")
    void deleteMedication_ValidId_ReturnsNoContent() {
        UUID medicationId = testMedication.getMedicationId();
        doNothing().when(medicationService).deleteMedication(medicationId);

        ResponseEntity<Void> response = medicationController.deleteMedication(medicationId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(medicationService, times(1)).deleteMedication(medicationId);
    }
}

