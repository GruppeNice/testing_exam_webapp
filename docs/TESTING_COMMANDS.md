# 🧪 Testing Commands Reference

Quick reference guide for running different types of tests in the project.

---

## 📋 Table of Contents

1. [E2E Tests (Cypress)](#-e2e-tests-cypress)
2. [Backend Unit Tests (Java)](#-backend-unit-tests-java)
3. [Backend Integration Tests (Java)](#-backend-integration-tests-java)
4. [Frontend Unit Tests (Vitest)](#-frontend-unit-tests-vitest)
5. [Test Coverage Reports](#-test-coverage-reports)
6. [All Tests](#-all-tests)

---

## 🎭 E2E Tests (Cypress)

### Prerequisites
- Backend must be running on `http://localhost:8080`
- Frontend must be running on `http://localhost:5173`

### Run All E2E Tests
```bash
cd frontend
npm run e2e
```

### Run Specific E2E Test
```bash
cd frontend
npx cypress run --spec "cypress/e2e/login.cy.ts"
npx cypress run --spec "cypress/e2e/patient-management.cy.ts"
npx cypress run --spec "cypress/e2e/hospital-creation.cy.ts"
```

### Open Cypress Test Runner (Interactive)
```bash
cd frontend
npm run cypress:open
```

### Run E2E Tests in Headless Mode
```bash
cd frontend
npx cypress run
```

---

## 🔬 Backend Unit Tests (Java)

### Run All Unit Tests
```bash
./gradlew test
```

### Run Specific Test Class
```bash
./gradlew test --tests PatientServiceTest
./gradlew test --tests AuthServiceTest
./gradlew test --tests AppointmentServiceTest
```

### Run Tests by Package
```bash
# All service tests
./gradlew test --tests "com.testing_exam_webapp.service.*"

# All controller tests
./gradlew test --tests "com.testing_exam_webapp.controller.*"
```

### Run Specific Test Method
```bash
./gradlew test --tests PatientServiceTest.createPatient_ValidRequest_CreatesPatient
```

### Run Tests with Coverage Report
```bash
./gradlew test jacocoTestReport
```

### View Coverage Report
After running `./gradlew test jacocoTestReport`, open:
```
build/reports/jacoco/html/index.html
```

---

## 🔗 Backend Integration Tests (Java)

### Run All Integration Tests
```bash
./gradlew test --tests "com.testing_exam_webapp.integration.*"
```

### Run Specific Integration Test
```bash
./gradlew test --tests PatientServiceIntegrationTest
./gradlew test --tests WeatherServiceIntegrationTest
./gradlew test --tests TimeServiceIntegrationTest
```

---

## ⚛️ Frontend Unit Tests (Vitest)

### Run All Frontend Unit Tests
```bash
cd frontend
npm test
```

### Run Tests in Watch Mode
```bash
cd frontend
npm test -- --watch
```

### Run Tests with UI
```bash
cd frontend
npm run test:ui
```

### Run Tests with Coverage
```bash
cd frontend
npm run test:coverage
```

---

## 📊 Test Coverage Reports

### Backend Coverage (JaCoCo)
```bash
./gradlew test jacocoTestReport
```
**Report Location**: `build/reports/jacoco/html/index.html`

### Frontend Coverage (Vitest)
```bash
cd frontend
npm run test:coverage
```

---

## 🎯 All Tests

### Run All Backend Tests (Unit + Integration)
```bash
./gradlew test
```

### Run All Tests (Backend + Frontend)
```bash
# Terminal 1: Run backend tests
./gradlew test

# Terminal 2: Run frontend tests
cd frontend
npm test

# Terminal 3: Run E2E tests (requires both servers running)
cd frontend
npm run e2e
```

---

## 🔍 Useful Test Commands

### Run Tests with Verbose Output
```bash
./gradlew test --info
```

### Run Tests and Skip Coverage Report
```bash
./gradlew test --no-daemon
```

### Clean and Run Tests
```bash
./gradlew clean test
```

### Run Only Failed Tests
```bash
./gradlew test --rerun-tasks
```

### Run Tests Matching Pattern
```bash
./gradlew test --tests "*ServiceTest"
./gradlew test --tests "*IntegrationTest"
```

---

## 📝 Test File Locations

### E2E Tests
- `frontend/cypress/e2e/login.cy.ts`
- `frontend/cypress/e2e/patient-management.cy.ts`
- `frontend/cypress/e2e/hospital-creation.cy.ts`

### Backend Unit Tests
- `src/test/java/com/testing_exam_webapp/service/*Test.java`
- `src/test/java/com/testing_exam_webapp/controller/*Test.java`
- `src/test/java/com/testing_exam_webapp/exception/*Test.java`

### Backend Integration Tests
- `src/test/java/com/testing_exam_webapp/integration/PatientServiceIntegrationTest.java`
- `src/test/java/com/testing_exam_webapp/integration/WeatherServiceIntegrationTest.java`
- `src/test/java/com/testing_exam_webapp/integration/TimeServiceIntegrationTest.java`


---

## 🌱 Database Seeding (For Stress Testing)

### PowerShell Scripts (Windows)
Seed the database using the PowerShell scripts:

**Step 1:** Start the application in one terminal:
```bash
./gradlew bootRun
```

**Step 2:** In another terminal, run one of the seeding scripts:

**Quick seed (50 of each):**
```powershell
.\seed-database.ps1
```
Seeds: 50 hospitals, 50 patients, 50 doctors, 50 nurses, 100 appointments

**Large seed (500 of each):**
```powershell
.\seed-database-large.ps1
```
Seeds: 500 hospitals, 500 patients, 500 doctors, 500 nurses, 1000 appointments

Both scripts automatically:
- Log in as admin
- Get JWT token
- Seed the database
- Show results

You can run the scripts multiple times without restarting the app.

## ⚠️ Important Notes

1. **E2E Tests**: Require both backend (`http://localhost:8080`) and frontend (`http://localhost:5173`) to be running
2. **Integration Tests**: Use H2 in-memory database (configured in `application-test.properties`)
3. **Unit Tests**: Use mocks and don't require a running database
4. **Coverage Reports**: Generated automatically after running tests with `jacocoTestReport` task
5. **Database Seeding**: Run `.\seed-database.ps1` after starting the app with `./gradlew bootRun`

---

<div align="center">

**Quick Reference** | **Command**
--- | ---
All backend tests | `./gradlew test`
All E2E tests | `cd frontend && npm run e2e`
Single E2E test | `cd frontend && npx cypress run --spec "cypress/e2e/login.cy.ts"`
Single unit test | `./gradlew test --tests PatientServiceTest`
Coverage report | `./gradlew test jacocoTestReport`
Seed database (50 each) | `.\seed-database.ps1` (after `./gradlew bootRun`)
Seed database (500 each) | `.\seed-database-large.ps1` (after `./gradlew bootRun`)

</div>

