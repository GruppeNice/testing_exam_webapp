/// <reference types="cypress" />

describe('Hospital Creation E2E Test', () => {
  beforeEach(() => {
    cy.login('admin', 'admin');
    cy.waitForApi();
  });

  it('should create a new hospital successfully', () => {
    cy.visit('/hospitals/new');

    cy.url().should('include', '/hospitals/new');
    
    cy.contains('Create New Hospital', { timeout: 10000 }).should('be.visible');
    cy.waitForApi();

    cy.get('#hospitalName').should('be.visible').clear().type('Odense Hospital');
    cy.get('#address').should('be.visible').clear().type('Odensevej 123');
    cy.get('#city').should('be.visible').clear().type('Odense');

    cy.get('button[type="submit"]').should('be.visible').should('contain', 'Create').click();

    cy.waitForApi();
    
    cy.url({ timeout: 10000 }).should('satisfy', (url) => {
      return url.includes('/hospitals') && !url.includes('/new');
    });

    cy.contains(/hospitals|hospital/i, { timeout: 5000 }).should('be.visible');
  });
});

