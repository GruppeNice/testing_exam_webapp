package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.DoctorRequest;
import com.testing_exam_webapp.model.mysql.Doctor;
import com.testing_exam_webapp.model.types.DoctorSpecialityType;
import com.testing_exam_webapp.service.DoctorService;
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
 * Test suite for DoctorController.
 * Tests HTTP endpoints, status codes, and response bodies.
 */
@DisplayName("DoctorController Tests")
class DoctorControllerTest {

    private DoctorService doctorService;
    private DoctorController doctorController;
    private Doctor testDoctor;

    @BeforeEach
    void setUp() {
        doctorService = mock(DoctorService.class);
        doctorController = new DoctorController(doctorService);
        testDoctor = TestDataBuilder.createDoctor();
    }

    @Test
    @DisplayName("getDoctors - Should return OK with doctors list")
    void getDoctors_WithDoctors_ReturnsOk() {
        // Arrange
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorService.getDoctors()).thenReturn(doctors);

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.getDoctors();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(doctorService, times(1)).getDoctors();
    }

    @Test
    @DisplayName("getDoctors - Should return NO_CONTENT when empty")
    void getDoctors_EmptyList_ReturnsNoContent() {
        // Arrange
        when(doctorService.getDoctors()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.getDoctors();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).getDoctors();
    }

    @Test
    @DisplayName("getDoctorById - Should return OK with doctor")
    void getDoctorById_ValidId_ReturnsOk() {
        // Arrange
        UUID doctorId = testDoctor.getDoctorId();
        when(doctorService.getDoctorById(doctorId)).thenReturn(testDoctor);

        // Act
        ResponseEntity<Doctor> response = doctorController.getDoctorById(doctorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(doctorId, response.getBody().getDoctorId());
        verify(doctorService, times(1)).getDoctorById(doctorId);
    }

    @Test
    @DisplayName("createDoctor - Should return CREATED with doctor")
    void createDoctor_ValidRequest_ReturnsCreated() {
        // Arrange
        DoctorRequest request = new DoctorRequest();
        request.setDoctorName("Dr. Test");
        request.setSpeciality(DoctorSpecialityType.CARDIOLOGY);

        when(doctorService.createDoctor(any(DoctorRequest.class))).thenReturn(testDoctor);

        // Act
        ResponseEntity<Doctor> response = doctorController.createDoctor(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(doctorService, times(1)).createDoctor(any(DoctorRequest.class));
    }

    @Test
    @DisplayName("updateDoctor - Should return OK with updated doctor")
    void updateDoctor_ValidRequest_ReturnsOk() {
        // Arrange
        UUID doctorId = testDoctor.getDoctorId();
        DoctorRequest request = new DoctorRequest();
        request.setDoctorName("Updated Doctor");

        when(doctorService.updateDoctor(eq(doctorId), any(DoctorRequest.class))).thenReturn(testDoctor);

        // Act
        ResponseEntity<Doctor> response = doctorController.updateDoctor(doctorId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(doctorService, times(1)).updateDoctor(eq(doctorId), any(DoctorRequest.class));
    }

    @Test
    @DisplayName("deleteDoctor - Should return NO_CONTENT")
    void deleteDoctor_ValidId_ReturnsNoContent() {
        // Arrange
        UUID doctorId = testDoctor.getDoctorId();
        doNothing().when(doctorService).deleteDoctor(doctorId);

        // Act
        ResponseEntity<Void> response = doctorController.deleteDoctor(doctorId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).deleteDoctor(doctorId);
    }

    @Test
    @DisplayName("getDoctorsByWardId - Should return OK with doctors")
    void getDoctorsByWardId_ValidWardId_ReturnsOk() {
        // Arrange
        UUID wardId = UUID.randomUUID();
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorService.getDoctorsByWardId(wardId)).thenReturn(doctors);

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.getDoctorsByWardId(wardId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(doctorService, times(1)).getDoctorsByWardId(wardId);
    }

    @Test
    @DisplayName("getDoctorsByWardId - Should return NO_CONTENT when empty")
    void getDoctorsByWardId_EmptyList_ReturnsNoContent() {
        // Arrange
        UUID wardId = UUID.randomUUID();
        when(doctorService.getDoctorsByWardId(wardId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.getDoctorsByWardId(wardId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).getDoctorsByWardId(wardId);
    }

    @Test
    @DisplayName("getDoctorsBySpeciality - Should return OK with doctors")
    void getDoctorsBySpeciality_ValidSpeciality_ReturnsOk() {
        // Arrange
        DoctorSpecialityType speciality = DoctorSpecialityType.CARDIOLOGY;
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorService.getDoctorsBySpeciality(speciality)).thenReturn(doctors);

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.getDoctorsBySpeciality(speciality);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(doctorService, times(1)).getDoctorsBySpeciality(speciality);
    }

    @Test
    @DisplayName("getDoctorsBySpeciality - Should return NO_CONTENT when empty")
    void getDoctorsBySpeciality_EmptyList_ReturnsNoContent() {
        // Arrange
        DoctorSpecialityType speciality = DoctorSpecialityType.NEUROLOGY;
        when(doctorService.getDoctorsBySpeciality(speciality)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.getDoctorsBySpeciality(speciality);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).getDoctorsBySpeciality(speciality);
    }

    @Test
    @DisplayName("getDoctorsByHospitalId - Should return OK with doctors")
    void getDoctorsByHospitalId_ValidHospitalId_ReturnsOk() {
        // Arrange
        UUID hospitalId = UUID.randomUUID();
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorService.getDoctorsByHospitalId(hospitalId)).thenReturn(doctors);

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.getDoctorsByHospitalId(hospitalId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(doctorService, times(1)).getDoctorsByHospitalId(hospitalId);
    }

    @Test
    @DisplayName("getDoctorsByHospitalId - Should return NO_CONTENT when empty")
    void getDoctorsByHospitalId_EmptyList_ReturnsNoContent() {
        // Arrange
        UUID hospitalId = UUID.randomUUID();
        when(doctorService.getDoctorsByHospitalId(hospitalId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Doctor>> response = doctorController.getDoctorsByHospitalId(hospitalId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).getDoctorsByHospitalId(hospitalId);
    }
}

