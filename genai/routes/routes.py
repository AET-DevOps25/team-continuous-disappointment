from fastapi import APIRouter, UploadFile, File, HTTPException, Request
from fastapi.responses import JSONResponse
import os

# from config import Config
from logger import logger
from time import perf_counter

from vector_database.qdrant_vdb import QdrantVDB
from rag.ingestion_pipeline import IngestionPipeline
from rag.llm.chat_model import ChatModel
# from rag.llm.cloud_chat_model import CloudLLM
from service.rag_service import (
    retrieve_similar_docs,
    prepare_prompt,
    process_raw_messages
)
from metrics import (
    file_upload_request_counter,
    file_upload_successfully_counter,
    file_upload_errors_counter,
    file_upload_duration,
    generation_request_counter,
    generation_successfully_counter,
    generation_errors_counter,
    generation_duration
)


router = APIRouter()

# Set vector database
qdrant = QdrantVDB()

# Set chat model for local llm models
# Make calls to local models in openwebui hosted by the university
llm = ChatModel(model_name="llama3.3:latest")

# Alternatively, we can switch to a chat model based on cloud models as well
# If you want to use other cloud models, please adjust model_name,
# model_provider, and api key
# accordingly

# Examples:
# llm_cloud_anthropic = CloudLLM(
#     model_name="claude-3-sonnet-20240229",
#     model_provider="anthropic",
#     api_key=Config.api_key_anthropic,
# )
# llm_cloud_openai = CloudLLM(
#     model_name="gpt-4-1106-preview",
#     model_provider="openai",
#     api_key=Config.api_key_openai,
# )
#
# llm_cloud_mistral = CloudLLM(
#     model_name="mistral-medium",
#     model_provider="mistral",
#     api_key=Config.api_key_mistral,
# )

# If no parameters are provided, the default cloud model will be openai.
# If a cloud model is wanted, please remove the comment
# for package import "CloudLLM"

# Example:
#llm = CloudLLM() # same as llm_cloud_openai



@router.post("/upload")
async def upload_file(file: UploadFile = File(...)):
    file_upload_request_counter.inc()
    start_time = perf_counter()
    logger.info(
        "Upload endpoint is called in genai for the file %s",
        file.filename
    )

    if not file.filename.endswith(".pdf"):
        raise HTTPException(
            status_code=400,
            detail="Invalid file type. Only PDF files are allowed."
        )

    filename = os.path.basename(file.filename).lower().strip()
    file_path = os.path.join("/tmp", filename)

    try:
        with open(file_path, "wb") as buffer:
            buffer.write(await file.read())

        collection_name = "recipes"
        if (
            qdrant.client.collection_exists(collection_name)
            and qdrant.collection_contains_file(
                qdrant.client,
                collection_name,
                filename
            )
        ):
            logger.info("File already exists in qdrant")
            return {"message": f"File '{filename}' already uploaded."}

        vector_store = qdrant.create_and_get_vector_storage(collection_name)
        ingestion_pipeline = IngestionPipeline(vector_store=vector_store)
        ingestion_pipeline.ingest(file_path, filename)

        file_upload_successfully_counter.inc()

        return {"message": "File processed successfully."}

    except Exception as e:
        logger.error("Upload is failed. Error: %s", str(e), exc_info=True)
        file_upload_errors_counter.inc()
        raise HTTPException(status_code=500, detail=str(e))

    finally:
        if os.path.exists(file_path):
            os.remove(file_path)
            logger.info("Removed temporary file")
        duration = perf_counter() - start_time
        file_upload_duration.observe(duration)
        logger.info("Upload duration: %.2f seconds", duration)


@router.post("/generate")
async def generate(request: Request):
    generation_request_counter.inc()
    start_time = perf_counter()
    logger.info("Generate endpoint is called in genai")

    body = await request.json()
    if "query" not in body or "messages" not in body:
        logger.error("Missing 'query' or 'messages' in the request body")
        raise HTTPException(
            status_code=400,
            detail="Missing 'query' or 'messages'"
        )

    query = body["query"]
    messages_raw = body["messages"]
    collection_name = "recipes"

    try:
        retrieved_docs = ""
        if qdrant.client.collection_exists(collection_name):
            vector_store = qdrant.create_and_get_vector_storage(
                collection_name
            )
            logger.info(
                "Vector store is created for the collection %s",
                collection_name
            )
            retrieved_docs = retrieve_similar_docs(vector_store, query)
            logger.info("Similar docs retrieved from the vector store")

        messages = process_raw_messages(messages_raw)
        logger.info("Raw messages are processed for prompt preparation")

        prompt = prepare_prompt(
            llm.get_system_prompt(),
            query,
            retrieved_docs,
            messages
        )
        logger.info("Prompt is prepared")

        response = llm.invoke(prompt)
        logger.info("Response is generated")

        generation_successfully_counter.inc()

        return JSONResponse(content={"response": response.content})

    except Exception as e:
        logger.error("Generation is failed. Error: %s", str(e), exc_info=True)
        generation_errors_counter.inc()
        raise HTTPException(status_code=500, detail=str(e))

    finally:
        duration = perf_counter() - start_time
        generation_duration.observe(duration)
        logger.info("Response generation duration: %.2f seconds", duration)
