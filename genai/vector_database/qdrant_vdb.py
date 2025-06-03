import logging

from .base_vdb import BaseVDB
from qdrant_client import QdrantClient
from qdrant_client.models import VectorParams, Distance
from langchain_qdrant import QdrantVectorStore
from langchain_openai import OpenAIEmbeddings

from config import Config

# Set Logging
logging.getLogger().setLevel(logging.INFO)


class QdrantVDB(BaseVDB):
    """Qdrant vector database implementation."""
    def __init__(self):
        self.host = "http://qdrant-service:6333"
        self.client = self.get_vector_database(self.host)
        logging.info("Qdrant vector database is initialized.")

    def get_vector_database(self, host: str):
        """Returns the Qdrant vector database instance."""
        return QdrantClient(url=host)

    def create_and_get_vector_storage(self, collection_name: str):
        """Creates a vector storage with the given
        collection name in Qdrant."""

        logging.info(
            "Checking if vector store exists in collection %s",
            collection_name
        )
        if self.client.collection_exists(collection_name):
            logging.info("Collection %s already exists, using it.",
                         collection_name)
        else:
            logging.info("Collection %s does not exist, creating it.",
                         collection_name)

            self.client.create_collection(
                collection_name=collection_name,
                vectors_config=VectorParams(
                    size=1536,
                    distance=Distance.COSINE
                    ),
            )

        logging.info("Creating an embedding model for the collection %s",
                     collection_name)
        
        embeddings = OpenAIEmbeddings(
            model="text-embedding-3-small", openai_api_key=Config.api_key_openai
        )

        logging.info("An embedding model is created for the collection %s",
                     collection_name)

        vector_store = QdrantVectorStore(
            client=self.client,
            collection_name=collection_name,
            embedding=embeddings,
        )
        logging.info("Vector store is created for the collection %s",
                     collection_name)

        return vector_store

    def delete_collection(self, collection_name: str):
        """Deletes the given collection in the vector storage."""
        if not self.client.check_collection(collection_name):
            logging.info("Collection %s does not exist, nothing to delete.",
                         collection_name)
            return

        logging.info("Deleting the collection %s",
                     collection_name)
        self.client.delete_collection(collection_name)
    
