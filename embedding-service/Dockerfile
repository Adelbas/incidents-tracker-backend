# syntax=docker/dockerfile:1
FROM python:3.11-slim

WORKDIR /app

ENV PIP_DEFAULT_TIMEOUT=1000 \
    PIP_RETRIES=10 \
    HF_HUB_DOWNLOAD_TIMEOUT=120

# wget is used to fetch the large torch wheel with resume support — robust on unstable networks
# where the PyTorch CDN connection drops mid-download.
RUN apt-get update \
 && apt-get install -y --no-install-recommends wget ca-certificates \
 && rm -rf /var/lib/apt/lists/*

# CPU-only PyTorch wheel (linux x86_64, cp311). Downloaded with resume + many retries; the partial file is
# kept in the BuildKit cache between attempts, so a dropped connection just resumes instead of starting over.
ARG TORCH_WHEEL="torch-2.2.2%2Bcpu-cp311-cp311-linux_x86_64.whl"
ARG TORCH_WHEEL_NAME="torch-2.2.2+cpu-cp311-cp311-linux_x86_64.whl"
RUN --mount=type=cache,target=/wheels \
    wget --continue --tries=30 --timeout=60 --waitretry=5 --retry-connrefused \
      -O /wheels/torch.whl \
      "https://download.pytorch.org/whl/cpu/${TORCH_WHEEL}" \
 && cp /wheels/torch.whl "/tmp/${TORCH_WHEEL_NAME}" \
 && pip install --no-cache-dir "/tmp/${TORCH_WHEEL_NAME}" \
 && rm -f "/tmp/${TORCH_WHEEL_NAME}"

COPY requirements.txt .
RUN --mount=type=cache,target=/root/.cache/pip \
    pip install -r requirements.txt

COPY app.py .

# Bake the model into the image so the container starts offline.
ARG EMBEDDING_MODEL=intfloat/multilingual-e5-small
ENV EMBEDDING_MODEL=${EMBEDDING_MODEL}
RUN python -c "from sentence_transformers import SentenceTransformer; SentenceTransformer('${EMBEDDING_MODEL}')"

EXPOSE 8000

CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
