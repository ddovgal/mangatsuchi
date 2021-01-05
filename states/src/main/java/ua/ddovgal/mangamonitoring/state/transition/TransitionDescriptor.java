package ua.ddovgal.mangamonitoring.state.transition;

import ua.ddovgal.mangamonitoring.domain.Privilege;
import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.StateMachine;
import ua.ddovgal.mangamonitoring.state.exception.TransitionExecutionException;

/**
 * Describes {@link StateMachine} transition.
 * <pre>
 * Specifies
 *  - departure and arrival states;
 *  - priority;
 *  - minimal required privilege of the impact initiator;
 *  - suitability condition;
 *  - action which will be performed while transiting and which results a data-filled arrival state;
 *  - way to build impact result;
 * </pre>
 *
 * @param <D> type of the departure state.
 * @param <A> type of the arrival state.
 * @param <I> type of the impact.
 * @param <R> type of the impact result.
 *
 * @see StateMachine for the state machine a general concept.
 * @see State for the state definition details.
 * @see Impact for the impact definition details.
 */
public interface TransitionDescriptor<D extends State, A extends State, I extends Impact, R> {

    /**
     * Default and, one might say, the lowest valid transition priority.
     */
    int NORMAL_PRIORITY = 0;

    /**
     * Returns the starting, departure state class.
     *
     * @return the starting, departure state class.
     */
    Class<D> getDepartureState();

    /**
     * Returns the ending, arrival state class.
     *
     * @return the ending, arrival state class.
     */
    Class<A> getArrivalState();

    /**
     * Returns priority of the transition.
     *
     * @return priority of the transition.
     */
    int getPriority();

    /**
     * Returns minimal required privilege the impact initiator required to have to make the transition.
     *
     * @return minimal required privilege the impact initiator required to have to make the transition.
     */
    Privilege getRequiredPrivilege();

    /**
     * Checks if the transition is suitable to make with the provided {@code departure} and {@code impact}.
     *
     * @param departure state to check transition suitability.
     * @param impact    impact to check transition suitability.
     *
     * @return {@code true} if transition is suitable to make with the provided {@code departure} and {@code impact}, or else {@code false}.
     */
    boolean isSuitable(D departure, I impact);

    /**
     * Performs actual transition action and results in a new arrived state.
     * <p/>
     * Since this method is created for the whole transition, it covers all possible steps that need to be done starting from departure,
     * ending with the arrival. Hence, actual implementations could leverage this method not only for providing the arrival state but also
     * for performing any side actions before that.
     * <p/>
     * But there is also a downside... Implementation of this method MUST be atomic. Meaning it must either succeeds completely, or fails
     * completely. Here, for the case of failure, it's extremely important that no irreversible idempotent (which changes the external
     * state) actions are made. This requirement is due to the fact that for such a case, the state of the machine will not change, it will
     * remain in exactly the same state. And then, on the next impact handling iteration, it's most likely that the failed transition will
     * be again elected and be tried to make again. This means that an action that has already been made will be repeated, and because of
     * its idempotence most likely that it shouldn't be made again.
     * <br>
     * If implementation couldn't provide an atomicity, but all its actions have rollbacks, then implementation must foresee and apply those
     * rollbacks for exceptional cases.
     * <br>
     * If implementation couldn't provide an atomicity, and it has irreversible idempotent actions... then it's bad news, those cases are up
     * to you. The main problem that needs to be solved is "how not to repeat the actions already performed". The possible way that I see,
     * is to extend {@link TransitionExecutionException} with some "actions that left" stuff and use {@link StateMachine} implementation
     * that will catch exceptions and handle it in some way. Possibly for those catch cases, you could change the state to some
     * "PartiallyCompletedState" that in some way will tell which actions need to skip in the next run try.
     * <br>
     * Or another possible approach is to have intermediate sub-states, which will be updated during the transition, by transition itself.
     * Those sub-states will serve like a checkpoints or markers od done actions. Unfortunately, this is an open question.
     *
     * @param departure machine current state.
     * @param impact    external impact.
     *
     * @return arrived state with filled internal data.
     *
     * @throws TransitionExecutionException if for any possible case transition can't be completed successfully.
     */
    A execute(D departure, I impact) throws TransitionExecutionException;

    /**
     * Builds impact result from the provided already departed state, just arrived state and impact. This method is intended to be used only
     * after the {@link #execute(State, Impact)} method has completed, using its result as the {@code arrival} parameter.
     *
     * @param departure already departed state, where state machine was before the transition.
     * @param arrival   just arrived, new current state.
     * @param impact    external impact caused the transition.
     *
     * @return impact result.
     */
    R buildImpactResult(D departure, A arrival, I impact);
}
