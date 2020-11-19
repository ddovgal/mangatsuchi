package ua.ddovgal.mangamonitoring.state.exception;

import lombok.Getter;

import ua.ddovgal.mangamonitoring.exception.ApplicationException;
import ua.ddovgal.mangamonitoring.state.State;

/**
 * Throws to indicate that transition couldn't be determined for provided {@link #update} while being in {@link #state}. Actually there are
 * only two possible cases for that: there are no transitions that the provided state could make, or there is more than one.
 */
public class TransitionDeterminationException extends ApplicationException {

    @Getter
    private final State state;

    @Getter
    private final Object update;

    /**
     * Constructs a new exception with the specified detail message, current state and update by which determination was attempted.
     *
     * @param message the detail message.
     * @param state   state for which was determined transition.
     * @param update  input update for transition determination.
     */
    public TransitionDeterminationException(String message, State state, Object update) {
        super(message);
        this.state = state;
        this.update = update;
    }
}
