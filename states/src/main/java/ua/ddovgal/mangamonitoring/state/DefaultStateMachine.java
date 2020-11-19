package ua.ddovgal.mangamonitoring.state;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import ua.ddovgal.mangamonitoring.api.AuthenticationData;
import ua.ddovgal.mangamonitoring.state.StateGraph.LinkedTransition;
import ua.ddovgal.mangamonitoring.state.StateGraph.LinkedTransition.Transit;
import ua.ddovgal.mangamonitoring.state.exception.TransitionDeterminationException;

@AllArgsConstructor
public abstract class DefaultStateMachine<T> implements StateMachine<T> {

    private static final String NO_TRANSITIONS_MESSAGE = "There are no transitions that the provided state could make";
    private static final String MORE_THAT_ONE_TRANSITION_MESSAGE = "There is more than one transition that the provided state could make";

    @NonNull
    private StateGraph<T> stateGraph;

    /*@Override
    public void handleUpdate(AuthenticationData authData, T update) {
        State currentState = getCurrentState(authData).orElse(new NeverActedBeforeState());

        List<Transition<? extends State, ? extends State, T>> possibleTransitions = statesGraph
            .getDepartures(currentState)
            .filter(transition -> transition.isSuitable(currentState, update))
            .collect(Collectors.toList());

        if (possibleTransitions.isEmpty()) {
            throw new RuntimeException();//todo change
        }

        if (possibleTransitions.size() > 1) {
            throw new RuntimeException();//todo change
        }

        Transition<? extends State, ? extends State, T> transition = possibleTransitions.get(0);
        transition.doTransit(update, newState -> {
            newState.transitInto();
            setStateForAuthData(authData, newState);
        });
    }*/

    @Override
    public void handleUpdate(AuthenticationData authData, T update) throws TransitionDeterminationException {
        State currentState = getCurrentState(authData).orElse(new NeverActedBeforeState());

        List<LinkedTransition<State, State, T>> possibleTransitions = stateGraph
            .getDepartures(currentState)
            .filter(transition -> transition.isSuitable(update))
            .collect(Collectors.toList());

        if (possibleTransitions.isEmpty()) {
            throw new TransitionDeterminationException(NO_TRANSITIONS_MESSAGE, currentState, update);
        }

        if (possibleTransitions.size() > 1) {
            throw new TransitionDeterminationException(MORE_THAT_ONE_TRANSITION_MESSAGE, currentState, update);
        }

        LinkedTransition<State, State, T> chosenTransition = possibleTransitions.get(0);
        doTransit(chosenTransition.initTransit(update), authData);
    }

    /**
     * Method that encapsulates whole actual transit process. It doesn't throw any possible exceptions and handles any emerging issues
     * during transit. Could be treated like some kind of transaction.
     * <p/>
     * Method has default simplest implementation of pure sequential calls Method declared as {@code protected} and with default the
     * simplest implementation of pure sequential calls, but could be overridden to add exception handling, fallback or retry abilities and
     * so on.
     *
     * @param transit  initiated transit for received {@code update}.
     * @param authData auth data which identifies an update sender user.
     */
    // todo: how to handle exceptions, rollbacks ?
    protected void doTransit(Transit transit, AuthenticationData authData) {
        transit.leaveOldState();
        State newState = transit.arriveNewState();
        setStateForAuthData(authData, newState);
        transit.presentNewState();
    }

    protected abstract Optional<State> getCurrentState(AuthenticationData authData);
    protected abstract void setStateForAuthData(AuthenticationData authData, State state);
}
