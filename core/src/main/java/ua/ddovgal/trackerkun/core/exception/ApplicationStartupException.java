package ua.ddovgal.trackerkun.core.exception;

import ua.ddovgal.trackerkun.exception.ApplicationException;

/**
 * Throws to indicate that startup fails at some phase, some component couldn't be started.
 */
public class ApplicationStartupException extends ApplicationException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public ApplicationStartupException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public ApplicationStartupException(String message, Throwable cause) {
        super(message, cause);
    }
}
