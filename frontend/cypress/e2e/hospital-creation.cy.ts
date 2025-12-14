/// <reference types="cypress" />

describe('Hospital Creation E2E Test', () => {
  beforeEach(() => {
    // Login before each test
    cy.login('admin', 'admin');
    cy.waitForApi();
  });

  it('should create a new hospital successfully', () => {
    // Navigate to create hospital page
    cy.visit('/hospitals/new');

    // Verify we're on the hospital form page
    cy.url().should('include', '/hospitals/new');
    
    // Wait for form to load and verify form title
    cy.contains('Create New Hospital', { timeout: 10000 }).should('be.visible');
    cy.waitForApi();

    // Fill in hospital form with required fields
    cy.get('#hospitalName').should('be.visible').clear().type('Odense Hospital');
    cy.get('#address').should('be.visible').clear().type('Odensevej 123');
    cy.get('#city').should('be.visible').clear().type('Odense');

    // Submit the form
    cy.get('button[type="submit"]').should('be.visible').should('contain', 'Create').click();

    // Wait for navigation after successful creation
    cy.waitForApi();
    
    // Should redirect to hospital list page
    cy.url({ timeout: 10000 }).should('satisfy', (url) => {
      return url.includes('/hospitals') && !url.includes('/new');
    });

    // Verify we're on the hospitals list page
    cy.contains(/hospitals|hospital/i, { timeout: 5000 }).should('be.visible');
  });
});

