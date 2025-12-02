package com.example.hospital_db_backend.service;

import com.example.hospital_db_backend.dto.SurgeryRequest;
import com.example.hospital_db_backend.model.mysql.Doctor;
import com.example.hospital_db_backend.model.mysql.Patient;
import com.example.hospital_db_backend.model.mysql.Surgery;
import com.example.hospital_db_backend.exception.EntityNotFoundException;
import com.example.hospital_db_backend.repository.DoctorRepository;
import com.example.hospital_db_backend.repository.PatientRepository;
import com.example.hospital_db_backend.repository.SurgeryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SurgeryService {
    private final SurgeryRepository surgeryRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public SurgeryService(SurgeryRepository surgeryRepository,
                          PatientRepository patientRepository,
                          DoctorRepository doctorRepository) {
        this.surgeryRepository = surgeryRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<Surgery> getSurgeries() {
        return surgeryRepository.findAll();
    }

    public Surgery getSurgeryById(UUID id) {
        UUID surgeryId = Objects.requireNonNull(id, "Surgery ID cannot be null");
        return surgeryRepository.findById(surgeryId)
                .orElseThrow(() -> new EntityNotFoundException("Surgery not found"));
    }

    public Surgery createSurgery(SurgeryRequest request) {
        Surgery surgery = new Surgery();
        surgery.setSurgeryId(UUID.randomUUID());
        surgery.setSurgeryDate(request.getSurgeryDate());
        surgery.setDescription(request.getDescription());

        UUID patientId = request.getPatientId();
        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
            surgery.setPatient(patient);
        }

        UUID doctorId = request.getDoctorId();
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
            surgery.setDoctor(doctor);
        }

        return surgeryRepository.save(surgery);
    }

    public Surgery updateSurgery(UUID id, SurgeryRequest request) {
        UUID surgeryId = Objects.requireNonNull(id, "Surgery ID cannot be null");
        Surgery surgery = surgeryRepository.findById(surgeryId)
                .orElseThrow(() -> new EntityNotFoundException("Surgery not found"));

        surgery.setSurgeryDate(request.getSurgeryDate());
        surgery.setDescription(request.getDescription());

        UUID patientId = request.getPatientId();
        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
            surgery.setPatient(patient);
        }

        UUID doctorId = request.getDoctorId();
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
            surgery.setDoctor(doctor);
        }

        return surgeryRepository.save(surgery);
    }

    public void deleteSurgery(UUID id) {
        UUID surgeryId = Objects.requireNonNull(id, "Surgery ID cannot be null");
        if (!surgeryRepository.existsById(surgeryId)) {
            throw new EntityNotFoundException("Surgery not found");
        }
        surgeryRepository.deleteById(surgeryId);
    }
}

