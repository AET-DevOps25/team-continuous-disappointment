from pathlib import Path
from fpdf import FPDF
from service.ingestion_service import ingest
from vector_database.qdrant_vdb import QdrantVDB


# Helper method to generate a dummy pdf file with real content
def generate_sample_pdf(path: Path, text: str = "Real ingestion test."):
    pdf = FPDF()
    pdf.add_page()
    pdf.set_font("Arial", size=12)
    pdf.multi_cell(0, 10, text)
    pdf.output(str(path))


def test_real_ingestion_pipeline(tmp_path):
    collection_name = "test_collection"
    qdrant = QdrantVDB()
    # Just for testing purposes
    qdrant.host = "http://localhost:6333"
    qdrant.client = qdrant.get_vector_database(qdrant.host)

    # Ensure collection does not exists before tests
    qdrant.delete_collection(collection_name)

    # Create a dummy PDF
    pdf_path = tmp_path / "sample_test_doc.pdf"
    generate_sample_pdf(pdf_path)
    filename = pdf_path.name

    # Get vector store
    vector_store = qdrant.create_and_get_vector_storage(collection_name)

    # Ingestion
    ingest(vector_store, str(pdf_path), filename)

    found = qdrant.collection_contains_file(
        qdrant.client,
        collection_name,
        filename
    )

    assert found is True

    # Clean the vector database
    qdrant.delete_collection(collection_name)
