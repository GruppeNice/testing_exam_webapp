package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.TimeDto;
import com.testing_exam_webapp.service.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test suite for TimeController.
 * Tests HTTP endpoints for time API wrapper operations.
 */
@DisplayName("TimeController Tests")
class TimeControllerTest {

    private TimeService timeService;
    private TimeController timeController;
    private TimeDto testTimeDto;

    @BeforeEach
    void setUp() {
        timeService = mock(TimeService.class);
        timeController = new TimeController(timeService);
        
        testTimeDto = new TimeDto();
        testTimeDto.setDatetime("2024-06-15T12:00:00");
        testTimeDto.setTimezone("Europe/Copenhagen");
        testTimeDto.setAbbreviation("CET");
        testTimeDto.setDayOfWeek(6);
        testTimeDto.setDayOfYear(167);
    }

    @Test
    @DisplayName("getCurrentTime - With timezone parameter - Returns OK")
    void getCurrentTime_WithTimezone_ReturnsOk() {
        String timezone = "Europe/Copenhagen";
        when(timeService.getCurrentTime(timezone)).thenReturn(testTimeDto);

        ResponseEntity<TimeDto> response = timeController.getCurrentTime(timezone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Europe/Copenhagen", response.getBody().getTimezone());
        verify(timeService, times(1)).getCurrentTime(timezone);
    }

    @Test
    @DisplayName("getCurrentTime - Without timezone parameter - Returns OK with default")
    void getCurrentTime_WithoutTimezone_ReturnsOk() {
        when(timeService.getCurrentTime()).thenReturn(testTimeDto);

        ResponseEntity<TimeDto> response = timeController.getCurrentTime(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(timeService, times(1)).getCurrentTime();
        verify(timeService, never()).getCurrentTime(anyString());
    }

    @Test
    @DisplayName("getCurrentTime - Empty timezone - Uses default")
    void getCurrentTime_EmptyTimezone_UsesDefault() {
        // Empty string is not null, so it calls getCurrentTime("") not getCurrentTime()
        when(timeService.getCurrentTime("")).thenReturn(testTimeDto);

        ResponseEntity<TimeDto> response = timeController.getCurrentTime("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(timeService, times(1)).getCurrentTime("");
        verify(timeService, never()).getCurrentTime();
    }

    @Test
    @DisplayName("getCurrentTime - Service throws exception - Exception propagated")
    void getCurrentTime_ServiceThrowsException_ExceptionPropagated() {
        String timezone = "Invalid/Timezone";
        when(timeService.getCurrentTime(timezone))
                .thenThrow(new RuntimeException("Invalid timezone"));

        assertThrows(RuntimeException.class, () -> timeController.getCurrentTime(timezone));
    }
}

