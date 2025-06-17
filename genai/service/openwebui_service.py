import requests

from config import Config
from logger import logger

BASE_URL = Config.base_url


def generate_response(model_name: str, prompt: str):
    """Making a POST request to the respective endpoint for
    response generation by an LLM"""
    url = f"{BASE_URL}/api/chat/completions"

    headers = {
        "Authorization": f"Bearer {Config.api_openwebui}",
        "Content-Type": "application/json"
    }

    payload = {
        "model": model_name,
        "messages": [
            {
                "role": "user",
                "content": prompt
                }
            ]
    }

    try:
        logger.info("Request to LLM is sent")
        response = requests.post(
            url,
            json=payload,
            headers=headers,
            timeout=120
        )
        response.raise_for_status()
        logger.info("Response from LLM is received")
        return response.json()["choices"][0]["message"]["content"]

    except requests.exceptions.HTTPError as e:
        logger.error("HTTP error from LLM server: %s", e)
        raise RuntimeError(
            f"HTTP error from LLM server: {e}, {response.status_code})"
            ) from e
    except requests.exceptions.Timeout as e:
        logger.error("Request to LLM timed out: %s", e)
        raise RuntimeError(f"Request to LLM timed out: {e}") from e
    except requests.exceptions.RequestException as e:
        logger.error("Request to LLM failed: %s", e)
        raise RuntimeError(f"Request to LLM failed: {e}") from e
