from fastapi import FastAPI
from prometheus_fastapi_instrumentator import Instrumentator
from routes.routes import router


app = FastAPI(
    title="LLM Recipe Service",
    description="Service that generates recipes based on "
                "given ingredients using an LLM",
    version="1.0.0"
)

Instrumentator().instrument(app).expose(app)

app.include_router(router, prefix="/genai")
