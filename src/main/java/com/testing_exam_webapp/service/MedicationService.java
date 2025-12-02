package com.testing_exam_webapp.service;

import com.testing_exam_webapp.dto.MedicationRequest;
import com.testing_exam_webapp.exception.EntityNotFoundException;
import com.testing_exam_webapp.model.mysql.Medication;
import com.testing_exam_webapp.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getMedications() {
        return medicationRepository.findAll();
    }

    public Medication getMedicationById(UUID id) {
        UUID medicationId = Objects.requireNonNull(id, "Medication ID cannot be null");
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found"));
    }

    public Medication createMedication(MedicationRequest request) {
        Medication medication = new Medication();
        medication.setMedicationId(UUID.randomUUID());
        medication.setMedicationName(request.getMedicationName());
        medication.setDosage(request.getDosage());

        return medicationRepository.save(medication);
    }

    public Medication updateMedication(UUID id, MedicationRequest request) {
        UUID medicationId = Objects.requireNonNull(id, "Medication ID cannot be null");
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found"));

        medication.setMedicationName(request.getMedicationName());
        medication.setDosage(request.getDosage());

        return medicationRepository.save(medication);
    }

    public void deleteMedication(UUID id) {
        UUID medicationId = Objects.requireNonNull(id, "Medication ID cannot be null");
        if (!medicationRepository.existsById(medicationId)) {
            throw new EntityNotFoundException("Medication not found");
        }
        medicationRepository.deleteById(medicationId);
    }
}

