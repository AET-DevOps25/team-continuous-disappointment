from unittest.mock import MagicMock
from langchain_core.documents import Document
from service.rag_service import retrieve_similar_docs

def test_retrieve_similar_docs_returns_joined_string():
    mock_vector_store = MagicMock()
    mock_retriever = mock_vector_store.as_retriever.return_value
    mock_retriever.invoke.return_value = [
        Document(page_content="Doc 1"),
        Document(page_content="Doc 2")
    ]

    result = retrieve_similar_docs(mock_vector_store, "test query")

    assert "Doc 1" in result
    assert "Doc 2" in result
    assert result == "Doc 1\n\nDoc 2"

def test_retrieve_similar_docs_with_empty_results():
    mock_vector_store = MagicMock()
    mock_vector_store.as_retriever.return_value.invoke.return_value = []

    result = retrieve_similar_docs(mock_vector_store, "irrelevant query")
    assert result == ""