package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.SurgeryRequest;
import com.testing_exam_webapp.model.mysql.Surgery;
import com.testing_exam_webapp.service.SurgeryService;
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
 * Test suite for SurgeryController.
 * Tests HTTP endpoints, status codes, and response bodies for surgery operations.
 */
@DisplayName("SurgeryController Tests")
class SurgeryControllerTest {

    private SurgeryService surgeryService;
    private SurgeryController surgeryController;
    private Surgery testSurgery;
    private SurgeryRequest surgeryRequest;

    @BeforeEach
    void setUp() {
        surgeryService = mock(SurgeryService.class);
        surgeryController = new SurgeryController(surgeryService);
        
        testSurgery = new Surgery();
        testSurgery.setSurgeryId(UUID.randomUUID());
        testSurgery.setSurgeryDate(LocalDate.of(2024, 6, 15));
        testSurgery.setDescription("Heart surgery");
        
        surgeryRequest = new SurgeryRequest();
        surgeryRequest.setSurgeryDate(LocalDate.of(2024, 6, 15));
        surgeryRequest.setDescription("Heart surgery");
    }

    @Test
    @DisplayName("getSurgeries - With surgeries - Returns OK")
    void getSurgeries_WithSurgeries_ReturnsOk() {
        List<Surgery> surgeries = Arrays.asList(testSurgery);
        when(surgeryService.getSurgeries()).thenReturn(surgeries);

        ResponseEntity<List<Surgery>> response = surgeryController.getSurgeries();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(surgeryService, times(1)).getSurgeries();
    }

    @Test
    @DisplayName("getSurgeries - Empty list - Returns NO_CONTENT")
    void getSurgeries_EmptyList_ReturnsNoContent() {
        when(surgeryService.getSurgeries()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Surgery>> response = surgeryController.getSurgeries();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(surgeryService, times(1)).getSurgeries();
    }

    @Test
    @DisplayName("getSurgeryById - Valid ID - Returns OK")
    void getSurgeryById_ValidId_ReturnsOk() {
        UUID surgeryId = testSurgery.getSurgeryId();
        when(surgeryService.getSurgeryById(surgeryId)).thenReturn(testSurgery);

        ResponseEntity<Surgery> response = surgeryController.getSurgeryById(surgeryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(surgeryId, response.getBody().getSurgeryId());
        verify(surgeryService, times(1)).getSurgeryById(surgeryId);
    }

    @Test
    @DisplayName("createSurgery - Valid request - Returns CREATED")
    void createSurgery_ValidRequest_ReturnsCreated() {
        when(surgeryService.createSurgery(any(SurgeryRequest.class))).thenReturn(testSurgery);

        ResponseEntity<Surgery> response = surgeryController.createSurgery(surgeryRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(surgeryService, times(1)).createSurgery(surgeryRequest);
    }

    @Test
    @DisplayName("updateSurgery - Valid request - Returns OK")
    void updateSurgery_ValidRequest_ReturnsOk() {
        UUID surgeryId = testSurgery.getSurgeryId();
        when(surgeryService.updateSurgery(eq(surgeryId), any(SurgeryRequest.class))).thenReturn(testSurgery);

        ResponseEntity<Surgery> response = surgeryController.updateSurgery(surgeryId, surgeryRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(surgeryService, times(1)).updateSurgery(surgeryId, surgeryRequest);
    }

    @Test
    @DisplayName("deleteSurgery - Valid ID - Returns NO_CONTENT")
    void deleteSurgery_ValidId_ReturnsNoContent() {
        UUID surgeryId = testSurgery.getSurgeryId();
        doNothing().when(surgeryService).deleteSurgery(surgeryId);

        ResponseEntity<Void> response = surgeryController.deleteSurgery(surgeryId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(surgeryService, times(1)).deleteSurgery(surgeryId);
    }

    @Test
    @DisplayName("getSurgeryById - Service throws exception - Exception propagated")
    void getSurgeryById_ServiceThrowsException_ExceptionPropagated() {
        UUID surgeryId = UUID.randomUUID();
        when(surgeryService.getSurgeryById(surgeryId))
                .thenThrow(new RuntimeException("Surgery not found"));

        assertThrows(RuntimeException.class, () -> surgeryController.getSurgeryById(surgeryId));
    }

    @Test
    @DisplayName("createSurgery - Service throws exception - Exception propagated")
    void createSurgery_ServiceThrowsException_ExceptionPropagated() {
        when(surgeryService.createSurgery(any(SurgeryRequest.class)))
                .thenThrow(new RuntimeException("Validation failed"));

        assertThrows(RuntimeException.class, () -> surgeryController.createSurgery(surgeryRequest));
    }
}

