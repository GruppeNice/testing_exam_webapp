package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.WardRequest;
import com.testing_exam_webapp.model.mysql.Ward;
import com.testing_exam_webapp.model.types.WardType;
import com.testing_exam_webapp.service.WardService;
import com.testing_exam_webapp.util.TestDataBuilder;
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
 * Test suite for WardController.
 * Tests HTTP endpoints, status codes, and response bodies.
 */
@DisplayName("WardController Tests")
class WardControllerTest {

    private WardService wardService;
    private WardController wardController;
    private Ward testWard;

    @BeforeEach
    void setUp() {
        wardService = mock(WardService.class);
        wardController = new WardController(wardService);
        testWard = TestDataBuilder.createWard();
    }

    @Test
    @DisplayName("getWards - Should return OK with wards list")
    void getWards_WithWards_ReturnsOk() {
        // Arrange
        List<Ward> wards = Arrays.asList(testWard);
        when(wardService.getWards()).thenReturn(wards);

        // Act
        ResponseEntity<List<Ward>> response = wardController.getWards();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(wardService, times(1)).getWards();
    }

    @Test
    @DisplayName("getWards - Should return NO_CONTENT when empty")
    void getWards_EmptyList_ReturnsNoContent() {
        // Arrange
        when(wardService.getWards()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Ward>> response = wardController.getWards();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(wardService, times(1)).getWards();
    }

    @Test
    @DisplayName("getWardById - Should return OK with ward")
    void getWardById_ValidId_ReturnsOk() {
        // Arrange
        UUID wardId = testWard.getWardId();
        when(wardService.getWardById(wardId)).thenReturn(testWard);

        // Act
        ResponseEntity<Ward> response = wardController.getWardById(wardId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(wardId, response.getBody().getWardId());
        verify(wardService, times(1)).getWardById(wardId);
    }

    @Test
    @DisplayName("createWard - Should return CREATED with ward")
    void createWard_ValidRequest_ReturnsCreated() {
        // Arrange
        WardRequest request = new WardRequest();
        request.setType(WardType.CARDIOLOGY);
        request.setMaxCapacity(20);

        when(wardService.createWard(any(WardRequest.class))).thenReturn(testWard);

        // Act
        ResponseEntity<Ward> response = wardController.createWard(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(wardService, times(1)).createWard(any(WardRequest.class));
    }

    @Test
    @DisplayName("updateWard - Should return OK with updated ward")
    void updateWard_ValidRequest_ReturnsOk() {
        // Arrange
        UUID wardId = testWard.getWardId();
        WardRequest request = new WardRequest();
        request.setMaxCapacity(30);

        when(wardService.updateWard(eq(wardId), any(WardRequest.class))).thenReturn(testWard);

        // Act
        ResponseEntity<Ward> response = wardController.updateWard(wardId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(wardService, times(1)).updateWard(eq(wardId), any(WardRequest.class));
    }

    @Test
    @DisplayName("deleteWard - Should return NO_CONTENT")
    void deleteWard_ValidId_ReturnsNoContent() {
        // Arrange
        UUID wardId = testWard.getWardId();
        doNothing().when(wardService).deleteWard(wardId);

        // Act
        ResponseEntity<Void> response = wardController.deleteWard(wardId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(wardService, times(1)).deleteWard(wardId);
    }

    @Test
    @DisplayName("getWardsByType - Should return OK with wards")
    void getWardsByType_ValidType_ReturnsOk() {
        // Arrange
        WardType type = WardType.CARDIOLOGY;
        List<Ward> wards = Arrays.asList(testWard);
        when(wardService.getWardsByType(type)).thenReturn(wards);

        // Act
        ResponseEntity<List<Ward>> response = wardController.getWardsByType(type);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(wardService, times(1)).getWardsByType(type);
    }

    @Test
    @DisplayName("getWardsByType - Should return NO_CONTENT when empty")
    void getWardsByType_EmptyList_ReturnsNoContent() {
        // Arrange
        WardType type = WardType.NEUROLOGY;
        when(wardService.getWardsByType(type)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Ward>> response = wardController.getWardsByType(type);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(wardService, times(1)).getWardsByType(type);
    }

    @Test
    @DisplayName("getWardsByHospitalId - Should return OK with wards")
    void getWardsByHospitalId_ValidHospitalId_ReturnsOk() {
        // Arrange
        UUID hospitalId = UUID.randomUUID();
        List<Ward> wards = Arrays.asList(testWard);
        when(wardService.getWardsByHospitalId(hospitalId)).thenReturn(wards);

        // Act
        ResponseEntity<List<Ward>> response = wardController.getWardsByHospitalId(hospitalId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(wardService, times(1)).getWardsByHospitalId(hospitalId);
    }

    @Test
    @DisplayName("getWardsByHospitalId - Should return NO_CONTENT when empty")
    void getWardsByHospitalId_EmptyList_ReturnsNoContent() {
        // Arrange
        UUID hospitalId = UUID.randomUUID();
        when(wardService.getWardsByHospitalId(hospitalId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Ward>> response = wardController.getWardsByHospitalId(hospitalId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(wardService, times(1)).getWardsByHospitalId(hospitalId);
    }
}

