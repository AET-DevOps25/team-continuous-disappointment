import requests

from genai.config import Config

BASE_URL = "https://gpu.aet.cit.tum.de/"


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
        response = requests.post(
            url,
            json=payload,
            headers=headers,
            timeout=120
        )
        response.raise_for_status()
        return response.json()["choices"][0]["message"]["content"]

    except requests.exceptions.HTTPError as e:
        raise RuntimeError(
            f"HTTP error from LLM server: {e}, {response.status_code})"
            ) from e
    except requests.exceptions.Timeout as e:
        raise RuntimeError(f"Request to LLM timed out: {e}") from e
    except requests.exceptions.RequestException as e:
        raise RuntimeError(f"Request to LLM failed: {e}") from e
