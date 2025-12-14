/// <reference types="cypress" />

describe('Patient Creation E2E Test', () => {
  beforeEach(() => {
    // Login before each test
    cy.login('admin', 'admin');
    cy.waitForApi();
  });

  it('should create a new patient successfully', () => {
    // Navigate to create patient page
    cy.visit('/patients/new');

    // Verify we're on the patient form page
    cy.url().should('include', '/patients/new');
    
    // Wait for form to load and verify form title
    cy.contains('Create New Patient', { timeout: 10000 }).should('be.visible');
    cy.waitForApi();

    // Fill in patient form with required fields
    cy.get('#patientName').should('be.visible').clear().type('E2E Test Patient');
    cy.get('#dateOfBirth').should('be.visible').clear().type('1990-01-15');
    
    // Select gender (optional field)
    cy.get('#gender').should('be.visible').select('Male');

    // Wait for hospitals and wards dropdowns to load
    cy.wait(2000);

    // Hospital and ward are optional, so we'll skip them for simplicity
    // The form should work with just required fields

    // Submit the form
    cy.get('button[type="submit"]').should('be.visible').should('contain', 'Create').click();

    // Wait for navigation after successful creation
    cy.waitForApi();
    
    // Should redirect to patient list page
    cy.url({ timeout: 10000 }).should('satisfy', (url) => {
      return url.includes('/patients') && !url.includes('/new');
    });

    // Verify we're on the patients list page
    cy.contains(/patients|patient/i, { timeout: 5000 }).should('be.visible');
  });
});

