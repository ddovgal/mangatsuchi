package ua.ddovgal.mangamonitoring.state.exception;

import lombok.Getter;

import ua.ddovgal.mangamonitoring.exception.ApplicationException;
import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

/**
 * Throws to indicate that transition can't be completed successfully for any possible case, is used for {@link
 * TransitionDescriptor#execute(State, Impact)} only.
 */
public class TransitionExecutionException extends ApplicationException {

    /**
     * Unique error code which identifies certain exceptional case. Used to get the explanation of happened text. It's just a code instead
     * of common string with straightway description because of texts difference for different locales. Exception also has {@link
     * #getMessage()}, but that message describes exception from the technical perspective, while {@code code} refers to the test, intended
     * to be shown to a real user.
     * <p/>
     * Proposed code format is {action that describes transition camelCase}_{serial number}. For example "downloadReport_03".
     */
    @Getter
    private final String code;

    /**
     * Constructs a new exception with the specified {@code message}.
     *
     * @param message message that describes exception from a technical perspective.
     */
    public TransitionExecutionException(String message) {
        super(message);
        code = null;
    }

    /**
     * Constructs a new exception with the specified {@code message} and error {@code code}.
     *
     * @param message message that describes exception from a technical perspective.
     * @param code    code that identifies certain exceptional case.
     */
    public TransitionExecutionException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * Constructs a new exception with the specified {@code message} and {@code cause}.
     *
     * @param message message that describes exception from a technical perspective.
     * @param cause   cause exception.
     */
    public TransitionExecutionException(String message, Throwable cause) {
        super(message, cause);
        code = null;
    }

    /**
     * Constructs a new exception with the specified {@code message}, {@code cause} and error {@code code}.
     *
     * @param message message that describes exception from a technical perspective.
     * @param cause   cause exception.
     * @param code    code that identifies certain exceptional case.
     */
    public TransitionExecutionException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }
}
