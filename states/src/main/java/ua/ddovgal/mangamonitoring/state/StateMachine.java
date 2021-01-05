package ua.ddovgal.mangamonitoring.state;

import ua.ddovgal.mangamonitoring.state.exception.TransitionElectionException;
import ua.ddovgal.mangamonitoring.state.exception.TransitionExecutionException;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

/**
 * Describes system similar to "Finite state machine" but with small differences. Provides simple hi-level way for the interaction, which is
 * presented as some kind of handler of the on-system external impact.
 * <p/>
 * This is the entry point for {@code states} "engine" interaction.
 * <pre>
 * The idea behind the {@code StateMachine} is that:
 *  - there is some kind of system that has (could be described by) the finite number of {@code state}s;
 *  - each state has its own specific internal data;
 *  - machine changes its state only due to an external {@code impact} by making {@code transition}s;
 *  - transition can be made only when
 *     - the system is in the specified {@code required state};
 *       (Hence each state gets its own set of the only possible to make transitions)
 *     - and wherein its specified {@code suitability condition} is satisfied;
 *  - besides the machine state changing, transition performs the specified {@code action} and responds with the {@code impact result} that
 *    is "understandable" for the external {@code impact initiator};
 *    ("understandable" means that the {@code result} is a Java class specified externally, not by the {@code states} "engine")
 * </pre>
 * In other words, we specify actions that only could be done while being in a particular state only, wherein with a certain way looking
 * input. Each one specifies its own way to build a result. And in addition, those actions also change system state. Together, all those
 * actions (hidden behind the name of transition) form something like a set of rules, or from another perspective a graph with states as
 * vertexes and transitions as directed edges. So that, {@code StateMachine} is this ruleset/graph together with provided interaction api.
 *
 * @param <I> type of the incoming impact to handle.
 * @param <R> type of the impact result.
 *
 * @see Impact for the {@code impact} details.
 * @see State for the {@code state} details.
 * @see TransitionDescriptor for the {@code transition} details.
 */
public interface StateMachine<I extends Impact, R> {

    /**
     * Handles an impact in a way defined by an implementation. Namely, makes certain transition depending on the provided {@code impact}
     * and the state, the machine is currently in, and returns a result, or throws one of the listed exceptions.
     *
     * @param impact object that describes impact.
     *
     * @return object that describes impact result.
     *
     * @throws TransitionElectionException  if by the provided {@code impact} and the state, the machine is currently in, it can't be
     *                                      determined exactly one transition.
     * @throws TransitionExecutionException if transition action failed. See {@link TransitionDescriptor#execute(State, Impact)} for the
     *                                      details.
     */
    R handleImpact(I impact) throws TransitionElectionException, TransitionExecutionException;
}
