package ua.ddovgal.trackerkun.exception;

/**
 * Throws to indicate that provider didn't successfully performed operation, or result of that operation could not be consumed.
 */
public class MangaProviderException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public MangaProviderException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public MangaProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}