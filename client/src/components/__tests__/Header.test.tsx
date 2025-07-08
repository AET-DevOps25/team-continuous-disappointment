import { render, screen, fireEvent } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { vi } from "vitest";
import Header from "../Header";

const mockToggleSidebar = vi.fn();

describe("Header Component", () => {
  beforeEach(() => {
    mockToggleSidebar.mockClear();
  });

  it("renders the header with title", () => {
    render(
      <BrowserRouter>
        <Header isSidebarOpen={false} toggleSidebar={mockToggleSidebar} />
      </BrowserRouter>,
    );

    expect(screen.getByText("RecipAI")).toBeInTheDocument();
  });

  it("calls toggleSidebar when menu button is clicked", () => {
    render(
      <BrowserRouter>
        <Header isSidebarOpen={false} toggleSidebar={mockToggleSidebar} />
      </BrowserRouter>,
    );

    const menuButton = screen.getByRole("button", { name: /open sidebar/i });
    fireEvent.click(menuButton);

    expect(mockToggleSidebar).toHaveBeenCalledTimes(1);
  });

  it("shows theme toggle button", () => {
    render(
      <BrowserRouter>
        <Header isSidebarOpen={false} toggleSidebar={mockToggleSidebar} />
      </BrowserRouter>,
    );

    const themeButton = screen.getByRole("button", {
      name: /switch to dark mode/i,
    });
    expect(themeButton).toBeInTheDocument();
  });
});
