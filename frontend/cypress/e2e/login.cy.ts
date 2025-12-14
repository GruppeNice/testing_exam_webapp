/// <reference types="cypress" />

describe('Login E2E Test', () => {
  beforeEach(() => {
    // Clear any existing session
    cy.clearLocalStorage();
    cy.clearCookies();
  });

  it('should successfully login with valid credentials and redirect to dashboard', () => {
    // Visit the login page
    cy.visit('/login');

    // Verify we're on the login page
    cy.url().should('include', '/login');
    cy.contains('Hospital Management System').should('be.visible');
    cy.contains('Sign in to your account').should('be.visible');

    // Fill in login form
    cy.get('#username').should('be.visible').type('admin');
    cy.get('#password').should('be.visible').type('admin');

    // Submit the form
    cy.get('button[type="submit"]').should('be.visible').click();

    // Wait for navigation to dashboard
    cy.url({ timeout: 10000 }).should('include', '/dashboard');

    // Verify we're on the dashboard
    cy.contains('Dashboard', { timeout: 10000 }).should('be.visible');
  });

  it('should show error message with invalid credentials', () => {
    cy.visit('/login');

    // Fill in with invalid credentials
    cy.get('#username').type('invaliduser');
    cy.get('#password').type('wrongpassword');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Wait for error message to appear
    cy.contains('Login failed', { timeout: 5000 }).should('be.visible');
    
    // Should still be on login page
    cy.url().should('include', '/login');
  });

  it('should redirect to dashboard if already authenticated', () => {
    // First login
    cy.login('admin', 'admin');

    // Try to visit login page again
    cy.visit('/login');

    // Should be redirected to dashboard
    cy.url().should('include', '/dashboard');
  });

  it('should have login form elements visible and accessible', () => {
    cy.visit('/login');

    // Check all form elements are present
    cy.get('#username').should('be.visible').should('have.attr', 'type', 'text');
    cy.get('#password').should('be.visible').should('have.attr', 'type', 'password');
    cy.get('button[type="submit"]').should('be.visible').should('contain', 'Sign in');
    
    // Check default credentials hint
    cy.contains('Default credentials: admin / admin').should('be.visible');
  });
});

