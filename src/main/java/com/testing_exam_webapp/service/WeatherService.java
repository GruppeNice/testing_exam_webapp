package com.testing_exam_webapp.service;

import com.testing_exam_webapp.dto.WeatherDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unchecked")
@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private static final String DEFAULT_CITY = "Copenhagen";
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;

    public WeatherService(@Value("${weather.api.url}") String apiUrl,
                         @Value("${weather.api.key}") String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }

    public WeatherDto getWeatherByCity(String city) {
        // Use default city if city is null or empty
        String cityToUse = (city == null || city.trim().isEmpty()) ? DEFAULT_CITY : city.trim();
        
        // Convert Danish city names to English for API compatibility
        String originalCity = cityToUse;
        cityToUse = normalizeCityName(cityToUse);
        
        // Debug: Log the normalization (remove after testing)
        if (!originalCity.equals(cityToUse)) {
            logger.debug("Normalized city from '{}' to '{}'", originalCity, cityToUse);
        }

        // Check if API key is configured
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return createDefaultWeather(cityToUse);
        }

        try {
            // URL encode the city name to handle special characters
            String encodedCity = URLEncoder.encode(cityToUse, StandardCharsets.UTF_8);
            String url = String.format("%s?q=%s&appid=%s&units=metric", 
                    apiUrl, encodedCity, apiKey);
            
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    return mapToWeatherDto(responseBody, cityToUse);
                }
            }
            // Non-OK status or null body (shouldn't happen as RestTemplate throws exceptions, but safety check)
            return createDefaultWeather(cityToUse);
        } catch (HttpClientErrorException.Unauthorized e) {
            // Invalid API key - return default weather silently
            logger.debug("Invalid API key, returning default weather");
            return createDefaultWeather(DEFAULT_CITY);
        } catch (HttpClientErrorException.NotFound e) {
            // City not found - return default weather with default city
            logger.debug("City '{}' not found, returning default weather", cityToUse);
            return createDefaultWeather(DEFAULT_CITY);
        } catch (HttpClientErrorException e) {
            // Other HTTP errors - return default weather
            logger.debug("HTTP error fetching weather for '{}': {}", cityToUse, e.getMessage());
            return createDefaultWeather(cityToUse);
        } catch (org.springframework.web.client.RestClientException e) {
            // RestTemplate errors (network issues, etc.) - return default weather
            logger.debug("Network error fetching weather for '{}': {}", cityToUse, e.getMessage());
            return createDefaultWeather(cityToUse);
        }
    }

    /**
     * Normalizes city names from Danish to English for API compatibility
     */
    private String normalizeCityName(String city) {
        if (city == null) {
            return DEFAULT_CITY;
        }
        
        String normalized = city.trim();
        String lowerNormalized = normalized.toLowerCase(Locale.ROOT);
        
        // Copenhagen/København - catch all variations including encoding issues
        // Check if it contains both "k" (or "K") and "benhavn" anywhere in the string
        if ((lowerNormalized.contains("k") || normalized.contains("K")) && 
            (lowerNormalized.contains("benhavn") || normalized.contains("benhavn"))) {
            return DEFAULT_CITY;
        }
        
        // Exact matches for common variations
        if (lowerNormalized.equals("københavn") || 
            lowerNormalized.equals("kobenhavn") || 
            lowerNormalized.equals("kbh") || 
            lowerNormalized.equals("copenhagen")) {
            return DEFAULT_CITY;
        }
        // Return as-is if no mapping found
        return normalized;
    }

    private WeatherDto mapToWeatherDto(Map<String, Object> response, String city) {
        WeatherDto dto = new WeatherDto();
        
        // Extract city and country
        Map<String, Object> sys = (Map<String, Object>) response.get("sys");
        if (sys != null) {
            dto.setCountry((String) sys.get("country"));
        }
        dto.setCity(city);

        // Extract main weather data
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        if (main != null) {
            Object tempObj = main.get("temp");
            if (tempObj instanceof Number number) {
                dto.setTemperature(number.doubleValue());
            }
            Object humidityObj = main.get("humidity");
            if (humidityObj instanceof Number number) {
                dto.setHumidity(number.doubleValue());
            }
        }

        // Extract weather description
        java.util.List<Map<String, Object>> weatherList = (java.util.List<Map<String, Object>>) response.get("weather");
        if (weatherList != null && !weatherList.isEmpty()) {
            Map<String, Object> weather = weatherList.get(0);
            if (weather != null) {
                dto.setDescription((String) weather.get("description"));
                dto.setCondition((String) weather.get("main"));
                dto.setIcon((String) weather.get("icon"));
            }
        }

        // Extract wind speed
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        if (wind != null) {
            Object speedObj = wind.get("speed");
            if (speedObj instanceof Number number) {
                dto.setWindSpeed(number.doubleValue());
            }
        }

        return dto;
    }

    private WeatherDto createDefaultWeather(String city) {
        WeatherDto dto = new WeatherDto();
        dto.setCity(city);
        dto.setCountry("DK");
        dto.setTemperature(15.0);
        dto.setDescription("Partly cloudy");
        dto.setCondition("Clouds");
        dto.setIcon("02d");
        dto.setHumidity(65.0);
        dto.setWindSpeed(10.0);
        return dto;
    }
}

