import { render, screen, fireEvent } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { vi } from "vitest";
import Sidebar from "../Sidebar";

// Mock the helpers module
vi.mock("../../utils/helpers", () => ({
  formatTimestamp: vi.fn((timestamp) => `${timestamp} ago`),
  classNames: vi.fn((...classes) => classes.filter(Boolean).join(" ")),
}));

// Mock UploadRecipe component
vi.mock("../UploadRecipe", () => ({
  default: () => <div data-testid="upload-recipe">Upload Recipe Component</div>,
}));

const mockConversations = [
  {
    id: "1",
    title: "Recipe for Pasta",
    createdAt: new Date("2024-01-15T10:30:00Z"),
    messages: [],
  },
  {
    id: "2",
    title: "Healthy Breakfast Ideas",
    createdAt: new Date("2024-01-14T09:15:00Z"),
    messages: [],
  },
];

describe("Sidebar Component", () => {
  const mockCreateNewConversation = vi.fn();
  const mockDeleteConversation = vi.fn();

  beforeEach(() => {
    mockCreateNewConversation.mockClear();
    mockDeleteConversation.mockClear();
  });

  it("renders new chat button and conversations when open", () => {
    render(
      <BrowserRouter>
        <Sidebar
          conversations={mockConversations}
          createNewConversation={mockCreateNewConversation}
          deleteConversation={mockDeleteConversation}
          isOpen={true}
        />
      </BrowserRouter>,
    );

    expect(screen.getByText("New Chat")).toBeInTheDocument();
    expect(screen.getByText("Recipe for Pasta")).toBeInTheDocument();
    expect(screen.getByText("Healthy Breakfast Ideas")).toBeInTheDocument();
    expect(screen.getByTestId("upload-recipe")).toBeInTheDocument();
  });

  it("calls createNewConversation when New Chat button is clicked", () => {
    render(
      <BrowserRouter>
        <Sidebar
          conversations={mockConversations}
          createNewConversation={mockCreateNewConversation}
          deleteConversation={mockDeleteConversation}
          isOpen={true}
        />
      </BrowserRouter>,
    );

    const newChatButton = screen.getByText("New Chat");
    fireEvent.click(newChatButton);

    expect(mockCreateNewConversation).toHaveBeenCalledTimes(1);
  });

  it("calls deleteConversation when delete button is clicked", () => {
    render(
      <BrowserRouter>
        <Sidebar
          conversations={mockConversations}
          createNewConversation={mockCreateNewConversation}
          deleteConversation={mockDeleteConversation}
          isOpen={true}
        />
      </BrowserRouter>,
    );

    const deleteButtons = screen.getAllByLabelText("Delete conversation");
    fireEvent.click(deleteButtons[0]);

    expect(mockDeleteConversation).toHaveBeenCalledWith("1");
  });
});
