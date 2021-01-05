package ua.ddovgal.mangamonitoring.state.election;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.StateMachine;
import ua.ddovgal.mangamonitoring.state.exception.TransitionElectionException;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

/**
 * The only job of the transition elector, as it stays from its name, is to elect exactly one transition by the provided {@code departure}
 * state and {@code impact}.
 *
 * @param <I> type of the impact.
 * @param <R> type of the impact result.
 *
 * @see StateMachine for state machine a general concept.
 */
public interface TransitionElector<I extends Impact, R> {

    /**
     * Elects exactly one transition by the provided {@code departure} (machine current) state and the {@code impact}. The return type of
     * this method isn't plain {@link TransitionDescriptor} in favour or {@link DepartureDefinedTransition} to be able to retain states'
     * type safety. Check {@link DepartureDefinedTransition} for more details and explanations.
     *
     * @param departure state to elect transition.
     * @param impact    impact to elect transition.
     *
     * @return elected transition descriptor.
     *
     * @throws TransitionElectionException if transition couldn't be determined for sure for the provided data. Check exception class for
     *                                     more details.
     * @see DepartureDefinedTransition for the reasons it was created and is used insted of plain {@code TransitionDescriptor}.
     */
    DepartureDefinedTransition<?, ?, I, R> electTransition(State departure, I impact) throws TransitionElectionException;
}
