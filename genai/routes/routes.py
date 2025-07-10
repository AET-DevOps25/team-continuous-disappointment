from fastapi import (
    APIRouter,
    UploadFile,
    File,
    HTTPException,
    Request,
    Depends
)
from fastapi.responses import JSONResponse
import os

from logger import logger
from time import perf_counter

from service.llm_service import generate_response

from service.qdrant_service import (
    file_already_uploaded,
    collection_already_exists,
    ingest_file
)

from service.rag_service import (
    retrieve_similar_docs,
    prepare_prompt,
    process_raw_messages
)
from service.auth_service import get_current_user, UserInfo
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


@router.post("/upload")
async def upload_file(
    file: UploadFile = File(...),
    current_user: UserInfo = Depends(get_current_user)
):
    file_upload_request_counter.inc()
    start_time = perf_counter()
    logger.info(
        "Upload endpoint is called in genai for the file %s by user %s",
        file.filename,
        current_user.username
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

        collection_name = f"recipes_{current_user.user_id}"
        if file_already_uploaded(collection_name, filename):
            logger.info(
                "File already exists in qdrant for user %s",
                current_user.username
                )
            return {"message": f"File '{filename}' already uploaded."}

        ingest_file(collection_name, file_path, filename)
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
    if "query" not in body or "messages" not in body or "user_id" not in body:
        logger.error(
            "Missing 'query', 'messages', or 'user_id' in the request body"
            )
        raise HTTPException(
            status_code=400,
            detail="Missing 'query', 'messages', or 'user_id'"
        )

    query = body["query"]
    messages_raw = body["messages"]
    user_id = body["user_id"]
    collection_name = f"recipes_{user_id}"

    logger.info("Generate endpoint called for user_id: %s", user_id)

    try:
        retrieved_docs = ""
        if collection_already_exists(collection_name):
            logger.info(
                "Collection %s already exists for user_id %s",
                collection_name,
                user_id
            )

            retrieved_docs = retrieve_similar_docs(collection_name, query)
            logger.info("Similar docs retrieved from the vector store")

        messages = process_raw_messages(messages_raw)
        logger.info("Raw messages are processed for prompt preparation")

        prompt = prepare_prompt(
            query,
            retrieved_docs,
            messages
        )
        logger.info("Prompt is prepared")

        response = generate_response(prompt)
        logger.info("Response is generated")

        generation_successfully_counter.inc()

        return JSONResponse(content={"response": response})

    except Exception as e:
        logger.error("Generation is failed. Error: %s", str(e), exc_info=True)
        generation_errors_counter.inc()
        raise HTTPException(status_code=500, detail=str(e))

    finally:
        duration = perf_counter() - start_time
        generation_duration.observe(duration)
        logger.info("Response generation duration: %.2f seconds", duration)
