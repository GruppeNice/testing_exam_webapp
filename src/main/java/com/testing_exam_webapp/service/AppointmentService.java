package com.example.hospital_db_backend.service;

import com.example.hospital_db_backend.dto.AppointmentRequest;
import com.example.hospital_db_backend.model.mysql.Appointment;
import com.example.hospital_db_backend.model.mysql.Doctor;
import com.example.hospital_db_backend.model.mysql.Nurse;
import com.example.hospital_db_backend.model.mysql.Patient;
import com.example.hospital_db_backend.exception.EntityNotFoundException;
import com.example.hospital_db_backend.repository.AppointmentRepository;
import com.example.hospital_db_backend.repository.DoctorRepository;
import com.example.hospital_db_backend.repository.NurseRepository;
import com.example.hospital_db_backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              NurseRepository nurseRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
    }

    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(UUID id) {
        UUID appointmentId = Objects.requireNonNull(id, "Appointment ID cannot be null");
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
    }

    public Appointment createAppointment(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(UUID.randomUUID());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setReason(request.getReason());
        appointment.setStatus(request.getStatus());

        UUID patientId = request.getPatientId();
        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
            appointment.setPatient(patient);
        }

        UUID doctorId = request.getDoctorId();
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
            appointment.setDoctor(doctor);
        }

        UUID nurseId = request.getNurseId();
        if (nurseId != null) {
            Nurse nurse = nurseRepository.findById(nurseId)
                    .orElseThrow(() -> new EntityNotFoundException("Nurse not found"));
            appointment.setNurse(nurse);
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(UUID id, AppointmentRequest request) {
        UUID appointmentId = Objects.requireNonNull(id, "Appointment ID cannot be null");
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setReason(request.getReason());
        appointment.setStatus(request.getStatus());

        UUID patientId = request.getPatientId();
        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
            appointment.setPatient(patient);
        }

        UUID doctorId = request.getDoctorId();
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
            appointment.setDoctor(doctor);
        }

        UUID nurseId = request.getNurseId();
        if (nurseId != null) {
            Nurse nurse = nurseRepository.findById(nurseId)
                    .orElseThrow(() -> new EntityNotFoundException("Nurse not found"));
            appointment.setNurse(nurse);
        }

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(UUID id) {
        UUID appointmentId = Objects.requireNonNull(id, "Appointment ID cannot be null");
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new EntityNotFoundException("Appointment not found");
        }
        appointmentRepository.deleteById(appointmentId);
    }
}
