import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { vi } from "vitest";
import ChatInput from "../ChatInput";

describe("ChatInput Component", () => {
  const mockOnSendMessage = vi.fn();

  beforeEach(() => {
    mockOnSendMessage.mockClear();
  });

  it("renders input field and send button", () => {
    render(<ChatInput onSendMessage={mockOnSendMessage} />);

    expect(
      screen.getByPlaceholderText("Type a message..."),
    ).toBeInTheDocument();
    expect(screen.getByRole("button")).toBeInTheDocument();
    expect(
      screen.getByText("Press Enter to send, Shift+Enter for a new line"),
    ).toBeInTheDocument();
  });

  it("sends message when form is submitted", async () => {
    const user = userEvent.setup();
    render(<ChatInput onSendMessage={mockOnSendMessage} />);

    const textarea = screen.getByPlaceholderText("Type a message...");
    const sendButton = screen.getByRole("button");

    await user.type(textarea, "Hello world!");
    await user.click(sendButton);

    expect(mockOnSendMessage).toHaveBeenCalledWith("Hello world!");
    expect(textarea).toHaveValue("");
  });

  it("handles disabled state correctly", () => {
    render(<ChatInput onSendMessage={mockOnSendMessage} disabled={true} />);

    const textarea = screen.getByPlaceholderText("Type a message...");
    const sendButton = screen.getByRole("button");

    expect(textarea).toBeDisabled();
    expect(sendButton).toBeDisabled();
  });
});
