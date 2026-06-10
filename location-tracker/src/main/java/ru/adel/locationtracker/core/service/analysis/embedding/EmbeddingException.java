package ru.adel.locationtracker.core.service.analysis.embedding;

/**
 * Raised when the embedding model cannot be loaded or text cannot be encoded.
 */
public class EmbeddingException extends RuntimeException {

    public EmbeddingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmbeddingException(String message) {
        super(message);
    }
}
