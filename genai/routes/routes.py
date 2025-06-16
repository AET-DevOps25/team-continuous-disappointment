from fastapi import APIRouter, UploadFile, File, HTTPException, Request
from fastapi.responses import JSONResponse
import os
import logging
from vector_database.qdrant_vdb import QdrantVDB
from rag.ingestion_pipeline import IngestionPipeline
from rag.llm.chat_model import ChatModel
from service.rag_service import retrieve_similar_docs, prepare_prompt, process_raw_messages
from metrics import file_ingestion_counter, generation_duration, ingestion_errors_total, generation_errors_total

logger = logging.getLogger(__name__)
router = APIRouter()

llm = ChatModel(model_name="llama3.3:latest")
qdrant = QdrantVDB()

@router.post("/upload")
async def upload_file(file: UploadFile = File(...)):
    logger.info("Upload API endpoint is called")

    filename = os.path.basename(file.filename).lower().strip()
    file_path = os.path.join("/tmp", filename)

    try:
        with open(file_path, "wb") as buffer:
            buffer.write(await file.read())

        collection_name = "recipes"
        if (qdrant.client.collection_exists(collection_name) and
            qdrant.collection_contains_file(qdrant.client, collection_name, filename)):
            logger.info("File already exists in qdrant")
            return {"message": f"File '{filename}' already uploaded."}

        vector_store = qdrant.create_and_get_vector_storage(collection_name)
        ingestion_pipeline = IngestionPipeline(vector_store=vector_store)
        ingestion_pipeline.ingest(file_path, filename)
        file_ingestion_counter.inc()

        return {"message": "File processed successfully."}

    except Exception as e:
        logger.exception("Error in file upload")
        ingestion_errors_total.inc()
        raise HTTPException(status_code=500, detail=str(e))

    finally:
        if os.path.exists(file_path):
            os.remove(file_path)
            logger.info("Removed temporary file")

@router.post("/generate")
@generation_duration.time()
async def generate(request: Request):
    body = await request.json()
    if "query" not in body or "messages" not in body:
        raise HTTPException(status_code=400, detail="Missing 'query' or 'messages'")

    query = body["query"]
    messages_raw = body["messages"]
    collection_name = "recipes"

    try:
        retrieved_docs = ""
        if qdrant.client.collection_exists(collection_name):
            vector_store = qdrant.create_and_get_vector_storage(collection_name)
            retrieved_docs = retrieve_similar_docs(vector_store, query)

        messages = process_raw_messages(messages_raw)
        prompt = prepare_prompt(
            llm.get_system_prompt(),
            query,
            retrieved_docs,
            messages
        )

        response = llm.invoke(prompt)

        return JSONResponse(content={"response": response.content})

    except Exception as e:
        logger.exception("Error in generate endpoint")
        generation_errors_total.inc()
        raise HTTPException(status_code=500, detail=str(e))