from prometheus_client import Counter, Summary

# ----------- FILE UPLOAD METRICS ------------ #
file_upload_request_counter = Counter(
    "genai_file_upload_request_total",
    "Total number of file upload requests to the endpoint"
)

file_upload_successfully_counter = Counter(
    "genai_file_successfully_total",
    "Total number of files successfully ingested"
)

file_upload_errors_counter = Counter(
    "genai_ingestion_errors_total",
    "Total number of ingestion errors"
)

file_upload_duration = Summary(
    "genai_ingestion_duration_seconds",
    "Time taken to ingest a file"
)

# ----------- FILE INGESTION METRICS ------------ #
file_ingested_counter = Counter(
    "genai_file_ingested_total",
    "Total number of files ingested"
)

file_ingestion_duration = Summary(
    "genai_ingestion_duration_seconds",
    "Time taken to ingest a file"
)

# -------- LLM RESPONSE GENERATION METRICS ------- #
generation_request_counter = Counter(
    "genai_generation_request_total",
    "Total number of generation requests to the endpoint"
)

generation_successfully_counter = Counter(
    "genai_generation_successfully_total",
    "Total number of generated responses successfully"
)

generation_errors_counter = Counter(
    "genai_generation_errors_total",
    "Total number of generation errors"
)

generation_duration = Summary(
    "genai_generation_duration_seconds",
    "Time taken to generate a recipe response"
)
