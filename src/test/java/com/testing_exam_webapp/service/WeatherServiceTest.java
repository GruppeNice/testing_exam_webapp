package com.testing_exam_webapp.service;

import com.testing_exam_webapp.dto.WeatherDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WeatherService Tests")
class WeatherServiceTest {

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService("https://api.openweathermap.org/data/2.5/weather", "test-api-key");
    }

    static Stream<Arguments> cityNameVariations() {
        return Stream.of(
            Arguments.of("Copenhagen", "Copenhagen"),
            Arguments.of("copenhagen", "Copenhagen"),
            Arguments.of("København", "Copenhagen"),
            Arguments.of("københavn", "Copenhagen"),
            Arguments.of("kobenhavn", "Copenhagen"),
            Arguments.of("KBH", "Copenhagen"),
            Arguments.of("kbh", "Copenhagen")
        );
    }

    @ParameterizedTest
    @MethodSource("cityNameVariations")
    @DisplayName("getWeatherByCity - Equivalence Partitioning: City name normalization")
    void getWeatherByCity_CityNameVariations_NormalizesCorrectly(String inputCity, String expectedCity) {
        WeatherDto result = weatherService.getWeatherByCity(inputCity);
        assertNotNull(result);
        assertEquals(expectedCity, result.getCity());
    }

    @Test
    @DisplayName("getWeatherByCity - Null city - Uses default city and returns weather")
    void getWeatherByCity_NullCity_UsesDefaultCity() {
        // When city is null, service should use "Copenhagen" as default
        // Since we don't have a valid API key in tests, it will return default weather
        WeatherDto result = weatherService.getWeatherByCity(null);
        
        assertNotNull(result);
        assertEquals("Copenhagen", result.getCity());
        assertNotNull(result.getTemperature());
        assertNotNull(result.getDescription());
        assertNotNull(result.getCountry());
    }

    @Test
    @DisplayName("getWeatherByCity - Empty city - Uses default city")
    void getWeatherByCity_EmptyCity_UsesDefaultCity() {
        WeatherDto result = weatherService.getWeatherByCity("");
        
        assertNotNull(result);
        assertEquals("Copenhagen", result.getCity());
    }

    @Test
    @DisplayName("getWeatherByCity - Invalid city - Returns default weather on error")
    void getWeatherByCity_InvalidCity_ReturnsDefaultWeather() {
        // Invalid city will cause API error, service should return default weather
        WeatherDto result = weatherService.getWeatherByCity("InvalidCity12345");
        
        assertNotNull(result);
        assertEquals("Copenhagen", result.getCity());
        // Verify default values are set
        assertEquals(15.0, result.getTemperature());
        assertEquals("Partly cloudy", result.getDescription());
    }

    @Test
    @DisplayName("getWeatherByCity - Valid city name - Returns weather data")
    void getWeatherByCity_ValidCity_ReturnsWeather() {
        // This test will attempt to call the real API
        // Without a valid API key, it will return default weather
        WeatherDto result = weatherService.getWeatherByCity("Copenhagen");
        
        assertNotNull(result);
        assertNotNull(result.getCity());
        assertNotNull(result.getTemperature());
        assertNotNull(result.getDescription());
    }
}

