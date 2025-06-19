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


_handler = logging.StreamHandler(sys.stdout)
_handler.setFormatter(JsonFormatter())

logging.basicConfig(level=logging.INFO, handlers=[_handler])
logger = logging.getLogger("genai")
