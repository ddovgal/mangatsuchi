package ua.ddovgal.mangamonitoring.state.election;

import java.util.List;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;

/**
 * Filter function that is used for post filtering suitable transitions got from a graph in {@link SimpleTransitionElector} implementation.
 *
 * @param <I> type of the impact.
 * @param <R> type of the impact result.
 *
 * @see SimpleTransitionElector#electTransition(State, Impact) for details about the place, implementations of this class are intended to be
 * used.
 */
@FunctionalInterface
public interface TransitionFilter<I extends Impact, R> {

    /**
     * By the provided {@code impact} and {@code state} filters also provided {@code transitions} and returns new filtered list.
     *
     * @param impact      impact that was taken as a {@link SimpleTransitionElector#electTransition(State, Impact)} parameter.
     * @param state       departure state that was taken as a {@link SimpleTransitionElector#electTransition(State, Impact)} parameter.
     * @param transitions transitions list to filter.
     *
     * @return new filtered transitions list.
     *
     * @see SimpleTransitionElector for {@code filterTransitions(List, State, Impact)} method as a place where this method is called.
     */
    List<DepartureDefinedTransition<?, ?, I, R>> filter(I impact, State state, List<DepartureDefinedTransition<?, ?, I, R>> transitions);
}
