import io
from unittest.mock import patch, MagicMock
from fastapi.testclient import TestClient
from main import app

client = TestClient(app)


@patch("routes.routes.qdrant.client.collection_exists", return_value=False)
@patch("routes.routes.qdrant.create_and_get_vector_storage")
@patch("routes.routes.IngestionPipeline")
def test_upload_file_success(mock_pipeline_class, _mock_vector_store, _mock_exists):
    mock_pipeline = MagicMock()
    mock_pipeline_class.return_value = mock_pipeline

    file_content = b"%PDF-1.4 dummy content"
    file = io.BytesIO(file_content)
    file.name = "test.pdf"

    response = client.post(
        "/genai/upload",
        files={"file": ("test.pdf", file, "application/pdf")}
    )

    assert response.status_code == 200
    assert response.json() == {"message": "File processed successfully."}

    mock_pipeline_class.assert_called_once()
    mock_pipeline.ingest.assert_called_once()

def test_upload_file_invalid_type():
    file = io.BytesIO(b"just some text")
    file.name = "notes.txt"

    response = client.post(
        "/genai/upload",
        files={"file": ("notes.txt", file, "text/plain")}
    )

    assert response.status_code == 400
    assert response.json()["detail"] == "Invalid file type. Only PDF files are allowed."


@patch("routes.routes.qdrant.client.collection_exists", return_value=True)
@patch("routes.routes.qdrant.collection_contains_file", return_value=True)
def test_upload_file_already_exists(_mock_contains, _mock_exists):
    file = io.BytesIO(b"%PDF-1.4")
    file.name = "existing.pdf"

    response = client.post(
        "/genai/upload",
        files={"file": ("existing.pdf", file, "application/pdf")}
    )

    assert response.status_code == 200
    assert "already uploaded" in response.json()["message"]