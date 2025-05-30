from abc import ABC


class BaseVDB(ABC):
    """Abstract base class for vector databases."""
    def get_vector_database(self, host: str):
        """Returns the vector database instance."""

    def create_vector_storage(self, collection_name: str): 
        """Creates a vector storage with the given collection name."""

    def delete_vector_storage(self, collection_name: str):
        """Deletes the vector storage with the given collection name."""
