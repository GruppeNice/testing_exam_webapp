/// <reference types="cypress" />

// ***********************************************
// This example commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************

declare global {
  namespace Cypress {
    interface Chainable {
      /**
       * Custom command to login a user
       * @example cy.login('admin', 'admin')
       */
      login(username: string, password: string): Chainable<void>;
      
      /**
       * Custom command to wait for API calls to complete
       * @example cy.waitForApi()
       */
      waitForApi(): Chainable<void>;
    }
  }
}

Cypress.Commands.add('login', (username: string, password: string) => {
  cy.visit('/login');
  cy.get('#username').type(username);
  cy.get('#password').type(password);
  cy.get('button[type="submit"]').click();
  // Wait for navigation to dashboard after successful login
  cy.url().should('include', '/dashboard');
});

Cypress.Commands.add('waitForApi', () => {
  // Wait for any pending API requests to complete
  cy.wait(1000); // Adjust as needed
});

// Make this file a module so declare global works
export default {};

