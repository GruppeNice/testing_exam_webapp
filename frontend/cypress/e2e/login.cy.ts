/// <reference types="cypress" />

describe('Login E2E Test', () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.clearCookies();
  });

  it('should successfully login with valid credentials and redirect to dashboard', () => {
    cy.visit('/login');

    cy.url().should('include', '/login');
    cy.contains('Hospital Management System').should('be.visible');
    cy.contains('Sign in to your account').should('be.visible');

    cy.get('#username').should('be.visible').type('admin');
    cy.get('#password').should('be.visible').type('admin');

    cy.get('button[type="submit"]').should('be.visible').click();

    cy.url({ timeout: 10000 }).should('include', '/dashboard');

    cy.contains('Dashboard', { timeout: 10000 }).should('be.visible');
  });

  it('should show error message with invalid credentials', () => {
    cy.visit('/login');

    cy.get('#username').type('invaliduser');
    cy.get('#password').type('wrongpassword');

    cy.get('button[type="submit"]').click();

    cy.contains('Login failed', { timeout: 5000 }).should('be.visible');
    
    cy.url().should('include', '/login');
  });

  it('should redirect to dashboard if already authenticated', () => {
    cy.login('admin', 'admin');

    cy.visit('/login');

    cy.url().should('include', '/dashboard');
  });

  it('should have login form elements visible and accessible', () => {
    cy.visit('/login');

    cy.get('#username').should('be.visible').should('have.attr', 'type', 'text');
    cy.get('#password').should('be.visible').should('have.attr', 'type', 'password');
    cy.get('button[type="submit"]').should('be.visible').should('contain', 'Sign in');
    
    cy.contains('Default credentials: admin / admin').should('be.visible');
  });
});

