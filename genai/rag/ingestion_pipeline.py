import logging
from typing import List
from uuid import uuid4

from langchain_qdrant import QdrantVectorStore
from langchain_core.documents import Document
from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter

# Set Logging
logging.getLogger().setLevel(logging.INFO)

class IngestionPipeline:
    """Ingestion Pipeline Class for the RAG Orchestrator"""

    def __init__(self, vector_store: QdrantVectorStore):
        self.vector_store = vector_store

    def load_document(self, file_path: str) -> List[Document]:
        """Load a PDF document from the given file path"""
        loader = PyPDFLoader(file_path)
        docs = loader.load()
        return docs

    def chunk_documents(self, docs: List[Document],
                        filename: str) -> List[Document]:
        """Chunk documents into smaller pieces for
        better processing and storage."""
        text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=1000,
            chunk_overlap=100
        )
        chunked_docs = text_splitter.split_documents(docs)
        return [
            Document(page_content=chunk.page_content,
                     metadata={**chunk.metadata, "source": filename})
            for chunk in chunked_docs
        ]

    def store_documents(self, docs: List[Document]):
        """Store all chunks in vector database"""
        uuids = [str(uuid4()) for _ in range(len(docs))]
        self.vector_store.add_documents(docs, ids=uuids)

    def ingest(self, file_path: str, filename: str):
        """Ingestion method to load, chunk, and store pdf data"""
        unchunked_docs = self.load_document(file_path)
        logging.info("Documents are loaded for file %s", filename)
        chunked_docs = self.chunk_documents(unchunked_docs, filename)
        logging.info("Documents are chunked for file %s", filename)
        self.store_documents(chunked_docs)
        logging.info("Ingested %s chunks into Qdrant for file %s.",
                     len(chunked_docs), filename)
