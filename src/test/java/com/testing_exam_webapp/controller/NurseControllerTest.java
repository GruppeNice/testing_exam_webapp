package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.NurseRequest;
import com.testing_exam_webapp.model.mysql.Nurse;
import com.testing_exam_webapp.model.types.NurseSpecialityType;
import com.testing_exam_webapp.service.NurseService;
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
 * Test suite for NurseController.
 * Tests HTTP endpoints, status codes, and response bodies for nurse operations.
 */
@DisplayName("NurseController Tests")
class NurseControllerTest {

    private NurseService nurseService;
    private NurseController nurseController;
    private Nurse testNurse;
    private NurseRequest nurseRequest;

    @BeforeEach
    void setUp() {
        nurseService = mock(NurseService.class);
        nurseController = new NurseController(nurseService);
        
        testNurse = new Nurse();
        testNurse.setNurseId(UUID.randomUUID());
        testNurse.setNurseName("Test Nurse");
        testNurse.setSpeciality(NurseSpecialityType.GENERAL_CARE);
        
        nurseRequest = new NurseRequest();
        nurseRequest.setNurseName("New Nurse");
        nurseRequest.setSpeciality(NurseSpecialityType.ICU);
    }

    @Test
    @DisplayName("getNurses - With nurses - Returns OK")
    void getNurses_WithNurses_ReturnsOk() {
        List<Nurse> nurses = Arrays.asList(testNurse);
        when(nurseService.getNurses()).thenReturn(nurses);

        ResponseEntity<List<Nurse>> response = nurseController.getNurses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(nurseService, times(1)).getNurses();
    }

    @Test
    @DisplayName("getNurses - Empty list - Returns NO_CONTENT")
    void getNurses_EmptyList_ReturnsNoContent() {
        when(nurseService.getNurses()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Nurse>> response = nurseController.getNurses();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(nurseService, times(1)).getNurses();
    }

    @Test
    @DisplayName("getNurseById - Valid ID - Returns OK")
    void getNurseById_ValidId_ReturnsOk() {
        UUID nurseId = testNurse.getNurseId();
        when(nurseService.getNurseById(nurseId)).thenReturn(testNurse);

        ResponseEntity<Nurse> response = nurseController.getNurseById(nurseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(nurseId, response.getBody().getNurseId());
        verify(nurseService, times(1)).getNurseById(nurseId);
    }

    @Test
    @DisplayName("createNurse - Valid request - Returns CREATED")
    void createNurse_ValidRequest_ReturnsCreated() {
        when(nurseService.createNurse(any(NurseRequest.class))).thenReturn(testNurse);

        ResponseEntity<Nurse> response = nurseController.createNurse(nurseRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nurseService, times(1)).createNurse(nurseRequest);
    }

    @Test
    @DisplayName("updateNurse - Valid request - Returns OK")
    void updateNurse_ValidRequest_ReturnsOk() {
        UUID nurseId = testNurse.getNurseId();
        when(nurseService.updateNurse(eq(nurseId), any(NurseRequest.class))).thenReturn(testNurse);

        ResponseEntity<Nurse> response = nurseController.updateNurse(nurseId, nurseRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nurseService, times(1)).updateNurse(nurseId, nurseRequest);
    }

    @Test
    @DisplayName("deleteNurse - Valid ID - Returns NO_CONTENT")
    void deleteNurse_ValidId_ReturnsNoContent() {
        UUID nurseId = testNurse.getNurseId();
        doNothing().when(nurseService).deleteNurse(nurseId);

        ResponseEntity<Void> response = nurseController.deleteNurse(nurseId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(nurseService, times(1)).deleteNurse(nurseId);
    }
}

