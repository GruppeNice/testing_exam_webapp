package com.example.hospital_db_backend.service;

import com.example.hospital_db_backend.dto.PrescriptionRequest;
import com.example.hospital_db_backend.model.mysql.Doctor;
import com.example.hospital_db_backend.model.mysql.Medication;
import com.example.hospital_db_backend.model.mysql.Patient;
import com.example.hospital_db_backend.model.mysql.Prescription;
import com.example.hospital_db_backend.exception.EntityNotFoundException;
import com.example.hospital_db_backend.repository.DoctorRepository;
import com.example.hospital_db_backend.repository.MedicationRepository;
import com.example.hospital_db_backend.repository.PatientRepository;
import com.example.hospital_db_backend.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicationRepository medicationRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               PatientRepository patientRepository,
                               DoctorRepository doctorRepository,
                               MedicationRepository medicationRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.medicationRepository = medicationRepository;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription getPrescriptionById(UUID id) {
        UUID prescriptionId = Objects.requireNonNull(id, "Prescription ID cannot be null");
        return prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found"));
    }

    public Prescription createPrescription(PrescriptionRequest request) {
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(UUID.randomUUID());
        prescription.setStartDate(request.getStartDate());
        prescription.setEndDate(request.getEndDate());

        UUID patientId = request.getPatientId();
        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
            prescription.setPatient(patient);
        }

        UUID doctorId = request.getDoctorId();
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
            prescription.setDoctor(doctor);
        }

        UUID medicationId = request.getMedicationId();
        if (medicationId != null) {
            Medication medication = medicationRepository.findById(medicationId)
                    .orElseThrow(() -> new EntityNotFoundException("Medication not found"));
            prescription.setMedication(medication);
        }

        return prescriptionRepository.save(prescription);
    }

    public Prescription updatePrescription(UUID id, PrescriptionRequest request) {
        UUID prescriptionId = Objects.requireNonNull(id, "Prescription ID cannot be null");
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found"));

        prescription.setStartDate(request.getStartDate());
        prescription.setEndDate(request.getEndDate());

        UUID patientId = request.getPatientId();
        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
            prescription.setPatient(patient);
        }

        UUID doctorId = request.getDoctorId();
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
            prescription.setDoctor(doctor);
        }

        UUID medicationId = request.getMedicationId();
        if (medicationId != null) {
            Medication medication = medicationRepository.findById(medicationId)
                    .orElseThrow(() -> new EntityNotFoundException("Medication not found"));
            prescription.setMedication(medication);
        }

        return prescriptionRepository.save(prescription);
    }

    public void deletePrescription(UUID id) {
        UUID prescriptionId = Objects.requireNonNull(id, "Prescription ID cannot be null");
        if (!prescriptionRepository.existsById(prescriptionId)) {
            throw new EntityNotFoundException("Prescription not found");
        }
        prescriptionRepository.deleteById(prescriptionId);
    }
}

