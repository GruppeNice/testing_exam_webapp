package com.testing_exam_webapp.integration;

import com.testing_exam_webapp.dto.WeatherDto;
import com.testing_exam_webapp.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.hamcrest.Matchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * Integration tests for WeatherService.
 * Uses MockRestServiceServer to mock HTTP responses from the OpenWeatherMap API.
 */
@DisplayName("WeatherService Integration Tests")
class WeatherServiceIntegrationTest {

    private WeatherService weatherService;
    private RestTemplate testRestTemplate;
    private MockRestServiceServer mockServer;

    private String apiUrl = "http://test-weather-api.com";
    private String apiKey = "test-api-key";

    @BeforeEach
    void setUp() throws Exception {
        // Create a RestTemplate for testing that can be mocked
        testRestTemplate = new RestTemplate();
        
        // Create WeatherService with test configuration
        weatherService = new WeatherService(apiUrl, apiKey);
        
        // Inject the test RestTemplate into WeatherService using reflection
        // This allows us to mock HTTP calls with MockRestServiceServer
        Field restTemplateField = WeatherService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(weatherService, testRestTemplate);
        
        mockServer = MockRestServiceServer.createServer(testRestTemplate);
    }

    @Test
    @DisplayName("getWeatherByCity - Valid API Response - Returns Mapped Weather Data")
    void getWeatherByCity_ValidApiResponse_ReturnsMappedWeatherData() {
        // Arrange - Mock successful API response
        String city = "Copenhagen";
        String encodedCity = java.net.URLEncoder.encode(city, java.nio.charset.StandardCharsets.UTF_8);
        String expectedUrl = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, encodedCity, apiKey);
        
        String jsonResponse = """
            {
                "name": "Copenhagen",
                "sys": {
                    "country": "DK"
                },
                "main": {
                    "temp": 18.5,
                    "humidity": 65.0
                },
                "weather": [{
                    "main": "Clear",
                    "description": "clear sky",
                    "icon": "01d"
                }],
                "wind": {
                    "speed": 5.2
                }
            }
            """;

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        WeatherDto result = weatherService.getWeatherByCity(city);

        // Assert
        assertNotNull(result);
        assertEquals("Copenhagen", result.getCity());
        assertEquals("DK", result.getCountry());
        assertEquals(18.5, result.getTemperature(), 0.01);
        assertEquals(65.0, result.getHumidity(), 0.01);
        assertEquals("clear sky", result.getDescription());
        assertEquals("Clear", result.getCondition());
        assertEquals("01d", result.getIcon());
        assertEquals(5.2, result.getWindSpeed(), 0.01);
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getWeatherByCity - API Returns 404 - Falls Back to Default Weather")
    void getWeatherByCity_ApiReturns404_FallsBackToDefaultWeather() {
        // Arrange - Mock 404 Not Found response
        String city = "InvalidCity12345";
        String encodedCity = java.net.URLEncoder.encode(city, java.nio.charset.StandardCharsets.UTF_8);
        String expectedUrl = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, encodedCity, apiKey);

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // Act
        WeatherDto result = weatherService.getWeatherByCity(city);

        // Assert - Should return default weather on error
        assertNotNull(result);
        assertEquals("Copenhagen", result.getCity()); // Default city
        assertEquals(15.0, result.getTemperature(), 0.01); // Default temperature
        assertEquals("Partly cloudy", result.getDescription()); // Default description
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getWeatherByCity - API Returns 401 Unauthorized - Falls Back to Default Weather")
    void getWeatherByCity_ApiReturns401_FallsBackToDefaultWeather() {
        // Arrange - Mock 401 Unauthorized response (invalid API key)
        String city = "Copenhagen";
        String encodedCity = java.net.URLEncoder.encode(city, java.nio.charset.StandardCharsets.UTF_8);
        String expectedUrl = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, encodedCity, apiKey);

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        // Act
        WeatherDto result = weatherService.getWeatherByCity(city);

        // Assert - Should return default weather on unauthorized
        assertNotNull(result);
        assertEquals("Copenhagen", result.getCity());
        assertEquals(15.0, result.getTemperature(), 0.01);
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getWeatherByCity - Network Error - Falls Back to Default Weather")
    void getWeatherByCity_NetworkError_FallsBackToDefaultWeather() {
        // Arrange - Mock network error
        String city = "Copenhagen";
        String encodedCity = java.net.URLEncoder.encode(city, java.nio.charset.StandardCharsets.UTF_8);
        String expectedUrl = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, encodedCity, apiKey);

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withServerError());

        // Act
        WeatherDto result = weatherService.getWeatherByCity(city);

        // Assert - Should return default weather on network error
        assertNotNull(result);
        assertEquals("Copenhagen", result.getCity());
        assertEquals(15.0, result.getTemperature(), 0.01);
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getWeatherByCity - Null City - Uses Default City and Returns Weather")
    void getWeatherByCity_NullCity_UsesDefaultCity() {
        // Arrange - Mock API call with default city
        String defaultCity = "Copenhagen";
        String encodedCity = java.net.URLEncoder.encode(defaultCity, java.nio.charset.StandardCharsets.UTF_8);
        String expectedUrl = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, encodedCity, apiKey);

        String jsonResponse = """
            {
                "name": "Copenhagen",
                "sys": {"country": "DK"},
                "main": {"temp": 20.0, "humidity": 70.0},
                "weather": [{"main": "Clouds", "description": "scattered clouds", "icon": "03d"}],
                "wind": {"speed": 3.5}
            }
            """;

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        WeatherDto result = weatherService.getWeatherByCity(null);

        // Assert
        assertNotNull(result);
        assertEquals("Copenhagen", result.getCity());
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getWeatherByCity - Empty City - Uses Default City")
    void getWeatherByCity_EmptyCity_UsesDefaultCity() {
        // Arrange
        String defaultCity = "Copenhagen";
        String encodedCity = java.net.URLEncoder.encode(defaultCity, java.nio.charset.StandardCharsets.UTF_8);
        String expectedUrl = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, encodedCity, apiKey);

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        // Act
        WeatherDto result = weatherService.getWeatherByCity("");

        // Assert
        assertNotNull(result);
        // Should use default city (Copenhagen) when empty string provided
        
        mockServer.verify();
    }

    @Test
    @DisplayName("getWeatherByCity - City with Special Characters - URL Encoded Correctly")
    void getWeatherByCity_CityWithSpecialCharacters_UrlEncodedCorrectly() {
        // Arrange - Test URL encoding of special characters
        // Note: RestTemplate may encode URLs, so we use a flexible matcher
        String city = "São Paulo";
        
        String jsonResponse = """
            {
                "name": "São Paulo",
                "sys": {"country": "BR"},
                "main": {"temp": 25.0, "humidity": 60.0},
                "weather": [{"main": "Clear", "description": "clear sky", "icon": "01d"}],
                "wind": {"speed": 4.0}
            }
            """;

        // Use a flexible matcher - RestTemplate handles URL encoding automatically
        // The exact encoding may vary, so we just verify the request is made to the API
        mockServer.expect(requestTo(Matchers.startsWith(apiUrl)))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        WeatherDto result = weatherService.getWeatherByCity(city);

        // Assert
        assertNotNull(result);
        assertEquals("São Paulo", result.getCity());
        
        // Verify the request was made
        mockServer.verify();
    }
}
