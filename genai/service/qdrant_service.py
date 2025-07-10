from rag.ingestion_pipeline import IngestionPipeline
from vector_database.qdrant_vdb import QdrantVDB
from logger import logger


# Set vector database
qdrant = QdrantVDB()


def file_already_uploaded(collection_name: str, filename: str) -> bool:
    """
    Checks if a file has already been uploaded to the vector database.
    This function is used to avoid duplicate uploads of the same file.
    """
    if (
        qdrant.client.collection_exists(collection_name)
        and qdrant.collection_contains_file(
            qdrant.client,
            collection_name,
            filename
        )
    ):
        return True
    return False


def get_vector_store(collection_name: str):
    """
    Returns the vector store for the specified collection name.
    This function is used to retrieve the vector store instance for a specific
    collection in the vector database.
    """
    return qdrant.create_and_get_vector_storage(collection_name)


def collection_already_exists(collection_name: str) -> bool:
    """
    Checks if a collection already exists in the vector database.
    This function is used to avoid creating duplicate collections.
    """
    if qdrant.client.collection_exists(collection_name):
        logger.info(
            "Collection already exists in qdrant: %s",
            collection_name,
        )
        return True
    return False


def ingest_file(collection_name: str, file_path: str, filename: str):
    """
    Ingests a file into the vector database.
    This function is used to process and store the file content in the vector
    database for later retrieval.
    """
    pipeline = IngestionPipeline(
        vector_store=get_vector_store(collection_name)
        )
    pipeline.ingest(file_path, filename)
