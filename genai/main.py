from fastapi import FastAPI
from prometheus_fastapi_instrumentator import Instrumentator
from routes.routes import router
import logging
import json
import sys


class JsonFormatter(logging.Formatter):
    def format(self, record):
        log_record = {
            "timestamp": self.formatTime(record, self.datefmt),
            "level": record.levelname,
            "logger": record.name,
            "message": record.getMessage(),
        }
        if record.exc_info:
            log_record["exception"] = self.formatException(record.exc_info)
        return json.dumps(log_record)

handler = logging.StreamHandler(sys.stdout)
handler.setFormatter(JsonFormatter())
logging.basicConfig(level=logging.INFO, handlers=[handler])

app = FastAPI(
    title="LLM Recipe Service",
    description="Service that generates recipes based on given ingredients using an LLM",
    version="1.0.0"
)

Instrumentator().instrument(app).expose(app)

app.include_router(router, prefix="/genai")