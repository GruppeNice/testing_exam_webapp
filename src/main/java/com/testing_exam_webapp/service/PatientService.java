package com.example.hospital_db_backend.service;

import com.example.hospital_db_backend.dto.PatientRequest;
import com.example.hospital_db_backend.model.mysql.Diagnosis;
import com.example.hospital_db_backend.model.mysql.Hospital;
import com.example.hospital_db_backend.model.mysql.Patient;
import com.example.hospital_db_backend.model.mysql.Ward;
import com.example.hospital_db_backend.exception.EntityNotFoundException;
import com.example.hospital_db_backend.repository.DiagnosisRepository;
import com.example.hospital_db_backend.repository.HospitalRepository;
import com.example.hospital_db_backend.repository.PatientRepository;
import com.example.hospital_db_backend.repository.WardRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final WardRepository wardRepository;
    private final HospitalRepository hospitalRepository;
    private final DiagnosisRepository diagnosisRepository;

    public PatientService(PatientRepository patientRepository,
                          WardRepository wardRepository,
                          HospitalRepository hospitalRepository, DiagnosisRepository diagnosisRepository) {
        this.patientRepository = patientRepository;
        this.wardRepository = wardRepository;
        this.hospitalRepository = hospitalRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(UUID id) {
        UUID patientId = Objects.requireNonNull(id, "Patient ID cannot be null");
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
    }

    public Patient createPatient(PatientRequest request) {
        Patient patient = new Patient();
        patient.setPatientId(UUID.randomUUID());
        patient.setPatientName(request.getPatientName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());

        if (request.getDiagnosisIds() != null) {
            Set<Diagnosis> diagnoses = new HashSet<>();
            for (UUID diagnosisId : request.getDiagnosisIds()) {
                UUID diagnosisUuid = Objects.requireNonNull(diagnosisId, "Diagnosis ID cannot be null");
                Diagnosis diagnosis = diagnosisRepository.findById(diagnosisUuid)
                        .orElseThrow(() -> new EntityNotFoundException("Diagnosis not found: " + diagnosisId));
                diagnoses.add(diagnosis);
            }
            patient.setDiagnosis(diagnoses);
        }

        UUID wardId = request.getWardId();
        if (wardId != null) {
            Ward ward = wardRepository.findById(wardId)
                    .orElseThrow(() -> new EntityNotFoundException("Ward not found"));
            patient.setWard(ward);
        }

        UUID hospitalId = request.getHospitalId();
        if (hospitalId != null) {
            Hospital hospital = hospitalRepository.findById(hospitalId)
                    .orElseThrow(() -> new EntityNotFoundException("Hospital not found"));
            patient.setHospital(hospital);
        }

        return patientRepository.save(patient);
    }

    public Patient updatePatient(UUID id, PatientRequest request) {
        UUID patientId = Objects.requireNonNull(id, "Patient ID cannot be null");
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        patient.setPatientName(request.getPatientName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());

        if (request.getDiagnosisIds() != null) {
            Set<Diagnosis> patients = new HashSet<>();
            for (UUID diagnosisId : request.getDiagnosisIds()) {
                UUID diagnosisUuid = Objects.requireNonNull(diagnosisId, "Diagnosis ID cannot be null");
                Diagnosis diagnosis = diagnosisRepository.findById(diagnosisUuid)
                        .orElseThrow(() -> new EntityNotFoundException("Diagnosis not found: " + diagnosisId));
                patients.add(diagnosis);
            }
            patient.setDiagnosis(patients);
        }

        UUID wardId = request.getWardId();
        if (wardId != null) {
            Ward ward = wardRepository.findById(wardId)
                    .orElseThrow(() -> new EntityNotFoundException("Ward not found"));
            patient.setWard(ward);
        }

        UUID hospitalId = request.getHospitalId();
        if (hospitalId != null) {
            Hospital hospital = hospitalRepository.findById(hospitalId)
                    .orElseThrow(() -> new EntityNotFoundException("Hospital not found"));
            patient.setHospital(hospital);
        }

        return patientRepository.save(patient);
    }

    public void deletePatient(UUID id) {
        UUID patientId = Objects.requireNonNull(id, "Patient ID cannot be null");
        if (!patientRepository.existsById(patientId)) {
            throw new EntityNotFoundException("Patient not found");
        }
        patientRepository.deleteById(patientId);
    }
}

