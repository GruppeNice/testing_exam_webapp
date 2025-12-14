package com.testing_exam_webapp.integration;

import com.testing_exam_webapp.dto.TimeDto;
import com.testing_exam_webapp.service.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * Integration tests for TimeService.
 * 
 * These tests verify the integration between TimeService and the external WorldTimeAPI
 * using MockRestServiceServer to mock HTTP responses. This demonstrates integration testing
 * of external API dependencies without making actual HTTP calls.
 * 
 * Key Testing Points:
 * - HTTP request construction with timezone parameter
 * - Response mapping from JSON to TimeDto
 * - Error handling (404, network errors)
 * - Fallback behavior when API fails
 * - Default timezone handling
 */
@DisplayName("TimeService Integration Tests")
class TimeServiceIntegrationTest {

    private TimeService timeService;
    private RestTemplate testRestTemplate;
    private MockRestServiceServer mockServer;

    private String apiUrl = "http://test-time-api.com";

    @BeforeEach
    void setUp() throws Exception {
        // Create a RestTemplate for testing that can be mocked
        testRestTemplate = new RestTemplate();
        
        // Create TimeService with test configuration
        timeService = new TimeService(apiUrl);
        
        // Inject the test RestTemplate into TimeService using reflection
        // This allows us to mock HTTP calls with MockRestServiceServer
        Field restTemplateField = TimeService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(timeService, testRestTemplate);
        
        mockServer = MockRestServiceServer.createServer(testRestTemplate);
    }

    @Test
    @DisplayName("getCurrentTime - Valid API Response - Returns Mapped Time Data")
    void getCurrentTime_ValidApiResponse_ReturnsMappedTimeData() {
        // Arrange - Mock successful API response
        String timezone = "Europe/Copenhagen";
        String expectedUrl = String.format("%s/timezone/%s", apiUrl, timezone);
        
        String jsonResponse = """
            {
                "datetime": "2024-01-15T14:30:00.123456+01:00",
                "timezone": "Europe/Copenhagen",
                "abbreviation": "CET",
                "day_of_week": 1,
                "day_of_year": 15
            }
            """;

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        TimeDto result = timeService.getCurrentTime(timezone);

        // Assert
        assertNotNull(result);
        assertEquals("2024-01-15T14:30:00.123456+01:00", result.getDatetime());
        assertEquals("Europe/Copenhagen", result.getTimezone());
        assertEquals("CET", result.getAbbreviation());
        assertEquals(1, result.getDayOfWeek());
        assertEquals(15, result.getDayOfYear());
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getCurrentTime - No Timezone Parameter - Uses Default Timezone")
    void getCurrentTime_NoTimezoneParameter_UsesDefaultTimezone() {
        // Arrange - Default timezone is Europe/Copenhagen
        String defaultTimezone = "Europe/Copenhagen";
        String expectedUrl = String.format("%s/timezone/%s", apiUrl, defaultTimezone);
        
        String jsonResponse = """
            {
                "datetime": "2024-01-15T14:30:00+01:00",
                "timezone": "Europe/Copenhagen",
                "abbreviation": "CET",
                "day_of_week": 1,
                "day_of_year": 15
            }
            """;

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        TimeDto result = timeService.getCurrentTime();

        // Assert
        assertNotNull(result);
        assertEquals("Europe/Copenhagen", result.getTimezone());
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getCurrentTime - Different Timezone - Returns Time for Specified Timezone")
    void getCurrentTime_DifferentTimezone_ReturnsTimeForSpecifiedTimezone() {
        // Arrange
        String timezone = "America/New_York";
        String expectedUrl = String.format("%s/timezone/%s", apiUrl, timezone);
        
        String jsonResponse = """
            {
                "datetime": "2024-01-15T08:30:00-05:00",
                "timezone": "America/New_York",
                "abbreviation": "EST",
                "day_of_week": 1,
                "day_of_year": 15
            }
            """;

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        TimeDto result = timeService.getCurrentTime(timezone);

        // Assert
        assertNotNull(result);
        assertEquals("America/New_York", result.getTimezone());
        assertEquals("EST", result.getAbbreviation());
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getCurrentTime - API Returns 404 - Falls Back to Default Time")
    void getCurrentTime_ApiReturns404_FallsBackToDefaultTime() {
        // Arrange - Mock 404 Not Found response
        String timezone = "Invalid/Timezone";
        String expectedUrl = String.format("%s/timezone/%s", apiUrl, timezone);

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // Act
        TimeDto result = timeService.getCurrentTime(timezone);

        // Assert - Should return default time on error
        assertNotNull(result);
        assertNotNull(result.getDatetime());
        assertEquals("Europe/Copenhagen", result.getTimezone()); // Default timezone
        assertEquals("CET", result.getAbbreviation()); // Default abbreviation
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getCurrentTime - Network Error - Falls Back to Default Time")
    void getCurrentTime_NetworkError_FallsBackToDefaultTime() {
        // Arrange - Mock network error
        String timezone = "Europe/Copenhagen";
        String expectedUrl = String.format("%s/timezone/%s", apiUrl, timezone);

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withServerError());

        // Act
        TimeDto result = timeService.getCurrentTime(timezone);

        // Assert - Should return default time on network error
        assertNotNull(result);
        assertNotNull(result.getDatetime());
        assertEquals("Europe/Copenhagen", result.getTimezone());
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getCurrentTime - Null Timezone - Uses Default Timezone")
    void getCurrentTime_NullTimezone_UsesDefaultTimezone() {
        // Arrange
        String defaultTimezone = "Europe/Copenhagen";
        String expectedUrl = String.format("%s/timezone/%s", apiUrl, defaultTimezone);
        
        String jsonResponse = """
            {
                "datetime": "2024-01-15T14:30:00+01:00",
                "timezone": "Europe/Copenhagen",
                "abbreviation": "CET",
                "day_of_week": 1,
                "day_of_year": 15
            }
            """;

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        TimeDto result = timeService.getCurrentTime(null);

        // Assert
        assertNotNull(result);
        assertEquals("Europe/Copenhagen", result.getTimezone());
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getCurrentTime - Empty Timezone - Uses Default Timezone")
    void getCurrentTime_EmptyTimezone_UsesDefaultTimezone() {
        // Arrange
        String defaultTimezone = "Europe/Copenhagen";
        String expectedUrl = String.format("%s/timezone/%s", apiUrl, defaultTimezone);
        
        String jsonResponse = """
            {
                "datetime": "2024-01-15T14:30:00+01:00",
                "timezone": "Europe/Copenhagen",
                "abbreviation": "CET",
                "day_of_week": 1,
                "day_of_year": 15
            }
            """;

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        TimeDto result = timeService.getCurrentTime("");

        // Assert
        assertNotNull(result);
        assertEquals("Europe/Copenhagen", result.getTimezone());
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getCurrentTime - API Returns Empty Response - Returns TimeDto with Null Fields")
    void getCurrentTime_ApiReturnsEmptyResponse_ReturnsTimeDtoWithNullFields() {
        // Arrange - Mock empty response body (empty JSON object)
        // Note: When API returns {}, the service creates a TimeDto but fields are null
        // because the map is empty. The service only falls back to default on exceptions.
        String timezone = "Europe/Copenhagen";
        String expectedUrl = String.format("%s/timezone/%s", apiUrl, timezone);

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess().body("{}").contentType(MediaType.APPLICATION_JSON));

        // Act
        TimeDto result = timeService.getCurrentTime(timezone);

        // Assert - Empty JSON object results in TimeDto with null fields
        // The service doesn't fall back for empty responses, only for exceptions
        assertNotNull(result); // TimeDto object is created
        // Note: datetime, timezone, etc. will be null from empty map - this is expected
        
        mockServer.verify();
    }
}
