package com.example.hospital_db_backend.service;

import com.example.hospital_db_backend.dto.DiagnosisRequest;
import com.example.hospital_db_backend.model.mysql.Diagnosis;
import com.example.hospital_db_backend.model.mysql.Doctor;
import com.example.hospital_db_backend.exception.EntityNotFoundException;
import com.example.hospital_db_backend.repository.DiagnosisRepository;
import com.example.hospital_db_backend.repository.DoctorRepository;
import com.example.hospital_db_backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;
    private final DoctorRepository doctorRepository;

    public DiagnosisService(DiagnosisRepository diagnosisRepository,
                            DoctorRepository doctorRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<Diagnosis> getDiagnoses() {
        return diagnosisRepository.findAll();
    }

    public Diagnosis getDiagnosisById(UUID id) {
        UUID diagnosisId = Objects.requireNonNull(id, "Diagnosis ID cannot be null");
        return diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new EntityNotFoundException("Diagnosis not found"));
    }

    public Diagnosis createDiagnosis(DiagnosisRequest request) {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDiagnosisId(UUID.randomUUID());
        diagnosis.setDiagnosisDate(request.getDiagnosisDate());
        diagnosis.setDescription(request.getDescription());

        UUID doctorId = request.getDoctorId();
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
            diagnosis.setDoctor(doctor);
        }

        return diagnosisRepository.save(diagnosis);
    }

    public Diagnosis updateDiagnosis(UUID id, DiagnosisRequest request) {
        UUID diagnosisId = Objects.requireNonNull(id, "Diagnosis ID cannot be null");
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new EntityNotFoundException("Diagnosis not found"));

        diagnosis.setDiagnosisDate(request.getDiagnosisDate());
        diagnosis.setDescription(request.getDescription());

        UUID doctorId = request.getDoctorId();
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
            diagnosis.setDoctor(doctor);
        }

        return diagnosisRepository.save(diagnosis);
    }

    public void deleteDiagnosis(UUID id) {
        UUID diagnosisId = Objects.requireNonNull(id, "Diagnosis ID cannot be null");
        if (!diagnosisRepository.existsById(diagnosisId)) {
            throw new EntityNotFoundException("Diagnosis not found");
        }
        diagnosisRepository.deleteById(diagnosisId);
    }
}

