from collections import namedtuple
from os import environ, path
from dotenv import load_dotenv

basedir = path.abspath(path.dirname(__file__))
load_dotenv(path.join(basedir, ".env"))

ConfigT = namedtuple(
    "Config",
    [
        "api_key_openai",
        "waitress"
    ],
)

Config = ConfigT(
    api_key_openai=environ.get("API_SECRET_OPENAI_MINE"),
    waitress=environ.get("USE_WAITRESS", "false").lower() == "true",
)
