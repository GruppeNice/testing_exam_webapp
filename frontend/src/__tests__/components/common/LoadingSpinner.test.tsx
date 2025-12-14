import { describe, it, expect } from 'vitest';
import { render } from '@testing-library/react';
import { LoadingSpinner } from '../../../components/common/LoadingSpinner';

describe('LoadingSpinner', () => {
  it('should render loading spinner with default size', () => {
    const { container } = render(<LoadingSpinner />);

    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toBeInTheDocument();
    expect(spinner).toHaveClass('w-8', 'h-8'); // medium size (default)
  });

  it('should render loading spinner with small size', () => {
    const { container } = render(<LoadingSpinner size="small" />);

    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toBeInTheDocument();
    expect(spinner).toHaveClass('w-4', 'h-4'); // small size
  });

  it('should render loading spinner with medium size', () => {
    const { container } = render(<LoadingSpinner size="medium" />);

    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toBeInTheDocument();
    expect(spinner).toHaveClass('w-8', 'h-8'); // medium size
  });

  it('should render loading spinner with large size', () => {
    const { container } = render(<LoadingSpinner size="large" />);

    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toBeInTheDocument();
    expect(spinner).toHaveClass('w-12', 'h-12'); // large size
  });

  it('should have correct CSS classes for styling', () => {
    const { container } = render(<LoadingSpinner />);

    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-4', 'border-blue-200', 'border-t-blue-600', 'rounded-full');
  });

  it('should be centered in flex container', () => {
    const { container } = render(<LoadingSpinner />);

    const containerElement = container.querySelector('.flex');
    expect(containerElement).toBeInTheDocument();
    expect(containerElement).toHaveClass('justify-center', 'items-center');
  });
});

