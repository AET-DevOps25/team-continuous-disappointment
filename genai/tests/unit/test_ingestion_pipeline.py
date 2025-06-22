from unittest.mock import MagicMock, patch
from uuid import UUID
from rag.ingestion_pipeline import IngestionPipeline
from langchain_core.documents import Document


def test_load_document_returns_documents():
    with patch("rag.ingestion_pipeline.PyPDFLoader") as mock_loader:
        mock_loader.return_value.load.return_value = [
            Document(page_content="Test")
        ]
        pipeline = IngestionPipeline(vector_store=MagicMock())
        docs = pipeline.load_document("fake_path.pdf")
        assert isinstance(docs, list)
        assert isinstance(docs[0], Document)
        assert docs[0].page_content == "Test"


def test_chunk_documents_returns_chunks():
    pipeline = IngestionPipeline(vector_store=MagicMock())
    dummy_doc = Document(
        page_content="This is a long text. " * 100,
        metadata={}
    )
    chunks = pipeline.chunk_documents([dummy_doc], filename="sample.pdf")
    assert isinstance(chunks, list)
    assert all(isinstance(doc, Document) for doc in chunks)
    assert all(doc.metadata["source"] == "sample.pdf" for doc in chunks)


def test_store_documents_calls_add_documents_with_uuids():
    mock_vector_store = MagicMock()
    pipeline = IngestionPipeline(vector_store=mock_vector_store)
    docs = [Document(page_content="Chunk", metadata={}) for _ in range(3)]
    pipeline.store_documents(docs)
    args, kwargs = mock_vector_store.add_documents.call_args
    passed_docs = args[0]
    passed_ids = kwargs["ids"]
    assert len(passed_docs) == 3
    assert len(passed_ids) == 3
    assert all(UUID(uid) for uid in passed_ids)


def test_ingest_calls_all_steps():
    pipeline = IngestionPipeline(vector_store=MagicMock())

    with patch.object(pipeline, "load_document") as mock_load, \
         patch.object(pipeline, "chunk_documents") as mock_chunk, \
         patch.object(pipeline, "store_documents") as mock_store, \
         patch("rag.ingestion_pipeline.logger") as mock_logger, \
         patch("rag.ingestion_pipeline.file_ingestion_duration.observe"), \
         patch("rag.ingestion_pipeline.file_ingested_counter.inc"):

        mock_load.return_value = [Document(page_content="Doc")]
        mock_chunk.return_value = [Document(page_content="Chunk")]

        pipeline.ingest("test.pdf", "testfile.pdf")

        mock_load.assert_called_once_with("test.pdf")
        mock_chunk.assert_called_once()
        mock_store.assert_called_once()
        mock_logger.info.assert_any_call(
            "Documents are loaded for file %s",
            "testfile.pdf"
        )
