package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.HospitalRequest;
import com.testing_exam_webapp.model.mysql.Hospital;
import com.testing_exam_webapp.service.HospitalService;
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
 * Test suite for HospitalController.
 * Tests HTTP endpoints, status codes, and response bodies for hospital operations.
 */
@DisplayName("HospitalController Tests")
class HospitalControllerTest {

    private HospitalService hospitalService;
    private HospitalController hospitalController;
    private Hospital testHospital;
    private HospitalRequest hospitalRequest;

    @BeforeEach
    void setUp() {
        hospitalService = mock(HospitalService.class);
        hospitalController = new HospitalController(hospitalService);
        
        testHospital = TestDataBuilder.createHospital();
        
        hospitalRequest = new HospitalRequest();
        hospitalRequest.setHospitalName("Test Hospital");
        hospitalRequest.setAddress("123 Test Street");
        hospitalRequest.setCity("Test City");
    }

    @Test
    @DisplayName("getHospitals - With hospitals - Returns OK")
    void getHospitals_WithHospitals_ReturnsOk() {
        List<Hospital> hospitals = Arrays.asList(testHospital);
        when(hospitalService.getHospitals()).thenReturn(hospitals);

        ResponseEntity<List<Hospital>> response = hospitalController.getHospitals();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(hospitalService, times(1)).getHospitals();
    }

    @Test
    @DisplayName("getHospitals - Empty list - Returns NO_CONTENT")
    void getHospitals_EmptyList_ReturnsNoContent() {
        when(hospitalService.getHospitals()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Hospital>> response = hospitalController.getHospitals();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(hospitalService, times(1)).getHospitals();
    }

    @Test
    @DisplayName("getHospitalById - Valid ID - Returns OK")
    void getHospitalById_ValidId_ReturnsOk() {
        UUID hospitalId = testHospital.getHospitalId();
        when(hospitalService.getHospitalById(hospitalId)).thenReturn(testHospital);

        ResponseEntity<Hospital> response = hospitalController.getHospitalById(hospitalId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hospitalId, response.getBody().getHospitalId());
        verify(hospitalService, times(1)).getHospitalById(hospitalId);
    }

    @Test
    @DisplayName("createHospital - Valid request - Returns CREATED")
    void createHospital_ValidRequest_ReturnsCreated() {
        when(hospitalService.createHospital(any(HospitalRequest.class))).thenReturn(testHospital);

        ResponseEntity<Hospital> response = hospitalController.createHospital(hospitalRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(hospitalService, times(1)).createHospital(hospitalRequest);
    }

    @Test
    @DisplayName("updateHospital - Valid request - Returns OK")
    void updateHospital_ValidRequest_ReturnsOk() {
        UUID hospitalId = testHospital.getHospitalId();
        when(hospitalService.updateHospital(eq(hospitalId), any(HospitalRequest.class))).thenReturn(testHospital);

        ResponseEntity<Hospital> response = hospitalController.updateHospital(hospitalId, hospitalRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(hospitalService, times(1)).updateHospital(hospitalId, hospitalRequest);
    }

    @Test
    @DisplayName("deleteHospital - Valid ID - Returns NO_CONTENT")
    void deleteHospital_ValidId_ReturnsNoContent() {
        UUID hospitalId = testHospital.getHospitalId();
        doNothing().when(hospitalService).deleteHospital(hospitalId);

        ResponseEntity<Void> response = hospitalController.deleteHospital(hospitalId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(hospitalService, times(1)).deleteHospital(hospitalId);
    }

    @Test
    @DisplayName("getHospitalsByCity - With hospitals - Returns OK")
    void getHospitalsByCity_WithHospitals_ReturnsOk() {
        String city = "Test City";
        List<Hospital> hospitals = Arrays.asList(testHospital);
        when(hospitalService.getHospitalsByCity(city)).thenReturn(hospitals);

        ResponseEntity<List<Hospital>> response = hospitalController.getHospitalsByCity(city);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(hospitalService, times(1)).getHospitalsByCity(city);
    }

    @Test
    @DisplayName("getHospitalsByCity - Empty list - Returns NO_CONTENT")
    void getHospitalsByCity_EmptyList_ReturnsNoContent() {
        String city = "NonExistent City";
        when(hospitalService.getHospitalsByCity(city)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Hospital>> response = hospitalController.getHospitalsByCity(city);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(hospitalService, times(1)).getHospitalsByCity(city);
    }
}

