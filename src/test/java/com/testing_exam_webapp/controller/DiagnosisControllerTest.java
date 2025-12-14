package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.DiagnosisRequest;
import com.testing_exam_webapp.model.mysql.Diagnosis;
import com.testing_exam_webapp.service.DiagnosisService;
import com.testing_exam_webapp.util.TestDataBuilder;
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
 * Test suite for DiagnosisController.
 * Tests HTTP endpoints, status codes, and response bodies for diagnosis operations.
 */
@DisplayName("DiagnosisController Tests")
class DiagnosisControllerTest {

    private DiagnosisService diagnosisService;
    private DiagnosisController diagnosisController;
    private Diagnosis testDiagnosis;
    private DiagnosisRequest diagnosisRequest;

    @BeforeEach
    void setUp() {
        diagnosisService = mock(DiagnosisService.class);
        diagnosisController = new DiagnosisController(diagnosisService);
        
        testDiagnosis = TestDataBuilder.createDiagnosis();
        
        diagnosisRequest = new DiagnosisRequest();
        diagnosisRequest.setDiagnosisDate(LocalDate.of(2024, 6, 15));
        diagnosisRequest.setDescription("Hypertension");
    }

    @Test
    @DisplayName("getDiagnoses - With diagnoses - Returns OK")
    void getDiagnoses_WithDiagnoses_ReturnsOk() {
        List<Diagnosis> diagnoses = Arrays.asList(testDiagnosis);
        when(diagnosisService.getDiagnoses()).thenReturn(diagnoses);

        ResponseEntity<List<Diagnosis>> response = diagnosisController.getDiagnoses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(diagnosisService, times(1)).getDiagnoses();
    }

    @Test
    @DisplayName("getDiagnoses - Empty list - Returns NO_CONTENT")
    void getDiagnoses_EmptyList_ReturnsNoContent() {
        when(diagnosisService.getDiagnoses()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Diagnosis>> response = diagnosisController.getDiagnoses();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(diagnosisService, times(1)).getDiagnoses();
    }

    @Test
    @DisplayName("getDiagnosisById - Valid ID - Returns OK")
    void getDiagnosisById_ValidId_ReturnsOk() {
        UUID diagnosisId = testDiagnosis.getDiagnosisId();
        when(diagnosisService.getDiagnosisById(diagnosisId)).thenReturn(testDiagnosis);

        ResponseEntity<Diagnosis> response = diagnosisController.getDiagnosisById(diagnosisId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(diagnosisId, response.getBody().getDiagnosisId());
        verify(diagnosisService, times(1)).getDiagnosisById(diagnosisId);
    }

    @Test
    @DisplayName("createDiagnosis - Valid request - Returns CREATED")
    void createDiagnosis_ValidRequest_ReturnsCreated() {
        when(diagnosisService.createDiagnosis(any(DiagnosisRequest.class))).thenReturn(testDiagnosis);

        ResponseEntity<Diagnosis> response = diagnosisController.createDiagnosis(diagnosisRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(diagnosisService, times(1)).createDiagnosis(diagnosisRequest);
    }

    @Test
    @DisplayName("updateDiagnosis - Valid request - Returns OK")
    void updateDiagnosis_ValidRequest_ReturnsOk() {
        UUID diagnosisId = testDiagnosis.getDiagnosisId();
        when(diagnosisService.updateDiagnosis(eq(diagnosisId), any(DiagnosisRequest.class))).thenReturn(testDiagnosis);

        ResponseEntity<Diagnosis> response = diagnosisController.updateDiagnosis(diagnosisId, diagnosisRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(diagnosisService, times(1)).updateDiagnosis(diagnosisId, diagnosisRequest);
    }

    @Test
    @DisplayName("deleteDiagnosis - Valid ID - Returns NO_CONTENT")
    void deleteDiagnosis_ValidId_ReturnsNoContent() {
        UUID diagnosisId = testDiagnosis.getDiagnosisId();
        doNothing().when(diagnosisService).deleteDiagnosis(diagnosisId);

        ResponseEntity<Void> response = diagnosisController.deleteDiagnosis(diagnosisId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(diagnosisService, times(1)).deleteDiagnosis(diagnosisId);
    }

    @Test
    @DisplayName("getDiagnosisById - Service throws exception - Exception propagated")
    void getDiagnosisById_ServiceThrowsException_ExceptionPropagated() {
        UUID diagnosisId = UUID.randomUUID();
        when(diagnosisService.getDiagnosisById(diagnosisId))
                .thenThrow(new RuntimeException("Diagnosis not found"));

        assertThrows(RuntimeException.class, () -> diagnosisController.getDiagnosisById(diagnosisId));
    }
}

