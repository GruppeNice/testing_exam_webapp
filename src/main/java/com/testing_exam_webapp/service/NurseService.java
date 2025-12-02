package com.example.hospital_db_backend.service;

import com.example.hospital_db_backend.dto.NurseRequest;
import com.example.hospital_db_backend.model.mysql.Nurse;
import com.example.hospital_db_backend.model.mysql.Ward;
import com.example.hospital_db_backend.exception.EntityNotFoundException;
import com.example.hospital_db_backend.repository.NurseRepository;
import com.example.hospital_db_backend.repository.WardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class NurseService {
    private final NurseRepository nurseRepository;
    private final WardRepository wardRepository;

    public NurseService(NurseRepository nurseRepository, WardRepository wardRepository) {
        this.nurseRepository = nurseRepository;
        this.wardRepository = wardRepository;
    }

    public List<Nurse> getNurses() {
        return nurseRepository.findAll();
    }

    public Nurse getNurseById(UUID id) {
        UUID nurseId = Objects.requireNonNull(id, "Nurse ID cannot be null");
        return nurseRepository.findById(nurseId)
                .orElseThrow(() -> new EntityNotFoundException("Nurse not found"));
    }

    public Nurse createNurse(NurseRequest request) {
        Nurse nurse = new Nurse();
        nurse.setNurseId(UUID.randomUUID());
        nurse.setNurseName(request.getNurseName());
        nurse.setSpeciality(request.getSpeciality());

        UUID wardId = request.getWardId();
        if (wardId != null) {
            Ward ward = wardRepository.findById(wardId)
                    .orElseThrow(() -> new EntityNotFoundException("Ward not found"));
            nurse.setWard(ward);
        }

        return nurseRepository.save(nurse);
    }

    public Nurse updateNurse(UUID id, NurseRequest request) {
        UUID nurseId = Objects.requireNonNull(id, "Nurse ID cannot be null");
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new EntityNotFoundException("Nurse not found"));

        nurse.setNurseName(request.getNurseName());
        nurse.setSpeciality(request.getSpeciality());

        UUID wardId = request.getWardId();
        if (wardId != null) {
            Ward ward = wardRepository.findById(wardId)
                    .orElseThrow(() -> new EntityNotFoundException("Ward not found"));
            nurse.setWard(ward);
        }

        return nurseRepository.save(nurse);
    }

    public void deleteNurse(UUID id) {
        UUID nurseId = Objects.requireNonNull(id, "Nurse ID cannot be null");
        if (!nurseRepository.existsById(nurseId)) {
            throw new EntityNotFoundException("Nurse not found");
        }
        nurseRepository.deleteById(nurseId);
    }
}

