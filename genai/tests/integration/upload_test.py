import io
from unittest.mock import patch
from fastapi.testclient import TestClient
from main import app
from service.auth_service import UserInfo, get_current_user

client = TestClient(app)


@patch("routes.routes.ingest_file")
@patch("routes.routes.file_already_uploaded", return_value=False)
def test_upload_file_success(
        mock_file_uploaded,
        mock_ingest_file
):
    # Mock the authentication by overriding the dependency
    mock_user = UserInfo(user_id=123, username="test_user")
    app.dependency_overrides[get_current_user] = lambda: mock_user

    file_content = b"%PDF-1.4 dummy content"
    file = io.BytesIO(file_content)
    file.name = "test.pdf"

    response = client.post(
        "/genai/upload",
        files={"file": ("test.pdf", file, "application/pdf")}
    )

    assert response.status_code == 200
    assert response.json() == {"message": "File processed successfully."}

    mock_ingest_file.assert_called_once()
    mock_file_uploaded.assert_called_once()

    # Clean up the dependency override
    app.dependency_overrides.clear()


def test_upload_file_invalid_type():
    # Mock the authentication by overriding the dependency
    mock_user = UserInfo(user_id=123, username="test_user")
    app.dependency_overrides[get_current_user] = lambda: mock_user

    file = io.BytesIO(b"just some text")
    file.name = "notes.txt"

    response = client.post(
        "/genai/upload",
        files={"file": ("notes.txt", file, "text/plain")}
    )

    assert response.status_code == 400
    assert (response.json()["detail"] ==
            "Invalid file type. Only PDF files are allowed.")

    # Clean up the dependency override
    app.dependency_overrides.clear()


@patch(
    "service.qdrant_service.qdrant.client.collection_exists",
    return_value=True
    )
@patch(
    "service.qdrant_service.qdrant.collection_contains_file",
    return_value=True
    )
def test_upload_file_already_exists(_mock_contains, _mock_exists):
    # Mock the authentication
    mock_user = UserInfo(user_id=123, username="test_user")
    app.dependency_overrides[get_current_user] = lambda: mock_user

    file = io.BytesIO(b"%PDF-1.4")
    file.name = "existing.pdf"

    response = client.post(
        "/genai/upload",
        files={"file": ("existing.pdf", file, "application/pdf")}
    )

    assert response.status_code == 200
    assert "already uploaded" in response.json()["message"]

    # Clean up the dependency override
    app.dependency_overrides.clear()
