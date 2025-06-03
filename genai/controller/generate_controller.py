from flask import Blueprint, jsonify, request
import os
import logging
from werkzeug.utils import secure_filename

from rag.ingestion_pipeline import IngestionPipeline
from vector_database.qdrant_vdb import QdrantVDB

from PyPDF2 import PdfReader

generate_bp = Blueprint('generate', __name__)
# Set Logging
logging.getLogger().setLevel(logging.INFO)

@generate_bp.route('/api/upload', methods=['POST'])
def upload_file():
    """API endpoint for uploading a file to ingest it"""
    file = request.files.get('file')
    if not file:
        return jsonify({"error": "No file provided"}), 400

    # Store in tmp
    filename = secure_filename(file.filename)
    file_path = os.path.join("/tmp", filename)
    file.save(file_path)

    try:
        # Initialize vector database
        qdrant = QdrantVDB()
        # Get vector store
        vector_store = qdrant.create_and_get_vector_storage("recipes")
        # Create ingestion pipeline
        ingestion_pipeline = IngestionPipeline(vector_store=vector_store)
        # ingest the file
        ingestion_pipeline.ingest(file_path)
        return jsonify({"message": "File processed successfully."}), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

    finally:
        # Remove the file after we are done with it
        os.remove(file_path)


@generate_bp.route('/api/generate', methods=['POST'])
def generate():
    return jsonify({'output': 'Hello World!'})
