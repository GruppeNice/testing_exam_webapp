package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.AppointmentRequest;
import com.testing_exam_webapp.model.mysql.Appointment;
import com.testing_exam_webapp.model.types.AppointmentStatusType;
import com.testing_exam_webapp.service.AppointmentService;
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
 * Test suite for AppointmentController.
 * Tests HTTP endpoints, status codes, and response bodies.
 */
@DisplayName("AppointmentController Tests")
class AppointmentControllerTest {

    private AppointmentService appointmentService;
    private AppointmentController appointmentController;
    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        appointmentService = mock(AppointmentService.class);
        appointmentController = new AppointmentController(appointmentService);
        
        // Create test appointment manually since TestDataBuilder doesn't have createAppointment()
        testAppointment = new Appointment();
        testAppointment.setAppointmentId(UUID.randomUUID());
        testAppointment.setAppointmentDate(LocalDate.now().plusDays(1));
        testAppointment.setStatus(AppointmentStatusType.SCHEDULED);
        testAppointment.setReason("Test appointment");
    }

    @Test
    @DisplayName("getAppointments - Should return OK with appointments list")
    void getAppointments_WithAppointments_ReturnsOk() {
        // Arrange
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentService.getAppointments()).thenReturn(appointments);

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointments();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getAppointments();
    }

    @Test
    @DisplayName("getAppointments - Should return NO_CONTENT when empty")
    void getAppointments_EmptyList_ReturnsNoContent() {
        // Arrange
        when(appointmentService.getAppointments()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointments();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService, times(1)).getAppointments();
    }

    @Test
    @DisplayName("getAppointmentById - Should return OK with appointment")
    void getAppointmentById_ValidId_ReturnsOk() {
        // Arrange
        UUID appointmentId = testAppointment.getAppointmentId();
        when(appointmentService.getAppointmentById(appointmentId)).thenReturn(testAppointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.getAppointmentById(appointmentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(appointmentId, response.getBody().getAppointmentId());
        verify(appointmentService, times(1)).getAppointmentById(appointmentId);
    }

    @Test
    @DisplayName("createAppointment - Should return CREATED with appointment")
    void createAppointment_ValidRequest_ReturnsCreated() {
        // Arrange
        AppointmentRequest request = new AppointmentRequest();
        request.setAppointmentDate(LocalDate.now().plusDays(1));
        request.setStatus(AppointmentStatusType.SCHEDULED);

        when(appointmentService.createAppointment(any(AppointmentRequest.class))).thenReturn(testAppointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.createAppointment(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).createAppointment(any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("updateAppointment - Should return OK with updated appointment")
    void updateAppointment_ValidRequest_ReturnsOk() {
        // Arrange
        UUID appointmentId = testAppointment.getAppointmentId();
        AppointmentRequest request = new AppointmentRequest();
        request.setStatus(AppointmentStatusType.COMPLETED);

        when(appointmentService.updateAppointment(eq(appointmentId), any(AppointmentRequest.class)))
                .thenReturn(testAppointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.updateAppointment(appointmentId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).updateAppointment(eq(appointmentId), any(AppointmentRequest.class));
    }

    @Test
    @DisplayName("deleteAppointment - Should return NO_CONTENT")
    void deleteAppointment_ValidId_ReturnsNoContent() {
        // Arrange
        UUID appointmentId = testAppointment.getAppointmentId();
        doNothing().when(appointmentService).deleteAppointment(appointmentId);

        // Act
        ResponseEntity<Void> response = appointmentController.deleteAppointment(appointmentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService, times(1)).deleteAppointment(appointmentId);
    }

    @Test
    @DisplayName("getAppointmentsByPatientId - Should return OK with appointments")
    void getAppointmentsByPatientId_ValidPatientId_ReturnsOk() {
        // Arrange
        UUID patientId = UUID.randomUUID();
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentService.getAppointmentsByPatientId(patientId)).thenReturn(appointments);

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByPatientId(patientId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getAppointmentsByPatientId(patientId);
    }

    @Test
    @DisplayName("getAppointmentsByPatientId - Should return NO_CONTENT when empty")
    void getAppointmentsByPatientId_EmptyList_ReturnsNoContent() {
        // Arrange
        UUID patientId = UUID.randomUUID();
        when(appointmentService.getAppointmentsByPatientId(patientId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByPatientId(patientId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService, times(1)).getAppointmentsByPatientId(patientId);
    }

    @Test
    @DisplayName("getAppointmentsByDoctorId - Should return OK with appointments")
    void getAppointmentsByDoctorId_ValidDoctorId_ReturnsOk() {
        // Arrange
        UUID doctorId = UUID.randomUUID();
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentService.getAppointmentsByDoctorId(doctorId)).thenReturn(appointments);

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByDoctorId(doctorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getAppointmentsByDoctorId(doctorId);
    }

    @Test
    @DisplayName("getAppointmentsByStatus - Should return OK with appointments")
    void getAppointmentsByStatus_ValidStatus_ReturnsOk() {
        // Arrange
        AppointmentStatusType status = AppointmentStatusType.SCHEDULED;
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentService.getAppointmentsByStatus(status)).thenReturn(appointments);

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByStatus(status);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getAppointmentsByStatus(status);
    }

    @Test
    @DisplayName("getAppointmentsByDate - Should return OK with appointments")
    void getAppointmentsByDate_ValidDate_ReturnsOk() {
        // Arrange
        LocalDate date = LocalDate.now();
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentService.getAppointmentsByDate(date)).thenReturn(appointments);

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByDate(date);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getAppointmentsByDate(date);
    }

    @Test
    @DisplayName("getAppointmentsByDateRange - Should return OK with appointments")
    void getAppointmentsByDateRange_ValidRange_ReturnsOk() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentService.getAppointmentsByDateRange(startDate, endDate)).thenReturn(appointments);

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByDateRange(startDate, endDate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getAppointmentsByDateRange(startDate, endDate);
    }

    @Test
    @DisplayName("getAppointmentsByDateRange - Should return NO_CONTENT when empty")
    void getAppointmentsByDateRange_EmptyList_ReturnsNoContent() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        when(appointmentService.getAppointmentsByDateRange(startDate, endDate)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByDateRange(startDate, endDate);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService, times(1)).getAppointmentsByDateRange(startDate, endDate);
    }
}

