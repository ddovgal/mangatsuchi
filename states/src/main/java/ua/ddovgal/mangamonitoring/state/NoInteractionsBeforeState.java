package ua.ddovgal.mangamonitoring.state;

/**
 * Describes a default start point or initial state. Every possible {@link StateMachine} should have this state in its states set.
 * <p/>
 * Since this state is initial, meaning there were no interactions before, it doesn't have any "previous" data, which means that a {@code
 * NoInteractionsBeforeState} instance object data, which is latest or current data, will be absent. That's why this state doesn't have any
 * internal data.
 *
 * @see StateMachine for state machine a general concept.
 */
public final class NoInteractionsBeforeState implements State {}
