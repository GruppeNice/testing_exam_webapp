import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ErrorMessage } from '../../../components/common/ErrorMessage';

describe('ErrorMessage', () => {
  it('should render error message', () => {
    const message = 'Something went wrong';
    render(<ErrorMessage message={message} />);

    expect(screen.getByText(message)).toBeInTheDocument();
    expect(screen.getByRole('alert')).toBeInTheDocument();
  });

  it('should render dismiss button when onDismiss is provided', () => {
    const message = 'Error message';
    const onDismiss = vi.fn();
    render(<ErrorMessage message={message} onDismiss={onDismiss} />);

    const dismissButton = screen.getByRole('button');
    expect(dismissButton).toBeInTheDocument();
  });

  it('should not render dismiss button when onDismiss is not provided', () => {
    const message = 'Error message';
    render(<ErrorMessage message={message} />);

    expect(screen.queryByRole('button')).not.toBeInTheDocument();
  });

  it('should call onDismiss when dismiss button is clicked', async () => {
    const user = userEvent.setup();
    const message = 'Error message';
    const onDismiss = vi.fn();
    render(<ErrorMessage message={message} onDismiss={onDismiss} />);

    const dismissButton = screen.getByRole('button');
    await user.click(dismissButton);

    expect(onDismiss).toHaveBeenCalledTimes(1);
  });

  it('should render message with long text', () => {
    const longMessage = 'This is a very long error message that should still be displayed correctly in the error message component';
    render(<ErrorMessage message={longMessage} />);

    expect(screen.getByText(longMessage)).toBeInTheDocument();
  });

  it('should have correct CSS classes for styling', () => {
    const message = 'Error message';
    const { container } = render(<ErrorMessage message={message} />);

    const alertElement = container.querySelector('.bg-red-50');
    expect(alertElement).toBeInTheDocument();
    expect(alertElement).toHaveClass('border-red-200', 'text-red-800');
  });
});

