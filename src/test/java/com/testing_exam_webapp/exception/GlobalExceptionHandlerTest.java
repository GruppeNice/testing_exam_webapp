package com.testing_exam_webapp.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for GlobalExceptionHandler.
 * Tests all exception handler methods to ensure proper HTTP status codes and error responses.
 */
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("handleEntityNotFound - Should return NOT_FOUND with error message")
    void handleEntityNotFound_ReturnsNotFound() {
        // Arrange
        EntityNotFoundException ex = new EntityNotFoundException("Patient not found");

        // Act
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleEntityNotFound(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Patient not found", response.getBody().get("error"));
    }

    @Test
    @DisplayName("handleValidation - Should return BAD_REQUEST with error message")
    void handleValidation_ReturnsBadRequest() {
        // Arrange
        ValidationException ex = new ValidationException("Invalid ward-hospital relationship");

        // Act
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidation(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid ward-hospital relationship", response.getBody().get("error"));
    }

    @Test
    @DisplayName("handleUnauthorized - Should return UNAUTHORIZED with error message")
    void handleUnauthorized_ReturnsUnauthorized() {
        // Arrange
        UnauthorizedException ex = new UnauthorizedException("Invalid credentials");

        // Act
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleUnauthorized(ex);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid credentials", response.getBody().get("error"));
    }

    @Test
    @DisplayName("handleValidationExceptions - Should return BAD_REQUEST with field errors")
    void handleValidationExceptions_ReturnsBadRequestWithFieldErrors() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("patientRequest", "patientName", "Patient name is required");
        FieldError fieldError2 = new FieldError("patientRequest", "dateOfBirth", "Date of birth cannot be null");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // Act
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Patient name is required", response.getBody().get("patientName"));
        assertEquals("Date of birth cannot be null", response.getBody().get("dateOfBirth"));
    }

    @Test
    @DisplayName("handleRuntimeException - Should return INTERNAL_SERVER_ERROR with error message")
    void handleRuntimeException_ReturnsInternalServerError() {
        // Arrange
        RuntimeException ex = new RuntimeException("Unexpected error occurred");

        // Act
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleRuntimeException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error occurred", response.getBody().get("error"));
    }

    @Test
    @DisplayName("handleRuntimeException - Null message should return null in error map")
    void handleRuntimeException_NullMessage_ReturnsNullError() {
        // Arrange
        RuntimeException ex = new RuntimeException((String) null);

        // Act
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleRuntimeException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().get("error"));
    }
}

