import { describe, it, expect, beforeEach } from 'vitest';

describe('main.tsx', () => {
  beforeEach(() => {
    // Create a root element for testing
    document.body.innerHTML = '<div id="root"></div>';
  });

  it('should have a root element', () => {
    const rootElement = document.getElementById('root');
    expect(rootElement).toBeTruthy();
  });
});

