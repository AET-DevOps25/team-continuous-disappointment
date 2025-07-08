import { render, screen } from "@testing-library/react";
import { vi } from "vitest";
import MessageList from "../MessageList";

// Mock the child components
vi.mock("../MessageItem", () => ({
  default: ({ message }: { message: Message }) => (
    <div data-testid={`message-${message.id}`}>
      {message.content} - {message.role}
    </div>
  ),
}));

vi.mock("../TypingIndicator", () => ({
  default: () => <div data-testid="typing-indicator">AI is typing...</div>,
}));

const mockMessages: Message[] = [
  {
    id: "1",
    role: "USER",
    content: "Hello, how are you?",
    timestamp: new Date("2024-01-15T10:30:00Z"),
  },
  {
    id: "2",
    role: "ASSISTANT",
    content: "I'm doing well, thank you!",
    timestamp: new Date("2024-01-15T10:31:00Z"),
  },
];

describe("MessageList Component", () => {
  it("renders empty state when no messages", () => {
    render(<MessageList messages={[]} isTyping={false} />);

    expect(screen.getByText("How can I help you today?")).toBeInTheDocument();
    expect(
      screen.getByText(
        "Ask me anything! I'm here to assist with information, answer questions, or just chat."
      )
    ).toBeInTheDocument();
  });

  it("renders list of messages", () => {
    render(<MessageList messages={mockMessages} isTyping={false} />);

    expect(screen.getByTestId("message-1")).toBeInTheDocument();
    expect(screen.getByTestId("message-2")).toBeInTheDocument();
    expect(screen.getByText("Hello, how are you? - USER")).toBeInTheDocument();
    expect(screen.getByText("I'm doing well, thank you! - ASSISTANT")).toBeInTheDocument();
  });

  it("shows typing indicator when isTyping is true", () => {
    render(<MessageList messages={mockMessages} isTyping={true} />);

    expect(screen.getByTestId("typing-indicator")).toBeInTheDocument();
    expect(screen.getByText("AI is typing...")).toBeInTheDocument();
  });

  it("does not show typing indicator when isTyping is false", () => {
    render(<MessageList messages={mockMessages} isTyping={false} />);

    expect(screen.queryByTestId("typing-indicator")).not.toBeInTheDocument();
  });

  it("does not show empty state when messages exist", () => {
    render(<MessageList messages={mockMessages} isTyping={false} />);

    expect(screen.queryByText("How can I help you today?")).not.toBeInTheDocument();
  });
});
