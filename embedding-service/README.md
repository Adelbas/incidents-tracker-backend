# Embedding service

Контейнеризованный сервис эмбеддингов для категоризации событий. Запускает модель
`intfloat/multilingual-e5-small` (многоязычная, поддерживает русский) на CPU через
`sentence-transformers` и отдаёт нормализованные векторы по HTTP.

## API

`POST /embed`
```json
{ "inputs": ["query: горит здание", "passage: пожар, возгорание, ..."] }
```
Ответ:
```json
{ "embeddings": [[0.01, -0.02, ...], [...]] }
```

`GET /health` → `{"status":"ok","model":"intfloat/multilingual-e5-small"}`

## Запуск

Поднимается вместе с остальной инфраструктурой через корневой `docker-compose.yml`
(сервис `embedding-service`, порт 8000). Модель запекается в образ на этапе сборки,
поэтому в рантайме интернет не нужен.

`location-tracker` ходит сюда по адресу из переменной `EMBEDDING_SERVICE_URL`
(в docker-compose — `http://embedding-service:8000`). Асимметричные префиксы e5
(`query: ` / `passage: `) добавляет вызывающая сторона.

## Заметки

- `model.encode(..., normalize_embeddings=True)` — корректный mean-pooling и L2-нормализация
  для e5, поэтому косинусная близость = скалярное произведение.
- Сменить модель можно build-arg/ENV `EMBEDDING_MODEL`.
