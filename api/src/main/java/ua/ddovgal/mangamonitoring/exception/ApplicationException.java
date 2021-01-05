package ua.ddovgal.mangamonitoring.exception;

/**
 * Top level or root application-scoped exception that describes generic failed situation.
 */
public class ApplicationException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
