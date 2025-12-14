import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { ProtectedRoute } from '../../../components/common/ProtectedRoute';
import { useAuth } from '../../../context/AuthContext';

vi.mock('../../../context/AuthContext');

describe('ProtectedRoute', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render children when authenticated', () => {
    (useAuth as any).mockReturnValue({
      isAuthenticated: true,
      loading: false,
    });

    render(
      <MemoryRouter>
        <ProtectedRoute>
          <div>Protected Content</div>
        </ProtectedRoute>
      </MemoryRouter>
    );

    expect(screen.getByText('Protected Content')).toBeInTheDocument();
  });

  it('should show loading spinner when loading', () => {
    (useAuth as any).mockReturnValue({
      isAuthenticated: false,
      loading: true,
    });

    const { container } = render(
      <MemoryRouter>
        <ProtectedRoute>
          <div>Protected Content</div>
        </ProtectedRoute>
      </MemoryRouter>
    );

    expect(container.querySelector('.animate-spin')).toBeInTheDocument();
    expect(screen.queryByText('Protected Content')).not.toBeInTheDocument();
  });

  it('should redirect to login when not authenticated', () => {
    (useAuth as any).mockReturnValue({
      isAuthenticated: false,
      loading: false,
    });

    render(
      <MemoryRouter initialEntries={['/protected']}>
        <ProtectedRoute>
          <div>Protected Content</div>
        </ProtectedRoute>
      </MemoryRouter>
    );

    // Navigate component should redirect, so protected content should not be visible
    expect(screen.queryByText('Protected Content')).not.toBeInTheDocument();
  });

  it('should render multiple children when authenticated', () => {
    (useAuth as any).mockReturnValue({
      isAuthenticated: true,
      loading: false,
    });

    render(
      <MemoryRouter>
        <ProtectedRoute>
          <div>Child 1</div>
          <div>Child 2</div>
        </ProtectedRoute>
      </MemoryRouter>
    );

    expect(screen.getByText('Child 1')).toBeInTheDocument();
    expect(screen.getByText('Child 2')).toBeInTheDocument();
  });
});

