# Cypress E2E Tests

This directory contains End-to-End (E2E) tests for the Hospital Management System frontend.

## Prerequisites

Before running the E2E tests, ensure:

1. **Backend is running** on `http://localhost:8080`
   - The backend should be using H2 database (configured in test profile)
   - Start the backend with: `./gradlew bootRun` (from project root)

2. **Frontend is running** on `http://localhost:5173`
   - Start the frontend with: `npm run dev` (from frontend directory)

3. **Test user exists** in the database:
   - Username: `admin`
   - Password: `admin`
   - (This should be created by the DataInitializer on backend startup)

## Running Tests

### Open Cypress Test Runner (Interactive)
```bash
npm run cypress:open
```

This opens the Cypress Test Runner GUI where you can:
- Select and run individual tests
- Watch tests execute in real-time
- Debug tests interactively

### Run Tests Headlessly (CI/CD)
```bash
npm run cypress:run
```

This runs all tests in headless mode (no browser window) and is suitable for CI/CD pipelines.

### Run Specific Test File
```bash
npx cypress run --spec "cypress/e2e/patient-management.cy.ts"
```

## Test Files

### `login.cy.ts`
Tests the login and authentication flow:
- Successful login with valid credentials
- Error handling with invalid credentials
- Redirect behavior when already authenticated

### `patient-management.cy.ts`
Tests patient creation:
- Navigate to patient creation form
- Fill in required fields (name, date of birth, gender)
- Submit and verify successful creation

### `hospital-creation.cy.ts`
Tests hospital creation:
- Navigate to hospital creation form
- Fill in required fields (name, address, city)
- Submit and verify successful creation

## Configuration

The Cypress configuration is in `cypress.config.ts`:
- Base URL: `http://localhost:5173` (frontend)
- API URL: `http://localhost:8080` (backend)
- Test files: `cypress/e2e/**/*.cy.{js,jsx,ts,tsx}`

## Custom Commands

Custom Cypress commands are defined in `cypress/support/commands.ts`:
- `cy.login(username, password)` - Login helper
- `cy.waitForApi()` - Wait for API calls to complete

## Troubleshooting

### Tests fail with "Connection refused"
- Ensure both backend and frontend are running
- Check that ports 8080 and 5173 are not in use by other applications

### Tests fail with authentication errors
- Verify the test user (admin/admin) exists in the database
- Check that the backend DataInitializer has run successfully

### Tests timeout waiting for elements
- Increase timeout values in test files if needed
- Check browser console for JavaScript errors
- Verify the frontend is fully loaded before tests run

