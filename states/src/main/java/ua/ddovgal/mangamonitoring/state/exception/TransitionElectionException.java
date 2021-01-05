package ua.ddovgal.mangamonitoring.state.exception;

import java.util.List;

import lombok.Getter;

import ua.ddovgal.mangamonitoring.exception.ApplicationException;
import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

/**
 * Throws to indicate that transition couldn't be determined for sure for the provided {@link #impact} while being in {@link #departure}
 * state. Actually there are only two possible cases for that: there are no transitions that could be made, or there is more than one.
 */
@Getter
public class TransitionElectionException extends ApplicationException {

    private static final String ZERO_ELECTED_MESSAGE = "There are no transitions able to make";
    private static final String MANY_ELECTED_MESSAGE = "There is more than one transition able to make";

    private final Impact impact;
    private final State departure;
    private final List<TransitionDescriptor<?, ?, ?, ?>> electedDescriptors;

    /**
     * Constructs a new exception with the specified {@code impact}, {@code departure} by which determination was attempted, and the result
     * list of {@code electedDescriptors}.
     *
     * @param impact             impact used for transition determination.
     * @param departure          state used for transition determination.
     * @param electedDescriptors transition descriptors that were elected.
     *
     * @throws IllegalArgumentException if {@code electedDescriptors} has exactly one descriptor.
     */
    public TransitionElectionException(Impact impact, State departure, List<TransitionDescriptor<?, ?, ?, ?>> electedDescriptors) {
        super(selectMessageForElectedAmount(electedDescriptors.size()));
        this.departure = departure;
        this.impact = impact;
        this.electedDescriptors = electedDescriptors;
    }

    private static String selectMessageForElectedAmount(int amount) {
        if (amount == 0) {
            return ZERO_ELECTED_MESSAGE;
        }
        if (amount == 1) {
            throw new IllegalArgumentException("Provided electedDescriptors couldn't have exactly one descriptor");
        }
        // if amount > 1
        return MANY_ELECTED_MESSAGE;
    }

    public final boolean isZeroElected() {
        return electedDescriptors.isEmpty();
    }
}
