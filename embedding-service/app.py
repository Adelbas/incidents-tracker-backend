"""
Embedding microservice for event categorization.

Runs the multilingual e5-small sentence-embedding model and exposes a small HTTP API.
sentence-transformers applies the correct mean pooling and L2 normalization for the model,
so the returned vectors can be compared directly with cosine similarity (== dot product).

The asymmetric e5 prefixes ("query: " / "passage: ") are added by the caller (location-tracker),
so this service embeds the inputs verbatim.
"""
import logging
import os

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("embedding-service")

MODEL_NAME = os.getenv("EMBEDDING_MODEL", "intfloat/multilingual-e5-small")

logger.info("Loading embedding model %s ...", MODEL_NAME)
model = SentenceTransformer(MODEL_NAME)
logger.info("Embedding model loaded")

app = FastAPI(title="Embedding Service", version="1.0")


class EmbedRequest(BaseModel):
    inputs: list[str]


class EmbedResponse(BaseModel):
    embeddings: list[list[float]]


@app.get("/health")
def health() -> dict:
    return {"status": "ok", "model": MODEL_NAME}


@app.post("/embed", response_model=EmbedResponse)
def embed(request: EmbedRequest) -> EmbedResponse:
    try:
        vectors = model.encode(
            request.inputs,
            normalize_embeddings=True,
            convert_to_numpy=True,
        )
        return EmbedResponse(embeddings=[vector.tolist() for vector in vectors])
    except Exception as exc:  # surface the real cause instead of a bare 500
        logger.exception("Embedding failed")
        raise HTTPException(status_code=500, detail=str(exc))
