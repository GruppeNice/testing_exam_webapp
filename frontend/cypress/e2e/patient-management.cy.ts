/// <reference types="cypress" />

describe('Patient Creation E2E Test', () => {
  beforeEach(() => {
    cy.login('admin', 'admin');
    cy.waitForApi();
  });

  it('should create a new patient successfully', () => {
    cy.visit('/patients/new');

    cy.url().should('include', '/patients/new');
    
    cy.contains('Create New Patient', { timeout: 10000 }).should('be.visible');
    cy.waitForApi();

    cy.get('#patientName').should('be.visible').clear().type('E2E Test Patient');
    cy.get('#dateOfBirth').should('be.visible').clear().type('1990-01-15');
    
    cy.get('#gender').should('be.visible').select('Male');

    cy.wait(2000);

    cy.get('button[type="submit"]').should('be.visible').should('contain', 'Create').click();

    cy.waitForApi();
    
    cy.url({ timeout: 10000 }).should('satisfy', (url) => {
      return url.includes('/patients') && !url.includes('/new');
    });

    cy.contains(/patients|patient/i, { timeout: 5000 }).should('be.visible');
  });
});

