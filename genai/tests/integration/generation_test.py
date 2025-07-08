from fastapi.testclient import TestClient
from unittest.mock import patch, MagicMock
from main import app

client = TestClient(app)


@patch("rag.llm.chat_model.ChatModel.invoke")
@patch("routes.routes.qdrant.client.collection_exists", return_value=False)
def test_generate_endpoint_success(_mock_exists, mock_invoke):
    mock_response = MagicMock()
    mock_response.content = "This is a test response"
    mock_invoke.return_value = mock_response

    payload = {
        "query": "What can I cook with rice?",
        "messages": [
            {"role": "USER", "content": "I have rice and eggs"},
            {"role": "ASSISTANT", "content": "How about fried rice?"}
        ],
        "user_id": 123
    }

    response = client.post("/genai/generate", json=payload)

    assert response.status_code == 200
    data = response.json()
    assert "response" in data
    assert data["response"] == "This is a test response"
    mock_invoke.assert_called_once()


@patch("rag.llm.chat_model.ChatModel.invoke")
@patch("routes.routes.qdrant.client.collection_exists", return_value=False)
def test_generate_endpoint_empty_messages(_mock_exists, mock_invoke):

    mock_response = MagicMock()
    mock_response.content = "No prior messages, here's a new idea!"
    mock_invoke.return_value = mock_response

    payload = {
        "query": "Can I cook with lentils?",
        "messages": [],
        "user_id": 123
    }

    response = client.post("/genai/generate", json=payload)

    assert response.status_code == 200
    data = response.json()
    assert "response" in data
    assert data["response"] == "No prior messages, here's a new idea!"


def test_generate_endpoint_missing_fields():
    payload = {
        "query": "Can I cook with lentils?"
        # "messages" key is missing
    }

    response = client.post("/genai/generate", json=payload)

    assert response.status_code == 400
    assert response.json() == {"detail": "Missing 'query', 'messages', or 'user_id'"}
