package ua.ddovgal.mangamonitoring.state;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

import ua.ddovgal.mangamonitoring.state.GraphValidator.SquareMatrix;
import ua.ddovgal.mangamonitoring.state.exception.UnacceptableGraphStructureException;
import ua.ddovgal.mangamonitoring.state.util.Pair;

@Slf4j
public class ClassMappingStateGraph<T> implements StateGraph<T> {

    // S1 types of each Transition "value" list equal to list's corresponding "key"
    private final Map<Class<State>, List<Transition<State, State, T>>> stateAndItsDepartingTransitions;

    /**
     * Builds graph by provided forming transitions. Makes a base validation of built graph (see {@link #isGraphStructureAcceptable(List)})
     * and throws exception if case of unacceptable graph structure.
     * <p>
     * Unchecked explanations:
     * <p>
     * Since for the built {@link ClassMappingStateGraph} there is possibility to interact only with {@link LinkedTransition} but not with
     * raw {@link Transition}...
     * <p>
     * - it's safe to erase {@code S1} generic because all interactions with raw {@link Transition} inside {@link LinkedTransition} are made
     * with associated {@code departureState}, and {@code departureState} is guaranteed to always be {@code S1} type because of the way
     * {@link LinkedTransition} created (see {@link #getDepartures(State)});
     * <p>
     * - it's safe to erase {@code S2} generic because all interactions with {@code S2} type variable are made inside {@link
     * LinkedTransition.Transit} and that {@code S2} variable is created by {@link Transition} itself, so it guarantees type safety.
     *
     * @param transitions transitions that defines whole graph.
     */
    @SuppressWarnings("unchecked") // see explanations in the last 3 paragraphs of the description above
    public ClassMappingStateGraph(List<Transition<? extends State, ? extends State, T>> transitions) throws UnacceptableGraphStructureException {
        if (!isGraphStructureAcceptable(transitions)) {
            throw new UnacceptableGraphStructureException();
        }

        stateAndItsDepartingTransitions = transitions
            .stream()
            .map(transition -> (Transition<State, State, T>) transition)
            .collect(Collectors.groupingBy(Transition::getDepartureState));
    }

    /**
     * Unchecked explanation:
     * <p>
     * It's safe to specify {@code S1} type for each {@link Transition} because in {@link #stateAndItsDepartingTransitions} map {@code S1}
     * type of each {@link Transition} from {@code value} list is equal to list's corresponding {@code key}. And this is already achieved by
     * how {@link #stateAndItsDepartingTransitions} is created (see {@code stateDeparturesMap} creation in {@link
     * #ClassMappingStateGraph(List)}).
     */
    @SuppressWarnings("unchecked") // see explanation in description above
    @Override
    public <S extends State> Stream<LinkedTransition<S, State, T>> getDepartures(S state) {
        return stateAndItsDepartingTransitions
            .get(state.getClass())
            .stream()
            .map(transition -> (Transition<S, State, T>) transition)
            .map(transition -> new LinkedTransition<>(transition, state));
    }

    private boolean isGraphStructureAcceptable(List<Transition<? extends State, ? extends State, T>> transitions) {
        Pair<SquareMatrix, List<Class<? extends State>>> matrixAndStates = GraphValidator.buildTransitionMatrix(transitions);
        SquareMatrix matrix = matrixAndStates.getLeft();
        List<Class<? extends State>> states = matrixAndStates.getRight();

        int guiltyStateIndex;

		//todo: has NABState to NeutralState transition
		
        if ((guiltyStateIndex = GraphValidator.getUnreachableVertexIndex(matrix)) != GraphValidator.OK_INDEX) {
            log.error("The provided transitions make a graph with unreachable state={}", states.get(guiltyStateIndex).getSimpleName());
            return false;
        }

        if ((guiltyStateIndex = GraphValidator.getEndingVertexIndex(matrix)) != GraphValidator.OK_INDEX) {
            log.error("The provided transitions make a graph with dead-end state={}", states.get(guiltyStateIndex).getSimpleName());
            return false;
        }

        return true;
    }
}
