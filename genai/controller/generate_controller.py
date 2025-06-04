from flask import Blueprint, jsonify, request
import os
import logging
from werkzeug.utils import secure_filename

from rag.ingestion_pipeline import IngestionPipeline
from vector_database.qdrant_vdb import QdrantVDB

# Set Logging
logging.getLogger().setLevel(logging.INFO)

generate_bp = Blueprint('generate', __name__)


@generate_bp.route('/api/upload', methods=['POST'])
def upload_file():
    """API endpoint for uploading a file to ingest it"""
    logging.info("Upload API endpoint is called")
    file = request.files.get('file')
    if not file:
        logging.info("File can not be found")
        return jsonify({"error": "No file provided"}), 400

    # Store in tmp
    file_filename = secure_filename(file.filename)
    # normalize the filename
    filename = os.path.basename(file_filename).lower().strip()
    file_path = os.path.join("/tmp", filename)
    file.save(file_path)
    logging.info("File name %s", filename)

    try:
        collection_name = "recipes"
        # Initialize vector database
        qdrant = QdrantVDB()
        # Check if the file already in the collection
        if (qdrant.client.collection_exists(collection_name)
                and qdrant.collection_contains_file(
                qdrant.client, collection_name, filename
        )):
            logging.info("File already exists in qdrant")
            return jsonify(
                {"message": f"File '{filename}' already uploaded."}
            ), 200
        # Get vector store
        vector_store = qdrant.create_and_get_vector_storage(
            collection_name
        )
        # Create ingestion pipeline
        ingestion_pipeline = IngestionPipeline(
            vector_store=vector_store
        )
        # ingest the file
        ingestion_pipeline.ingest(file_path, filename)
        logging.info("Ingestion process complete")
        return (jsonify(
            {"message": "File processed successfully."}),
                200)

    except Exception as e:
        logging.info(
            "Exception occurred while processing file with error: {}".format(e)
        )
        return jsonify({"error": str(e)}), 500

    finally:
        # Remove the file after we are done with it
        logging.info("Removing the temporary file")
        os.remove(file_path)


@generate_bp.route('/api/generate', methods=['POST'])
def generate():
    return jsonify({'output': 'Hello World!'})
