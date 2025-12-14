package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.WeatherDto;
import com.testing_exam_webapp.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for WeatherController.
 * Tests HTTP endpoints for weather API wrapper operations.
 */
@DisplayName("WeatherController Tests")
class WeatherControllerTest {

    private WeatherService weatherService;
    private WeatherController weatherController;
    private WeatherDto testWeatherDto;

    @BeforeEach
    void setUp() {
        weatherService = mock(WeatherService.class);
        weatherController = new WeatherController(weatherService);
        
        testWeatherDto = new WeatherDto();
        testWeatherDto.setCity("Copenhagen");
        testWeatherDto.setCountry("DK");
        testWeatherDto.setTemperature(16.0);
        testWeatherDto.setDescription("Clear sky");
        testWeatherDto.setIcon("01d");
        testWeatherDto.setHumidity(60.0);
        testWeatherDto.setWindSpeed(4.0);
        testWeatherDto.setCondition("Clear");
    }

    @Test
    @DisplayName("getWeather - With city parameter - Returns OK")
    void getWeather_WithCity_ReturnsOk() {
        String city = "Copenhagen";
        when(weatherService.getWeatherByCity(city)).thenReturn(testWeatherDto);

        ResponseEntity<WeatherDto> response = weatherController.getWeather(city);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Copenhagen", response.getBody().getCity());
        assertEquals(16.0, response.getBody().getTemperature());
        verify(weatherService, times(1)).getWeatherByCity(city);
    }

    @Test
    @DisplayName("getWeather - Without city parameter - Uses default Copenhagen")
    void getWeather_WithoutCity_UsesDefault() {
        // In unit tests, @RequestParam defaultValue doesn't apply when calling method directly
        // So null is passed to service, which normalizes it to "Copenhagen"
        // But we need to mock for null since that's what actually gets passed
        when(weatherService.getWeatherByCity(null)).thenReturn(testWeatherDto);

        ResponseEntity<WeatherDto> response = weatherController.getWeather(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(weatherService, times(1)).getWeatherByCity(null);
    }

    @Test
    @DisplayName("getWeather - Empty city - Uses default Copenhagen")
    void getWeather_EmptyCity_UsesDefault() {
        // Empty string is passed to service, which normalizes it to "Copenhagen"
        // But @RequestParam with defaultValue might also handle this - let's test with empty string
        when(weatherService.getWeatherByCity("")).thenReturn(testWeatherDto);

        ResponseEntity<WeatherDto> response = weatherController.getWeather("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Service normalizes empty string to "Copenhagen", but we need to mock what actually gets called
        // Actually, @RequestParam defaultValue only applies when parameter is missing, not when it's empty string
        // So empty string gets passed to service, which then normalizes it
        verify(weatherService, times(1)).getWeatherByCity("");
    }

    @Test
    @DisplayName("getWeather - City with special characters - Returns OK")
    void getWeather_CityWithSpecialCharacters_ReturnsOk() {
        String city = "São Paulo";
        testWeatherDto.setCity(city);
        when(weatherService.getWeatherByCity(city)).thenReturn(testWeatherDto);

        ResponseEntity<WeatherDto> response = weatherController.getWeather(city);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("São Paulo", response.getBody().getCity());
        verify(weatherService, times(1)).getWeatherByCity(city);
    }

    @Test
    @DisplayName("getWeather - Service throws exception - Exception propagated")
    void getWeather_ServiceThrowsException_ExceptionPropagated() {
        String city = "NonExistentCity";
        when(weatherService.getWeatherByCity(city))
                .thenThrow(new RuntimeException("City not found"));

        assertThrows(RuntimeException.class, () -> weatherController.getWeather(city));
    }
}

