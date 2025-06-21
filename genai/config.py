from collections import namedtuple
from os import environ, path
from dotenv import load_dotenv

basedir = path.abspath(path.dirname(__file__))
load_dotenv(path.join(basedir, ".env"))

ConfigT = namedtuple(
    "Config",
    [
        "api_key_openai",
        "api_key_anthropic",
        "api_key_mistral",
        "api_key_huggingface",
        "api_key_openwebui",
        "base_url"
    ],
)

Config = ConfigT(
    api_key_openai=environ.get("API_OPENAI"),
    api_key_anthropic=environ.get("API_ANTHROPIC", ""),
    api_key_mistral=environ.get("API_MISTRAL", ""),
    api_key_huggingface=environ.get("API_HUGGINGFACE", ""),
    api_key_openwebui=environ.get("API_OPENWEBUI"),
    base_url=environ.get("BASE_URL")
)
