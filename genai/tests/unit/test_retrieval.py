from unittest.mock import MagicMock, patch
from langchain_core.documents import Document
from service.rag_service import retrieve_similar_docs


@patch('service.rag_service.get_vector_store')
def test_retrieve_similar_docs_returns_joined_string(mock_get_vector_store):
    collection_name = "test_collection"
    mock_vector_store = MagicMock()
    mock_retriever = MagicMock()
    mock_retriever.invoke.return_value = [
        Document(page_content="Doc 1"),
        Document(page_content="Doc 2")
    ]
    mock_vector_store.as_retriever.return_value = mock_retriever
    mock_get_vector_store.return_value = mock_vector_store

    result = retrieve_similar_docs(collection_name, "test query")

    assert "Doc 1" in result
    assert "Doc 2" in result
    assert result == "Doc 1\n\nDoc 2"


@patch('service.rag_service.get_vector_store')
def test_retrieve_similar_docs_with_empty_results(mock_get_vector_store):
    collection_name = "test_collection"
    mock_vector_store = MagicMock()
    mock_retriever = MagicMock()
    mock_retriever.invoke.return_value = []
    mock_vector_store.as_retriever.return_value = mock_retriever
    mock_get_vector_store.return_value = mock_vector_store

    result = retrieve_similar_docs(collection_name, "irrelevant query")
    assert result == ""
