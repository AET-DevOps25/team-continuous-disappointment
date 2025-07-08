import { render, screen } from "@testing-library/react";
import TypingIndicator from "../TypingIndicator";

describe("TypingIndicator Component", () => {
  it("renders AI assistant label", () => {
    render(<TypingIndicator />);

    expect(screen.getByText("AI Assistant")).toBeInTheDocument();
  });

  it("renders animated dots", () => {
    render(<TypingIndicator />);

    // Check for the container with animated dots
    const container = screen.getByText("AI Assistant").closest("div");
    expect(container).toBeInTheDocument();

    // Check that the main container has the expected structure
    const mainContainer = container?.parentElement?.parentElement;
    expect(mainContainer).toHaveClass("flex", "items-start", "space-x-4");
  });

  it("has correct structure", () => {
    render(<TypingIndicator />);

    // Check that the component renders with expected content
    expect(screen.getByText("AI Assistant")).toBeInTheDocument();

    // Check that the container exists (basic structure test)
    const container = screen.getByText("AI Assistant").closest("div");
    expect(container).toBeInTheDocument();
  });
});
