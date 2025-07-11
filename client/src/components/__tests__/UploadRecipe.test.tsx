import { render, screen, fireEvent, act } from "@testing-library/react";
import { vi } from "vitest";
import UploadRecipe from "../UploadRecipe";

// Mock the useAuth hook
vi.mock("../../hooks/useAuth", () => ({
  useAuth: vi.fn(() => ({
    user: {
      token: "test-token",
    },
  })),
}));

// Mock fetch
const mockFetch = vi.fn();
global.fetch = mockFetch;

describe("UploadRecipe Component", () => {
  beforeEach(() => {
    mockFetch.mockClear();
  });

  it("renders upload button in idle state", () => {
    render(<UploadRecipe />);

    expect(screen.getByText("Upload Recipe Book")).toBeInTheDocument();
    expect(screen.getByLabelText("Upload Recipe Book")).toBeInTheDocument();
  });

  it("shows uploading state when file is selected", async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({}),
    });

    render(<UploadRecipe />);

    const fileInput = screen.getByLabelText(
      "Upload Recipe Book",
    ) as HTMLInputElement;
    const file = new File(["test content"], "recipe.pdf", {
      type: "application/pdf",
    });

    await act(async () => {
      fireEvent.change(fileInput!, { target: { files: [file] } });
    });

    // Check that fetch was called
    expect(mockFetch).toHaveBeenCalledWith("/api/genai/upload", {
      method: "POST",
      body: expect.any(FormData),
      headers: {
        Authorization: "Bearer test-token",
      },
    });
  });

  it("does not upload when no file is selected", () => {
    render(<UploadRecipe />);

    const fileInput = screen.getByLabelText(
      "Upload Recipe Book",
    ) as HTMLInputElement;

    fireEvent.change(fileInput!, { target: { files: [] } });

    expect(mockFetch).not.toHaveBeenCalled();
    expect(screen.getByText("Upload Recipe Book")).toBeInTheDocument();
  });
});
