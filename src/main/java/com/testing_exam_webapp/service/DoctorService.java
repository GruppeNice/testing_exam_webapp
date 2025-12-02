package com.testing_exam_webapp.service;

import com.testing_exam_webapp.dto.DoctorRequest;
import com.testing_exam_webapp.exception.EntityNotFoundException;
import com.testing_exam_webapp.model.mysql.Doctor;
import com.testing_exam_webapp.model.mysql.Ward;
import com.testing_exam_webapp.repository.DoctorRepository;
import com.testing_exam_webapp.repository.WardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final WardRepository wardRepository;

    public DoctorService(DoctorRepository doctorRepository, WardRepository wardRepository) {
        this.doctorRepository = doctorRepository;
        this.wardRepository = wardRepository;
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(UUID id) {
        UUID doctorId = Objects.requireNonNull(id, "Doctor ID cannot be null");
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
    }

    public Doctor createDoctor(DoctorRequest request) {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(UUID.randomUUID());
        doctor.setDoctorName(request.getDoctorName());
        doctor.setSpeciality(request.getSpeciality());

        UUID wardId = request.getWardId();
        if (wardId != null) {
            Ward ward = wardRepository.findById(wardId)
                    .orElseThrow(() -> new EntityNotFoundException("Ward not found"));
            doctor.setWard(ward);
        }

        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(UUID id, DoctorRequest request) {
        UUID doctorId = Objects.requireNonNull(id, "Doctor ID cannot be null");
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        doctor.setDoctorName(request.getDoctorName());
        doctor.setSpeciality(request.getSpeciality());

        UUID wardId = request.getWardId();
        if (wardId != null) {
            Ward ward = wardRepository.findById(wardId)
                    .orElseThrow(() -> new EntityNotFoundException("Ward not found"));
            doctor.setWard(ward);
        }

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(UUID id) {
        UUID doctorId = Objects.requireNonNull(id, "Doctor ID cannot be null");
        if (!doctorRepository.existsById(doctorId)) {
            throw new EntityNotFoundException("Doctor not found");
        }
        doctorRepository.deleteById(doctorId);
    }
}

