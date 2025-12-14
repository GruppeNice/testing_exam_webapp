package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.service.BulkDataSeederService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for BulkDataSeederController.
 * Tests HTTP endpoints, status codes, and response bodies for bulk data seeding operations.
 */
@DisplayName("BulkDataSeederController Tests")
class BulkDataSeederControllerTest {

    private BulkDataSeederService bulkDataSeederService;
    private BulkDataSeederController bulkDataSeederController;

    @BeforeEach
    void setUp() {
        bulkDataSeederService = mock(BulkDataSeederService.class);
        bulkDataSeederController = new BulkDataSeederController(bulkDataSeederService);
    }

    @Test
    @DisplayName("seedQuickData - Success - Returns CREATED with results")
    void seedQuickData_Success_ReturnsCreated() {
        // Arrange
        Map<String, Integer> expectedResults = new HashMap<>();
        expectedResults.put("hospitals", 100);
        expectedResults.put("patients", 100);
        expectedResults.put("doctors", 100);
        expectedResults.put("nurses", 100);
        expectedResults.put("appointments", 200);
        expectedResults.put("wards", 300);
        expectedResults.put("medications", 15);
        expectedResults.put("diagnoses", 50);
        expectedResults.put("prescriptions", 33);
        expectedResults.put("surgeries", 10);

        when(bulkDataSeederService.seedBulkData(100, 100, 100, 100, 200))
                .thenReturn(expectedResults);

        // Act
        ResponseEntity<Map<String, Object>> response = bulkDataSeederController.seedQuickData();

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100, response.getBody().get("hospitals"));
        assertEquals(100, response.getBody().get("patients"));
        assertEquals(100, response.getBody().get("doctors"));
        assertEquals(100, response.getBody().get("nurses"));
        assertEquals(200, response.getBody().get("appointments"));
        verify(bulkDataSeederService, times(1)).seedBulkData(100, 100, 100, 100, 200);
    }

    @Test
    @DisplayName("seedQuickData - Exception - Returns INTERNAL_SERVER_ERROR with error message")
    void seedQuickData_Exception_ReturnsInternalServerError() {
        // Arrange
        when(bulkDataSeederService.seedBulkData(anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act
        ResponseEntity<Map<String, Object>> response = bulkDataSeederController.seedQuickData();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Database connection failed", response.getBody().get("error"));
        verify(bulkDataSeederService, times(1)).seedBulkData(100, 100, 100, 100, 200);
    }

    @Test
    @DisplayName("seedLargeData - Success - Returns CREATED with results")
    void seedLargeData_Success_ReturnsCreated() {
        // Arrange
        Map<String, Integer> expectedResults = new HashMap<>();
        expectedResults.put("hospitals", 500);
        expectedResults.put("patients", 500);
        expectedResults.put("doctors", 500);
        expectedResults.put("nurses", 500);
        expectedResults.put("appointments", 1000);

        when(bulkDataSeederService.seedBulkData(500, 500, 500, 500, 1000))
                .thenReturn(expectedResults);

        // Act
        ResponseEntity<Map<String, Object>> response = bulkDataSeederController.seedLargeData();

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().get("hospitals"));
        assertEquals(500, response.getBody().get("patients"));
        assertEquals(500, response.getBody().get("doctors"));
        assertEquals(500, response.getBody().get("nurses"));
        assertEquals(1000, response.getBody().get("appointments"));
        verify(bulkDataSeederService, times(1)).seedBulkData(500, 500, 500, 500, 1000);
    }

    @Test
    @DisplayName("seedLargeData - Exception - Returns INTERNAL_SERVER_ERROR with error message")
    void seedLargeData_Exception_ReturnsInternalServerError() {
        // Arrange
        when(bulkDataSeederService.seedBulkData(anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Out of memory"));

        // Act
        ResponseEntity<Map<String, Object>> response = bulkDataSeederController.seedLargeData();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Out of memory", response.getBody().get("error"));
        verify(bulkDataSeederService, times(1)).seedBulkData(500, 500, 500, 500, 1000);
    }

    @Test
    @DisplayName("seedQuickData - Null exception message - Returns INTERNAL_SERVER_ERROR with Unknown error")
    void seedQuickData_NullExceptionMessage_ReturnsUnknownError() {
        // Arrange
        when(bulkDataSeederService.seedBulkData(anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<Map<String, Object>> response = bulkDataSeederController.seedQuickData();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Unknown error", response.getBody().get("error"));
    }
}

