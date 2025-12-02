package com.testing_exam_webapp.service;

import com.testing_exam_webapp.dto.PrescriptionRequest;
import com.testing_exam_webapp.exception.EntityNotFoundException;
import com.testing_exam_webapp.model.mysql.Doctor;
import com.testing_exam_webapp.model.mysql.Medication;
import com.testing_exam_webapp.model.mysql.Patient;
import com.testing_exam_webapp.model.mysql.Prescription;
import com.testing_exam_webapp.repository.DoctorRepository;
import com.testing_exam_webapp.repository.MedicationRepository;
import com.testing_exam_webapp.repository.PatientRepository;
import com.testing_exam_webapp.repository.PrescriptionRepository;
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

