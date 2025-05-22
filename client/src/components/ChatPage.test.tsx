import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom'; // Import this line
import ChatPage from './ChatPage';

describe('ChatPage', () => {
    beforeEach(() => {
        jest.useFakeTimers();
    });

    afterEach(() => {
        jest.useRealTimers();
    });

    test('renders ChatPage component', () => {
        render(<ChatPage />);
        expect(screen.getByPlaceholderText('Enter ingredients and preferences...')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /Generate Recipe/i })).toBeInTheDocument();
    });

    test('updates input value on change', () => {
        render(<ChatPage />);
        const inputElement = screen.getByPlaceholderText('Enter ingredients and preferences...') as HTMLInputElement;
        fireEvent.change(inputElement, { target: { value: 'chicken and rice' } });
        expect(inputElement.value).toBe('chicken and rice');
    });

    test('adds user message and clears input on send', async () => {
        render(<ChatPage />);
        const inputElement = screen.getByPlaceholderText('Enter ingredients and preferences...') as HTMLInputElement;
        const sendButton = screen.getByRole('button', { name: /Generate Recipe/i });

        fireEvent.change(inputElement, { target: { value: 'pasta with tomatoes' } });
        fireEvent.click(sendButton);

        // Check if user message is added
        await waitFor(() => {
            expect(screen.getByText('pasta with tomatoes')).toBeInTheDocument();
        });

        // Check if input is cleared
        expect(inputElement.value).toBe('');
    });

    test('simulates AI response after delay', async () => {
        render(<ChatPage />);
        const inputElement = screen.getByPlaceholderText('Enter ingredients and preferences...') as HTMLInputElement;
        const sendButton = screen.getByRole('button', { name: /Generate Recipe/i });

        fireEvent.change(inputElement, { target: { value: 'vegan curry' } });
        fireEvent.click(sendButton);

        // Advance timers by 1000ms (the delay for AI response)
        jest.advanceTimersByTime(1000);

        // Check if AI message appears
        await waitFor(() => {
            expect(screen.getByText(/AI-generated recipe based on your input:/i)).toBeInTheDocument();
        });
    });

    test('does not send empty message', () => {
        render(<ChatPage />);
        const sendButton = screen.getByRole('button', { name: /Generate Recipe/i });

        fireEvent.click(sendButton);

        // Check that no message is added
        expect(screen.queryByText('AI-generated recipe based on your input:')).not.toBeInTheDocument();
        expect(screen.queryByText('')).not.toBeInTheDocument(); // Assuming empty string won't be rendered as a message
    });
});
