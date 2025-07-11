import { render, screen } from "@testing-library/react";
import { vi } from "vitest";
import MessageItem from "../MessageItem";

// Mock the helpers module
vi.mock("../../utils/helpers", () => ({
  formatTimestamp: vi.fn((timestamp) => `${timestamp} ago`),
}));

describe("MessageItem Component", () => {
  const mockUserMessage = {
    id: "1",
    role: "USER" as const,
    content: "Hello, how are you?",
    timestamp: new Date("2024-01-15T10:30:00Z"),
  };

  const mockBotMessage = {
    id: "2",
    role: "ASSISTANT" as const,
    content: "I'm doing well, thank you!\nHow can I help you today?",
    timestamp: new Date("2024-01-15T10:31:00Z"),
  };

  it("renders user message correctly", () => {
    render(<MessageItem message={mockUserMessage} />);

    expect(screen.getByText("You")).toBeInTheDocument();
    expect(screen.getByText("Hello, how are you?")).toBeInTheDocument();
    expect(screen.getByText(/ago/)).toBeInTheDocument();
  });

  it("renders bot message correctly", () => {
    render(<MessageItem message={mockBotMessage} />);

    expect(screen.getByText("AI Assistant")).toBeInTheDocument();
    expect(screen.getByText(/I'm doing well, thank you!/)).toBeInTheDocument();
    expect(screen.getByText(/How can I help you today?/)).toBeInTheDocument();
    expect(screen.getByText(/ago/)).toBeInTheDocument();
  });

  it("handles multi-line content with proper paragraph breaks", () => {
    render(<MessageItem message={mockBotMessage} />);

    // With react-markdown, newlines create line breaks within a single paragraph
    const paragraph = screen.getByText(/I'm doing well, thank you!/);
    expect(paragraph).toBeInTheDocument();
    expect(paragraph.textContent).toContain("I'm doing well, thank you!");
    expect(paragraph.textContent).toContain("How can I help you today?");
  });
});
