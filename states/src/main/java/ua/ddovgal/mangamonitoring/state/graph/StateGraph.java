package ua.ddovgal.mangamonitoring.state.graph;

import java.util.List;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

/**
 * Represents graph structure. To be more precise, it's a <a href="https://en.wikipedia.org/wiki/State_diagram">State diagram</a>, a
 * directed graph, that can have cycles, parallel edges, edges pointing to the outgoing vertex (self-directed). Vertexes in this graph are
 * {@link State}s and edges are {@link TransitionDescriptor}s.
 * <p/>
 * The only thing graph can do, is to provide a list of edges (transitions) that possible to use while being it a certain vertex. In other
 * words, provides edges directed from certain vertex. See {@link #getDepartingTransitions(State)} for the details.
 *
 * @param <I> type of the impact.
 * @param <R> type of the impact result.
 *
 * @see TransitionDescriptor for transition details, as they represent edges.
 * @see State for state details, as they represent vertexes.
 */
public interface StateGraph<I extends Impact, R> {

    /**
     * Returns a list of the transitions "that are possible to make" while being in the provided {@code state}. Possibility meant from a
     * graph perspective only, not by any other conditions. Translating into the graph terms, state is equal to vertex and transition with
     * its departure and arrival states is equal to departureState->arrivalState directed edge. So "that are possible to make" becomes
     * "edges that are directed from the provided {@code state} vertex".
     * <p/>
     * This method also provides type guaranty for a resulted {@link DepartureDefinedTransition} first generic parameter, which stands for
     * transition departure state type, meaning that returned transition can be used with the provided {@code state} object and will not
     * cause {@link ClassCastException}.
     *
     * @param state state to get departing transitions.
     * @param <S>   type of the state to get departing transitions.
     *
     * @return a list of departing transitions.
     */
    <S extends State> List<DepartureDefinedTransition<S, ?, I, R>> getDepartingTransitions(S state);
}
