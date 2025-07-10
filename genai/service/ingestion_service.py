from typing import List
from uuid import uuid4
from time import perf_counter

from langchain_qdrant import QdrantVectorStore
from langchain_core.documents import Document
from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter

from logger import logger
from metrics import file_ingested_counter, file_ingestion_duration


def _load_document(file_path: str) -> List[Document]:
    """Load a PDF document from the given file path"""
    loader = PyPDFLoader(file_path)
    docs = loader.load()
    return docs


def _chunk_documents(docs: List[Document], filename: str) -> List[Document]:
    """Chunk documents into smaller pieces for
    better processing and storage."""
    text_splitter = RecursiveCharacterTextSplitter(
        chunk_size=1000,
        chunk_overlap=100
    )
    chunked_docs = text_splitter.split_documents(docs)
    return [
        Document(
            page_content=chunk.page_content,
            metadata={
                **chunk.metadata,
                "source": filename
                }
        )
        for chunk in chunked_docs
    ]


def _store_documents(vector_store: QdrantVectorStore, docs: List[Document]):
    """Store all chunks in vector database"""
    uuids = [str(uuid4()) for _ in range(len(docs))]
    vector_store.add_documents(docs, ids=uuids)


def ingest(vector_store: QdrantVectorStore, file_path: str, filename: str):
    """Ingestion method to load, chunk, and store pdf data"""
    start_time = perf_counter()

    unchunked_docs = _load_document(file_path)
    logger.info("Documents are loaded for file %s", filename)

    chunked_docs = _chunk_documents(unchunked_docs, filename)
    logger.info("Documents are chunked for file %s", filename)

    _store_documents(vector_store, chunked_docs)
    logger.info(
        "Ingested %s chunks into Qdrant for file %s.",
        len(chunked_docs),
        filename
    )

    duration = perf_counter() - start_time
    file_ingestion_duration.observe(duration)
    file_ingested_counter.inc()
    logger.info("File ingestion duration: %.2f seconds", duration)
