package ua.ddovgal.trackerkun.exception;

import java.util.UUID;

import lombok.Getter;

/**
 * Throws to indicate that ID, provided as a parameter for some method is invalid an there is no requested data with such ID.
 */
public class InvalidIdProvidedException extends RuntimeException {

    /**
     * The invalid provided ID of requested data.
     */
    @Getter
    private final UUID providedId;

    /**
     * Constructs a new exception with the specified invalid id and detail message.
     *
     * @param providedId the invalid provided ID of requested data.
     * @param message    the detail message.
     */
    public InvalidIdProvidedException(UUID providedId, String message) {
        super(message);
        this.providedId = providedId;
    }
}
