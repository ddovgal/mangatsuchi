package ua.ddovgal.mangamonitoring.exception;

/**
 * Throws to indicate that provider didn't successfully perform operation, or result of that operation could not be consumed.
 */
public class MangaProviderException extends ApplicationException {

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
