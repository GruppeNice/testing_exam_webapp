package com.testing_exam_webapp.service;

import com.testing_exam_webapp.model.mysql.*;
import com.testing_exam_webapp.model.types.*;
import com.testing_exam_webapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BulkDataSeederService {

    private final HospitalRepository hospitalRepository;
    private final WardRepository wardRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final AppointmentRepository appointmentRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final MedicationRepository medicationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final SurgeryRepository surgeryRepository;

    // Danish cities for realistic data
    private static final String[] CITIES = {
        "København", "Aarhus", "Odense", "Aalborg", "Esbjerg", "Randers", "Kolding",
        "Horsens", "Vejle", "Roskilde", "Herning", "Helsingør", "Silkeborg", "Næstved",
        "Fredericia", "Viborg", "Køge", "Holstebro", "Taastrup", "Sønderborg"
    };

    // Common street names
    private static final String[] STREET_NAMES = {
        "Hovedgaden", "Kirkegade", "Strandvejen", "Parkvej", "Skovvej", "Bakkevej",
        "Møllevej", "Skovbakken", "Havnevej", "Markvej", "Vejlevej", "Stationsvej"
    };

    // First names for patients
    private static final String[] FIRST_NAMES = {
        "Lars", "Peter", "Michael", "Jens", "Anders", "Mads", "Henrik", "Thomas",
        "Anna", "Maria", "Karen", "Lise", "Mette", "Susanne", "Camilla", "Julie",
        "Emma", "Sofia", "Ida", "Freja", "Alberte", "Ella", "Olivia", "Clara"
    };

    // Last names
    private static final String[] LAST_NAMES = {
        "Nielsen", "Jensen", "Hansen", "Pedersen", "Andersen", "Christensen",
        "Larsen", "Sørensen", "Rasmussen", "Jørgensen", "Petersen", "Madsen",
        "Kristensen", "Olsen", "Thomsen", "Christiansen", "Poulsen", "Johansen"
    };

    public BulkDataSeederService(
            HospitalRepository hospitalRepository,
            WardRepository wardRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            NurseRepository nurseRepository,
            AppointmentRepository appointmentRepository,
            DiagnosisRepository diagnosisRepository,
            MedicationRepository medicationRepository,
            PrescriptionRepository prescriptionRepository,
            SurgeryRepository surgeryRepository) {
        this.hospitalRepository = hospitalRepository;
        this.wardRepository = wardRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
        this.appointmentRepository = appointmentRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.medicationRepository = medicationRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.surgeryRepository = surgeryRepository;
    }

    @Transactional
    public Map<String, Integer> seedBulkData(
            int hospitalCount,
            int patientCount,
            int doctorCount,
            int nurseCount,
            int appointmentCount) {

        Map<String, Integer> results = new HashMap<>();

        // Generate hospitals and wards
        List<Hospital> hospitals = generateHospitals(hospitalCount);
        results.put("hospitals", hospitals.size());

        List<Ward> allWards = new ArrayList<>();
        for (Hospital hospital : hospitals) {
            List<Ward> wards = generateWardsForHospital(hospital);
            allWards.addAll(wards);
        }
        results.put("wards", allWards.size());

        // Generate doctors and nurses (distributed across wards)
        List<Doctor> doctors = generateDoctors(doctorCount, allWards);
        results.put("doctors", doctors.size());

        List<Nurse> nurses = generateNurses(nurseCount, allWards);
        results.put("nurses", nurses.size());

        // Generate patients (distributed across hospitals and wards)
        List<Patient> patients = generatePatients(patientCount, hospitals, allWards);
        results.put("patients", patients.size());

        // Generate medications
        List<Medication> medications = generateMedications();
        medicationRepository.saveAll(medications);
        results.put("medications", medications.size());

        // Generate diagnoses
        List<Diagnosis> diagnoses = generateDiagnoses(patients.size() / 2, doctors);
        results.put("diagnoses", diagnoses.size());

        // Generate appointments
        List<Appointment> appointments = generateAppointments(
                appointmentCount, patients, doctors, nurses);
        results.put("appointments", appointments.size());

        // Generate prescriptions
        List<Prescription> prescriptions = generatePrescriptions(
                patients.size() / 3, patients, doctors, medications);
        results.put("prescriptions", prescriptions.size());

        // Generate surgeries
        List<Surgery> surgeries = generateSurgeries(
                patients.size() / 10, patients, doctors);
        results.put("surgeries", surgeries.size());

        return results;
    }

    private List<Hospital> generateHospitals(int count) {
        List<Hospital> hospitals = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();

        for (int i = 0; i < count; i++) {
            Hospital hospital = new Hospital();
            hospital.setHospitalId(UUID.randomUUID());
            
            // Generate unique hospital name
            String city = CITIES[ThreadLocalRandom.current().nextInt(CITIES.length)];
            String hospitalName;
            int attempts = 0;
            do {
                hospitalName = city + " " + getHospitalType() + " Hospital";
                attempts++;
            } while (usedNames.contains(hospitalName) && attempts < 100);
            usedNames.add(hospitalName);
            
            hospital.setHospitalName(hospitalName);
            hospital.setCity(city);
            hospital.setAddress(
                STREET_NAMES[ThreadLocalRandom.current().nextInt(STREET_NAMES.length)] + " " +
                (ThreadLocalRandom.current().nextInt(200) + 1)
            );
            
            hospitals.add(hospital);
        }

        hospitals = hospitalRepository.saveAll(hospitals);
        
        // Generate and associate wards
        for (Hospital hospital : hospitals) {
            List<Ward> wards = generateWardsForHospital(hospital);
            Set<Ward> wardSet = new HashSet<>(wards);
            hospital.setWards(wardSet);
        }
        
        hospitalRepository.saveAll(hospitals);
        return hospitals;
    }

    private String getHospitalType() {
        String[] types = {"General", "Regional", "University", "Municipal", "Specialized"};
        return types[ThreadLocalRandom.current().nextInt(types.length)];
    }

    private List<Ward> generateWardsForHospital(Hospital hospital) {
        int wardCount = ThreadLocalRandom.current().nextInt(2, 5); // 2-4 wards per hospital
        List<Ward> wards = new ArrayList<>();
        WardType[] wardTypes = WardType.values();

        for (int i = 0; i < wardCount; i++) {
            Ward ward = new Ward();
            ward.setWardId(UUID.randomUUID());
            ward.setType(wardTypes[ThreadLocalRandom.current().nextInt(wardTypes.length)]);
            ward.setMaxCapacity(ThreadLocalRandom.current().nextInt(15, 50));
            
            // Associate with hospital
            Set<Hospital> hospitals = new HashSet<>();
            hospitals.add(hospital);
            ward.setHospitals(hospitals);
            
            wards.add(ward);
        }

        return wardRepository.saveAll(wards);
    }

    private List<Doctor> generateDoctors(int count, List<Ward> wards) {
        List<Doctor> doctors = new ArrayList<>();
        DoctorSpecialityType[] specialities = DoctorSpecialityType.values();

        for (int i = 0; i < count; i++) {
            Doctor doctor = new Doctor();
            doctor.setDoctorId(UUID.randomUUID());
            doctor.setDoctorName("Dr. " + generateName());
            doctor.setSpeciality(specialities[ThreadLocalRandom.current().nextInt(specialities.length)]);
            
            // Assign to random ward and hospital
            Ward ward = wards.get(ThreadLocalRandom.current().nextInt(wards.size()));
            doctor.setWard(ward);
            doctor.setHospital(ward.getHospitals().iterator().next());
            
            doctors.add(doctor);
        }

        return doctorRepository.saveAll(doctors);
    }

    private List<Nurse> generateNurses(int count, List<Ward> wards) {
        List<Nurse> nurses = new ArrayList<>();
        NurseSpecialityType[] specialities = NurseSpecialityType.values();

        for (int i = 0; i < count; i++) {
            Nurse nurse = new Nurse();
            nurse.setNurseId(UUID.randomUUID());
            nurse.setNurseName("Nurse " + generateName());
            nurse.setSpeciality(specialities[ThreadLocalRandom.current().nextInt(specialities.length)]);
            
            // Assign to random ward and hospital
            Ward ward = wards.get(ThreadLocalRandom.current().nextInt(wards.size()));
            nurse.setWard(ward);
            nurse.setHospital(ward.getHospitals().iterator().next());
            
            nurses.add(nurse);
        }

        return nurseRepository.saveAll(nurses);
    }

    private List<Patient> generatePatients(int count, List<Hospital> hospitals, List<Ward> wards) {
        List<Patient> patients = new ArrayList<>();
        String[] genders = {"Male", "Female", "Other"};

        for (int i = 0; i < count; i++) {
            Patient patient = new Patient();
            patient.setPatientId(UUID.randomUUID());
            patient.setPatientName(generateName());
            patient.setGender(genders[ThreadLocalRandom.current().nextInt(genders.length)]);
            
            // Random date of birth (between 1920 and 2010)
            int year = ThreadLocalRandom.current().nextInt(1920, 2011);
            int month = ThreadLocalRandom.current().nextInt(1, 13);
            int day = ThreadLocalRandom.current().nextInt(1, 29);
            patient.setDateOfBirth(LocalDate.of(year, month, day));
            
            // Assign to random hospital and ward
            Hospital hospital = hospitals.get(ThreadLocalRandom.current().nextInt(hospitals.size()));
            patient.setHospital(hospital);
            
            // Get a ward from this hospital
            List<Ward> hospitalWards = wards.stream()
                .filter(w -> w.getHospitals().contains(hospital))
                .toList();
            
            if (!hospitalWards.isEmpty()) {
                Ward ward = hospitalWards.get(ThreadLocalRandom.current().nextInt(hospitalWards.size()));
                patient.setWard(ward);
            }
            
            patients.add(patient);
        }

        return patientRepository.saveAll(patients);
    }

    private List<Medication> generateMedications() {
        String[] medicationNames = {
            "Ibuprofen", "Paracetamol", "Amoxicillin", "Aspirin", "Metformin",
            "Atorvastatin", "Omeprazole", "Amlodipine", "Levothyroxine", "Albuterol",
            "Metoprolol", "Losartan", "Gabapentin", "Sertraline", "Tramadol"
        };
        
        String[] dosages = {"10mg", "20mg", "50mg", "100mg", "200mg", "500mg", "1000mg"};

        List<Medication> medications = new ArrayList<>();
        for (String name : medicationNames) {
            Medication med = new Medication();
            med.setMedicationId(UUID.randomUUID());
            med.setMedicationName(name);
            med.setDosage(dosages[ThreadLocalRandom.current().nextInt(dosages.length)]);
            medications.add(med);
        }

        return medications;
    }

    private List<Diagnosis> generateDiagnoses(int count, List<Doctor> doctors) {
        String[] diagnoses = {
            "Hypertension", "Diabetes Type 2", "Common Cold", "Bronchitis", "Pneumonia",
            "Asthma", "Arthritis", "Migraine", "Anxiety", "Depression", "Insomnia",
            "Gastritis", "Urinary Tract Infection", "Sinusitis", "Allergic Rhinitis"
        };

        List<Diagnosis> diagnosisList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setDiagnosisId(UUID.randomUUID());
            diagnosis.setDescription(diagnoses[ThreadLocalRandom.current().nextInt(diagnoses.length)]);
            diagnosis.setDiagnosisDate(LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(365)));
            diagnosis.setDoctor(doctors.get(ThreadLocalRandom.current().nextInt(doctors.size())));
            diagnosisList.add(diagnosis);
        }

        return diagnosisRepository.saveAll(diagnosisList);
    }

    private List<Appointment> generateAppointments(
            int count, List<Patient> patients, List<Doctor> doctors, List<Nurse> nurses) {
        List<Appointment> appointments = new ArrayList<>();
        AppointmentStatusType[] statuses = AppointmentStatusType.values();
        String[] reasons = {
            "Routine checkup", "Follow-up visit", "Emergency consultation",
            "Surgery consultation", "Lab results review", "Medication review",
            "Physical examination", "Specialist referral"
        };

        for (int i = 0; i < count; i++) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(UUID.randomUUID());
            appointment.setPatient(patients.get(ThreadLocalRandom.current().nextInt(patients.size())));
            appointment.setDoctor(doctors.get(ThreadLocalRandom.current().nextInt(doctors.size())));
            appointment.setNurse(nurses.get(ThreadLocalRandom.current().nextInt(nurses.size())));
            appointment.setStatus(statuses[ThreadLocalRandom.current().nextInt(statuses.length)]);
            appointment.setReason(reasons[ThreadLocalRandom.current().nextInt(reasons.length)]);
            
            // Random date between 6 months ago and 6 months from now
            int daysOffset = ThreadLocalRandom.current().nextInt(-180, 180);
            appointment.setAppointmentDate(LocalDate.now().plusDays(daysOffset));
            
            appointments.add(appointment);
        }

        return appointmentRepository.saveAll(appointments);
    }

    private List<Prescription> generatePrescriptions(
            int count, List<Patient> patients, List<Doctor> doctors, List<Medication> medications) {
        List<Prescription> prescriptions = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Prescription prescription = new Prescription();
            prescription.setPrescriptionId(UUID.randomUUID());
            prescription.setPatient(patients.get(ThreadLocalRandom.current().nextInt(patients.size())));
            prescription.setDoctor(doctors.get(ThreadLocalRandom.current().nextInt(doctors.size())));
            prescription.setMedication(medications.get(ThreadLocalRandom.current().nextInt(medications.size())));
            
            LocalDate startDate = LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(90));
            prescription.setStartDate(startDate);
            prescription.setEndDate(startDate.plusDays(ThreadLocalRandom.current().nextInt(7, 30)));
            
            prescriptions.add(prescription);
        }

        return prescriptionRepository.saveAll(prescriptions);
    }

    private List<Surgery> generateSurgeries(int count, List<Patient> patients, List<Doctor> doctors) {
        String[] surgeries = {
            "Appendectomy", "Gallbladder removal", "Hernia repair", "Knee arthroscopy",
            "Cataract surgery", "Tonsillectomy", "Cholecystectomy", "Hysterectomy",
            "Prostatectomy", "Mastectomy", "Coronary bypass", "Hip replacement"
        };

        List<Surgery> surgeryList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Surgery surgery = new Surgery();
            surgery.setSurgeryId(UUID.randomUUID());
            surgery.setPatient(patients.get(ThreadLocalRandom.current().nextInt(patients.size())));
            surgery.setDoctor(doctors.get(ThreadLocalRandom.current().nextInt(doctors.size())));
            surgery.setDescription(surgeries[ThreadLocalRandom.current().nextInt(surgeries.length)]);
            
            // Random date between 1 year ago and 1 year from now
            int daysOffset = ThreadLocalRandom.current().nextInt(-365, 365);
            surgery.setSurgeryDate(LocalDate.now().plusDays(daysOffset));
            
            surgeryList.add(surgery);
        }

        return surgeryRepository.saveAll(surgeryList);
    }

    private String generateName() {
        String firstName = FIRST_NAMES[ThreadLocalRandom.current().nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[ThreadLocalRandom.current().nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }
}

