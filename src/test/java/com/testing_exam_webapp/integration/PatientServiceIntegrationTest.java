package com.testing_exam_webapp.integration;

import com.testing_exam_webapp.dto.PatientRequest;
import com.testing_exam_webapp.exception.EntityNotFoundException;
import com.testing_exam_webapp.exception.ValidationException;
import com.testing_exam_webapp.model.mysql.*;
import com.testing_exam_webapp.repository.*;
import com.testing_exam_webapp.service.PatientService;
import com.testing_exam_webapp.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for PatientService.
 * Uses an in-memory H2 database to test service and repository integration.
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(PatientService.class)
@Transactional
@DisplayName("PatientService Integration Tests")
class PatientServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    private Hospital testHospital;
    private Ward testWard;
    private Ward testWard2;
    private Diagnosis testDiagnosis;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        patientRepository.deleteAll();
        diagnosisRepository.deleteAll();
        wardRepository.deleteAll();
        hospitalRepository.deleteAll();

        // Create test data
        testHospital = TestDataBuilder.createHospital("Test Hospital", "123 Test St", "Copenhagen");
        testHospital = entityManager.persistAndFlush(testHospital);

        testWard = TestDataBuilder.createWard();
        testWard = entityManager.persistAndFlush(testWard);

        testWard2 = TestDataBuilder.createWard(com.testing_exam_webapp.model.types.WardType.NEUROLOGY, 25);
        testWard2 = entityManager.persistAndFlush(testWard2);

        // Associate ward with hospital
        TestDataBuilder.associateWardWithHospital(testWard, testHospital);
        entityManager.persistAndFlush(testWard);
        entityManager.persistAndFlush(testHospital);

        testDiagnosis = TestDataBuilder.createDiagnosis();
        testDiagnosis = entityManager.persistAndFlush(testDiagnosis);
    }

    @Test
    @DisplayName("createPatient - Valid Request - Persists Patient to Database")
    void createPatient_ValidRequest_PersistsPatientToDatabase() {
        // Arrange
        PatientRequest request = new PatientRequest();
        request.setPatientName("John Doe");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setGender("Male");

        // Act
        Patient result = patientService.createPatient(request);

        // Assert
        assertNotNull(result.getPatientId());
        
        // Verify patient was persisted to database
        Patient savedPatient = patientRepository.findById(result.getPatientId()).orElse(null);
        assertNotNull(savedPatient);
        assertEquals("John Doe", savedPatient.getPatientName());
        assertEquals(LocalDate.of(1990, 1, 1), savedPatient.getDateOfBirth());
        assertEquals("Male", savedPatient.getGender());
    }

    @Test
    @DisplayName("createPatient - With Ward and Hospital - Persists Relationships")
    void createPatient_WithWardAndHospital_PersistsRelationships() {
        // Arrange
        PatientRequest request = new PatientRequest();
        request.setPatientName("Jane Smith");
        request.setDateOfBirth(LocalDate.of(1985, 5, 15));
        request.setGender("Female");
        request.setWardId(testWard.getWardId());
        request.setHospitalId(testHospital.getHospitalId());

        // Act
        Patient result = patientService.createPatient(request);

        // Assert
        assertNotNull(result.getPatientId());
        
        // Verify relationships were persisted
        Patient savedPatient = patientRepository.findById(result.getPatientId()).orElse(null);
        assertNotNull(savedPatient);
        assertNotNull(savedPatient.getWard());
        assertEquals(testWard.getWardId(), savedPatient.getWard().getWardId());
        assertNotNull(savedPatient.getHospital());
        assertEquals(testHospital.getHospitalId(), savedPatient.getHospital().getHospitalId());
    }

    @Test
    @DisplayName("createPatient - Ward Does Not Belong to Hospital - Throws ValidationException")
    void createPatient_WardDoesNotBelongToHospital_ThrowsValidationException() {
        // Arrange - testWard2 is not associated with testHospital
        PatientRequest request = new PatientRequest();
        request.setPatientName("Test Patient");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setWardId(testWard2.getWardId());
        request.setHospitalId(testHospital.getHospitalId());

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            patientService.createPatient(request);
        });

        assertTrue(exception.getMessage().contains("does not belong to the selected hospital"));
        
        // Verify patient was NOT persisted
        assertEquals(0, patientRepository.count());
    }

    @Test
    @DisplayName("createPatient - With Diagnosis IDs - Persists ManyToMany Relationship")
    void createPatient_WithDiagnosisIds_PersistsManyToManyRelationship() {
        // Arrange
        PatientRequest request = new PatientRequest();
        request.setPatientName("Patient With Diagnosis");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setDiagnosisIds(Set.of(testDiagnosis.getDiagnosisId()));

        // Act
        Patient result = patientService.createPatient(request);

        // Assert
        assertNotNull(result.getPatientId());
        
        // Verify diagnosis relationship was persisted
        Patient savedPatient = patientRepository.findById(result.getPatientId()).orElse(null);
        assertNotNull(savedPatient);
        assertNotNull(savedPatient.getDiagnosis());
        assertEquals(1, savedPatient.getDiagnosis().size());
        assertTrue(savedPatient.getDiagnosis().contains(testDiagnosis));
    }

    @Test
    @DisplayName("createPatient - Invalid Diagnosis ID - Throws EntityNotFoundException")
    void createPatient_InvalidDiagnosisId_ThrowsEntityNotFoundException() {
        // Arrange
        PatientRequest request = new PatientRequest();
        request.setPatientName("Test Patient");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setDiagnosisIds(Set.of(UUID.randomUUID())); // Non-existent diagnosis

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            patientService.createPatient(request);
        });

        assertTrue(exception.getMessage().contains("Diagnosis not found"));
        
        // Verify patient was NOT persisted
        assertEquals(0, patientRepository.count());
    }

    @Test
    @DisplayName("getPatientById - Existing Patient - Returns Patient from Database")
    void getPatientById_ExistingPatient_ReturnsPatientFromDatabase() {
        // Arrange - Create and persist a patient
        Patient existingPatient = TestDataBuilder.createPatient("Existing Patient", 
                LocalDate.of(1980, 1, 1), "Male");
        existingPatient = entityManager.persistAndFlush(existingPatient);

        // Act
        Patient result = patientService.getPatientById(existingPatient.getPatientId());

        // Assert
        assertNotNull(result);
        assertEquals(existingPatient.getPatientId(), result.getPatientId());
        assertEquals("Existing Patient", result.getPatientName());
    }

    @Test
    @DisplayName("getPatientById - Non-Existent Patient - Throws EntityNotFoundException")
    void getPatientById_NonExistentPatient_ThrowsEntityNotFoundException() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            patientService.getPatientById(nonExistentId);
        });

        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    @DisplayName("updatePatient - Valid Request - Updates Patient in Database")
    void updatePatient_ValidRequest_UpdatesPatientInDatabase() {
        // Arrange - Create and persist a patient
        Patient existingPatient = TestDataBuilder.createPatient("Original Name", 
                LocalDate.of(1990, 1, 1), "Male");
        existingPatient = entityManager.persistAndFlush(existingPatient);

        PatientRequest updateRequest = new PatientRequest();
        updateRequest.setPatientName("Updated Name");
        updateRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        updateRequest.setGender("Female");

        // Act
        Patient result = patientService.updatePatient(existingPatient.getPatientId(), updateRequest);

        // Assert
        assertEquals("Updated Name", result.getPatientName());
        assertEquals("Female", result.getGender());
        
        // Verify update was persisted
        Patient updatedPatient = patientRepository.findById(existingPatient.getPatientId()).orElse(null);
        assertNotNull(updatedPatient);
        assertEquals("Updated Name", updatedPatient.getPatientName());
        assertEquals("Female", updatedPatient.getGender());
    }

    @Test
    @DisplayName("updatePatient - Update Ward and Hospital - Updates Relationships")
    void updatePatient_UpdateWardAndHospital_UpdatesRelationships() {
        // Arrange - Create patient with initial ward
        Patient existingPatient = TestDataBuilder.createPatient("Test Patient", 
                LocalDate.of(1990, 1, 1), "Male");
        existingPatient.setWard(testWard);
        existingPatient.setHospital(testHospital);
        existingPatient = entityManager.persistAndFlush(existingPatient);

        // Create another ward associated with hospital
        Ward newWard = TestDataBuilder.createWard(com.testing_exam_webapp.model.types.WardType.GENERAL_MEDICINE, 20);
        newWard = entityManager.persistAndFlush(newWard);
        TestDataBuilder.associateWardWithHospital(newWard, testHospital);
        entityManager.persistAndFlush(newWard);

        PatientRequest updateRequest = new PatientRequest();
        updateRequest.setPatientName("Test Patient");
        updateRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        updateRequest.setWardId(newWard.getWardId());
        updateRequest.setHospitalId(testHospital.getHospitalId());

        // Act
        Patient result = patientService.updatePatient(existingPatient.getPatientId(), updateRequest);

        // Assert
        assertNotNull(result.getWard());
        assertEquals(newWard.getWardId(), result.getWard().getWardId());
    }

    @Test
    @DisplayName("deletePatient - Existing Patient - Removes Patient from Database")
    void deletePatient_ExistingPatient_RemovesPatientFromDatabase() {
        // Arrange - Create and persist a patient
        Patient existingPatient = TestDataBuilder.createPatient("To Be Deleted", 
                LocalDate.of(1990, 1, 1), "Male");
        existingPatient = entityManager.persistAndFlush(existingPatient);
        UUID patientId = existingPatient.getPatientId();

        // Act
        patientService.deletePatient(patientId);

        // Assert - Verify patient was deleted
        assertFalse(patientRepository.existsById(patientId));
        Optional<Patient> deletedPatient = patientRepository.findById(patientId);
        assertTrue(deletedPatient.isEmpty());
    }

    @Test
    @DisplayName("getPatientsByWardId - Patients in Ward - Returns Correct Patients")
    void getPatientsByWardId_PatientsInWard_ReturnsCorrectPatients() {
        // Arrange - Create patients in different wards
        Patient patient1 = TestDataBuilder.createPatient("Patient 1", LocalDate.of(1990, 1, 1), "Male");
        patient1.setWard(testWard);
        final Patient savedPatient1 = entityManager.persistAndFlush(patient1);

        Patient patient2 = TestDataBuilder.createPatient("Patient 2", LocalDate.of(1985, 1, 1), "Female");
        patient2.setWard(testWard);
        final Patient savedPatient2 = entityManager.persistAndFlush(patient2);

        Patient patient3 = TestDataBuilder.createPatient("Patient 3", LocalDate.of(1995, 1, 1), "Male");
        patient3.setWard(testWard2);
        final Patient savedPatient3 = entityManager.persistAndFlush(patient3);

        // Act
        List<Patient> result = patientService.getPatientsByWardId(testWard.getWardId());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getPatientId().equals(savedPatient1.getPatientId())));
        assertTrue(result.stream().anyMatch(p -> p.getPatientId().equals(savedPatient2.getPatientId())));
        assertFalse(result.stream().anyMatch(p -> p.getPatientId().equals(savedPatient3.getPatientId())));
    }

    @Test
    @DisplayName("getPatientsByHospitalId - Patients in Hospital - Returns Correct Patients")
    void getPatientsByHospitalId_PatientsInHospital_ReturnsCorrectPatients() {
        // Arrange - Create patients in hospital
        Patient patient1 = TestDataBuilder.createPatient("Patient 1", LocalDate.of(1990, 1, 1), "Male");
        patient1.setWard(testWard);
        patient1.setHospital(testHospital);
        final Patient savedPatient1 = entityManager.persistAndFlush(patient1);

        Patient patient2 = TestDataBuilder.createPatient("Patient 2", LocalDate.of(1985, 1, 1), "Female");
        patient2.setWard(testWard);
        patient2.setHospital(testHospital);
        final Patient savedPatient2 = entityManager.persistAndFlush(patient2);

        // Act
        List<Patient> result = patientService.getPatientsByHospitalId(testHospital.getHospitalId());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getPatientId().equals(savedPatient1.getPatientId())));
        assertTrue(result.stream().anyMatch(p -> p.getPatientId().equals(savedPatient2.getPatientId())));
    }
}
