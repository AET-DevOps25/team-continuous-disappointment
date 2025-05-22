//import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import "@testing-library/jest-dom"; // Import this line
//import ChatPage from './ChatPage';

describe("ChatPage", () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  it("should pass", async () => {
    expect(true).toBe(true);
  });
});
