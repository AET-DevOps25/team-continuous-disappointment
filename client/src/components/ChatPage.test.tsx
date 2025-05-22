import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom'; // Import this line
//import ChatPage from './ChatPage';

describe('ChatPage', () => {
    beforeEach(() => {
        jest.useFakeTimers();
    });

    afterEach(() => {
        jest.useRealTimers();
    });

    test('Test', async () => {
        expect(1==1).toBeTruthy();
    });
});
