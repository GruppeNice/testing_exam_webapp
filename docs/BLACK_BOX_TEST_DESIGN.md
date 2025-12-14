# Black-Box Test Design Analysis

<div align="center">

**Testing Exam Web Application**  
*Black-Box Testing Documentation*

---

</div>

## Table of Contents

1. [Introduction](#-introduction)
2. [Equivalence Partitioning](#-equivalence-partitioning)
3. [Boundary Value Analysis](#-boundary-value-analysis)
4. [Decision Tables](#-decision-tables)
5. [State Transition Analysis](#-state-transition-analysis)
6. [Test Case Mapping](#-test-case-mapping)

---

## Introduction

This document outlines the black-box testing techniques used in the Testing Exam Web Application. Black-box testing focuses on functionality without knowledge of internal implementation.

### Testing Techniques

| Technique | Description |
|-----------|-------------|
| **Equivalence Partitioning** | Dividing input data into equivalent classes |
| **Boundary Value Analysis** | Testing values at the boundaries of input domains |
| **Decision Tables** | Systematic testing of all condition combinations |
| **State Transition Testing** | Verifying correct state transitions |

---

## Equivalence Partitioning

Equivalence partitioning divides input data into equivalent classes that should produce similar outputs. We test one representative value from each partition.

### Example 1: Login Authentication

**Function**: `login(LoginRequest request)`  
**Location**: `src/main/java/com/testing_exam_webapp/service/AuthService.java:30`

#### Input Partitions

| Input Field | Valid Partitions | Invalid Partitions | Test Values |
|------------|---------------------|----------------------|-------------|
| `username` | Existing username | Non-existent username, null, empty | `"admin"`, `"nonexistent"`, `null` |
| `password` | Correct password | Incorrect password, null, empty | `"correct"`, `"wrong"`, `null` |

#### Test Cases

| Test Method | Description | Location |
|-------------|-------------|----------|
| `login_ValidCredentials_ReturnsToken()` | Valid username and password | `AuthServiceTest.java:66` |
| `login_InvalidUsername_ThrowsUnauthorizedException()` | Invalid username | `AuthServiceTest.java:84` |
| `login_InvalidPassword_ThrowsUnauthorizedException()` | Invalid password | `AuthServiceTest.java:98` |

### Example 2: Appointment Status Types

**Function**: `createAppointment(AppointmentRequest request)`  
**Location**: `src/main/java/com/testing_exam_webapp/service/AppointmentService.java:48`

#### Input Partitions

| Input Field | Valid Partitions | Test Values |
|------------|------------------|-------------|
| `status` | SCHEDULED, COMPLETED, CANCELLED | All enum values tested |

#### Test Case

| Test Method | Description | Location |
|-------------|-------------|----------|
| `createAppointment_AllStatusTypes_CreatesAppointment()` | Tests all status enum values | `AppointmentServiceTest.java:177` |

---

## Boundary Value Analysis

Boundary value analysis tests values at the boundaries of input domains, including minimum, maximum, and just inside/outside boundaries.

### Example 1: Username Length Validation

**Function**: `register(RegisterRequest request)`  
**Location**: `src/main/java/com/testing_exam_webapp/service/AuthService.java:42`

**Boundary**: `@Size(min = 3, max = 50)` - Username must be between 3 and 50 characters

#### Boundary Test Cases

| Test Value | Boundary Type | Expected Result | Test Method |
|-----------|--------------|-----------------|-------------|
| **2 characters** | Invalid boundary | Validation error | Handled by `@Size` annotation |
| **3 characters** | Valid boundary (minimum) | Success | `register_UsernameLengthBoundaryValues_CreatesUser()` |
| **50 characters** | Valid boundary (maximum) | Success | `register_UsernameLengthBoundaryValues_CreatesUser()` |
| **51 characters** | Invalid boundary | Validation error | Handled by `@Size` annotation |

**Test Implementation**: `AuthServiceTest.java:164`

### Example 2: Ward Capacity Validation

**Function**: `createWard(WardRequest request)`  
**Location**: `src/main/java/com/testing_exam_webapp/service/WardService.java:32`

**Boundary**: `@Min(value = 1)` - Capacity must be at least 1

#### Boundary Test Cases

| Test Value | Boundary Type | Expected Result | Test Method |
|-----------|--------------|-----------------|-------------|
| **-1** | Invalid boundary | Validation error | `createWard_MaxCapacityBoundaryValues_CreatesWard()` |
| **0** | Invalid boundary | Validation error | `createWard_MaxCapacityBoundaryValues_CreatesWard()` |
| **1** | Valid boundary (minimum) | Success | `createWard_MaxCapacityBoundaryValues_CreatesWard()` |
| **2** | Just above boundary | Success | `createWard_MaxCapacityBoundaryValues_CreatesWard()` |
| **1000** | Large value | Success | `createWard_MaxCapacityBoundaryValues_CreatesWard()` |

**Test Implementation**: `WardServiceTest.java:100`

---

## Decision Tables

Decision tables systematically test all combinations of conditions and their resulting actions. This is useful for complex business logic with multiple conditions.

### Example: Ward-Hospital Validation in `createPatient()`

**Function**: `createPatient(PatientRequest request)`  
**Location**: `src/main/java/com/testing_exam_webapp/service/PatientService.java:44-98`

**Business Rule**: When both ward and hospital are provided, the ward must belong to the selected hospital.

#### Decision Table

| Condition | Case 1 | Case 2 | Case 3 | Case 4 | Case 5 | Case 6 | Case 7 |
|-----------|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
| **Ward ID provided?** | N | N | Y | Y | Y | Y | N |
| **Hospital ID provided?** | N | Y | N | Y | Y | Y | Y |
| **Ward exists?** | - | - | Y | Y | Y | N | - |
| **Hospital exists?** | - | Y | - | Y | Y | - | N |
| **Ward belongs to hospital?** | - | - | - | Y | N | - | - |
| **Action** | Success | Success | Success | Success | ValidationException | EntityNotFoundException | EntityNotFoundException |

#### Test Case Mapping

| Case | Test Method | Location |
|------|-------------|----------|
| **Case 1** | `createPatient_BothWardAndHospitalNull_CreatesPatient()` | `PatientServiceTest.java:272` |
| **Case 2** | `createPatient_WardNullHospitalProvided_CreatesPatient()` | `PatientServiceTest.java:218` |
| **Case 3** | `createPatient_WardProvidedHospitalNull_CreatesPatient()` | `PatientServiceTest.java:245` |
| **Case 4** | `createPatient_WardAndHospitalBothProvidedAndValid_CreatesPatient()` | `PatientServiceTest.java:186` |
| **Case 5** | `createPatient_WardDoesNotBelongToHospital_ThrowsValidationException()` | `PatientServiceTest.java:296` |
| **Case 6** | `createPatient_WardNotFound_ThrowsEntityNotFoundException()` | `PatientServiceTest.java:320` |
| **Case 7** | `createPatient_HospitalNotFound_ThrowsEntityNotFoundException()` | `PatientServiceTest.java:340` |

---

## State Transition Analysis

State transition testing verifies that the system correctly handles transitions between different states.

### Example: Appointment Status Transitions

**States**: `SCHEDULED`, `COMPLETED`, `CANCELLED`

#### State Transition Diagram

```
┌──────────────┐
│  SCHEDULED   │
└──────┬───────┘
       │
       ├──────────────┐
       │              │
       ▼              ▼
┌──────────────┐  ┌──────────────┐
│  COMPLETED   │  │  CANCELLED   │
└──────────────┘  └──────────────┘
```

#### Valid Transitions

- yes `SCHEDULED` → `COMPLETED` (when appointment is finished)
- ye `SCHEDULED` → `CANCELLED` (when appointment is cancelled)

#### State Transition Table

| Current State | Event | Next State | Valid? | Test Coverage |
|--------------|-------|------------|--------|---------------|
| `SCHEDULED` | Complete | `COMPLETED` | yes | `updateAppointment_ScheduledToCompleted_UpdatesStatus()` |
| `SCHEDULED` | Cancel | `CANCELLED` | yes | `updateAppointment_ScheduledToCancelled_UpdatesStatus()` |

#### Test Cases

| Test Method | Description | Location |
|-------------|-------------|----------|
| `updateAppointment_ScheduledToCompleted_UpdatesStatus()` | SCHEDULED → COMPLETED | `AppointmentServiceTest.java` |
| `updateAppointment_ScheduledToCancelled_UpdatesStatus()` | SCHEDULED → CANCELLED | `AppointmentServiceTest.java` |

> **Note**: The current implementation allows any status transition during creation/update. For production, business logic should validate state transitions.

---

## Test Case Mapping

This section maps the black-box test design techniques to actual test implementations.

### Summary of Test Coverage

| Technique | Functions Covered | Test Files | Test Methods |
|-----------|------------------|------------|--------------|
| **Equivalence Partitioning** | 2 major functions | AuthServiceTest, AppointmentServiceTest | **5+** |
| **Boundary Value Analysis** | 2 input domains | AuthServiceTest, WardServiceTest | **4+** |
| **Decision Tables** | 1 business rule | PatientServiceTest | **7** |
| **State Transition** | 1 state machine | AppointmentServiceTest | **2** |

### Test File Locations

#### 1. PatientServiceTest.java

- **Location**: `src/test/java/com/testing_exam_webapp/service/PatientServiceTest.java`
- **Coverage**: Decision tables (ward-hospital validation)
- **Key Tests**: Ward-hospital validation decision table with 7 test cases

#### 2. AuthServiceTest.java

- **Location**: `src/test/java/com/testing_exam_webapp/service/AuthServiceTest.java`
- **Coverage**: Equivalence partitioning, boundary value analysis
- **Key Tests**: Login authentication (equivalence partitioning), username/password boundaries

#### 3. WardServiceTest.java

- **Location**: `src/test/java/com/testing_exam_webapp/service/WardServiceTest.java`
- **Coverage**: Boundary value analysis
- **Key Tests**: Max capacity boundaries

#### 4. AppointmentServiceTest.java

- **Location**: `src/test/java/com/testing_exam_webapp/service/AppointmentServiceTest.java`
- **Coverage**: Equivalence partitioning, state transitions
- **Key Tests**: Appointment status (equivalence partitioning), status transitions

---

### Running the Tests

All black-box tests can be executed using:

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests PatientServiceTest
./gradlew test --tests AuthServiceTest
./gradlew test --tests WardServiceTest
./gradlew test --tests AppointmentServiceTest

# Generate test coverage report
./gradlew test jacocoTestReport
```

### Test Coverage Report

After running tests, view the coverage report:
- **Location**: `build/reports/jacoco/html/index.html`
- **Command**: `./gradlew test jacocoTestReport`


