from prometheus_client import Counter, Summary

file_ingestion_counter = Counter(
    "genai_ingested_files_total",
    "Total number of files successfully ingested"
)

ingestion_errors_total = Counter(
    "genai_ingestion_errors_total",
    "Total number of ingestion errors"
)

generation_duration = Summary(
    "genai_generation_duration_seconds",
    "Time taken to generate a recipe response"
)

generation_errors_total = Counter(
    "genai_generation_errors_total",
    "Total number of generation errors"
)