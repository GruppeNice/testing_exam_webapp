package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.PrescriptionRequest;
import com.testing_exam_webapp.model.mysql.Prescription;
import com.testing_exam_webapp.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test suite for PrescriptionController.
 * Tests HTTP endpoints, status codes, and response bodies for prescription operations.
 */
@DisplayName("PrescriptionController Tests")
class PrescriptionControllerTest {

    private PrescriptionService prescriptionService;
    private PrescriptionController prescriptionController;
    private Prescription testPrescription;
    private PrescriptionRequest prescriptionRequest;

    @BeforeEach
    void setUp() {
        prescriptionService = mock(PrescriptionService.class);
        prescriptionController = new PrescriptionController(prescriptionService);
        
        testPrescription = new Prescription();
        testPrescription.setPrescriptionId(UUID.randomUUID());
        testPrescription.setStartDate(LocalDate.of(2024, 6, 1));
        testPrescription.setEndDate(LocalDate.of(2024, 6, 30));
        
        prescriptionRequest = new PrescriptionRequest();
        prescriptionRequest.setStartDate(LocalDate.of(2024, 6, 1));
        prescriptionRequest.setEndDate(LocalDate.of(2024, 6, 30));
    }

    @Test
    @DisplayName("getPrescriptions - With prescriptions - Returns OK")
    void getPrescriptions_WithPrescriptions_ReturnsOk() {
        List<Prescription> prescriptions = Arrays.asList(testPrescription);
        when(prescriptionService.getPrescriptions()).thenReturn(prescriptions);

        ResponseEntity<List<Prescription>> response = prescriptionController.getPrescriptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(prescriptionService, times(1)).getPrescriptions();
    }

    @Test
    @DisplayName("getPrescriptions - Empty list - Returns NO_CONTENT")
    void getPrescriptions_EmptyList_ReturnsNoContent() {
        when(prescriptionService.getPrescriptions()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Prescription>> response = prescriptionController.getPrescriptions();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(prescriptionService, times(1)).getPrescriptions();
    }

    @Test
    @DisplayName("getPrescriptionById - Valid ID - Returns OK")
    void getPrescriptionById_ValidId_ReturnsOk() {
        UUID prescriptionId = testPrescription.getPrescriptionId();
        when(prescriptionService.getPrescriptionById(prescriptionId)).thenReturn(testPrescription);

        ResponseEntity<Prescription> response = prescriptionController.getPrescriptionById(prescriptionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(prescriptionId, response.getBody().getPrescriptionId());
        verify(prescriptionService, times(1)).getPrescriptionById(prescriptionId);
    }

    @Test
    @DisplayName("createPrescription - Valid request - Returns CREATED")
    void createPrescription_ValidRequest_ReturnsCreated() {
        when(prescriptionService.createPrescription(any(PrescriptionRequest.class))).thenReturn(testPrescription);

        ResponseEntity<Prescription> response = prescriptionController.createPrescription(prescriptionRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(prescriptionService, times(1)).createPrescription(prescriptionRequest);
    }

    @Test
    @DisplayName("updatePrescription - Valid request - Returns OK")
    void updatePrescription_ValidRequest_ReturnsOk() {
        UUID prescriptionId = testPrescription.getPrescriptionId();
        when(prescriptionService.updatePrescription(eq(prescriptionId), any(PrescriptionRequest.class))).thenReturn(testPrescription);

        ResponseEntity<Prescription> response = prescriptionController.updatePrescription(prescriptionId, prescriptionRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(prescriptionService, times(1)).updatePrescription(prescriptionId, prescriptionRequest);
    }

    @Test
    @DisplayName("deletePrescription - Valid ID - Returns NO_CONTENT")
    void deletePrescription_ValidId_ReturnsNoContent() {
        UUID prescriptionId = testPrescription.getPrescriptionId();
        doNothing().when(prescriptionService).deletePrescription(prescriptionId);

        ResponseEntity<Void> response = prescriptionController.deletePrescription(prescriptionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(prescriptionService, times(1)).deletePrescription(prescriptionId);
    }
}

