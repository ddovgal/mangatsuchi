package ua.ddovgal.mangamonitoring.state.election;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.exception.TransitionElectionException;
import ua.ddovgal.mangamonitoring.state.graph.StateGraph;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

import static ua.ddovgal.mangamonitoring.state.util.TransitionDescriptorUtils.getDescriptorsSimpleForm;

/**
 * Simple implementation that under the hood uses {@link StateGraph} and provide ability to specify {@link TransitionFilter}s for post
 * filtering suitable transitions got from a graph. Filters applied to the suitable transitions list consequently, hence if the filtering
 * order is important, specify it in the {@link #filters} list.
 * <p/>
 * This implementation will throw {@link TransitionElectionException} if not exactly one transition remains after the filtering.
 *
 * @param <I> type of the impact.
 * @param <R> type of the impact result.
 *
 * @see StateGraph for the graph details.
 * @see TransitionFilter for the filtering details.
 */
@Slf4j
@RequiredArgsConstructor
public class SimpleTransitionElector<I extends Impact, R> implements TransitionElector<I, R> {

    @NonNull
    private final StateGraph<I, R> stateGraph;
    @NonNull
    private final List<TransitionFilter<I, R>> filters;

    @Override
    public DepartureDefinedTransition<?, ?, I, R> electTransition(State departure, I impact) throws TransitionElectionException {
        List<DepartureDefinedTransition<?, ?, I, R>> suitableTransitions = getSuitableTransitions(departure, impact);
        log.info("suitableTransitions={}", getDescriptorsSimpleForm(suitableTransitions));
        List<DepartureDefinedTransition<?, ?, I, R>> electedTransitions = filterTransitions(suitableTransitions, departure, impact);

        if (electedTransitions.size() != 1) {
            List<TransitionDescriptor<?, ?, ?, ?>> electedDescriptors = electedTransitions
                .stream()
                .map(DepartureDefinedTransition::getDescriptor)
                .collect(Collectors.toList());
            throw new TransitionElectionException(impact, departure, electedDescriptors);
        }

        return electedTransitions.get(0);
    }

    private List<DepartureDefinedTransition<?, ?, I, R>> getSuitableTransitions(State departure, I impact) {
        return stateGraph
            .getDepartingTransitions(departure)
            .stream()
            .filter(transition -> transition.isSuitable(impact))
            .collect(Collectors.toList());
    }

    private List<DepartureDefinedTransition<?, ?, I, R>> filterTransitions(List<DepartureDefinedTransition<?, ?, I, R>> transitions,
                                                                           State departure,
                                                                           I impact) {
        return filters.stream()
            // apply each filter from the filters, using transitions as an initial list to filter
            .reduce(
                transitions,
                (transitionsList, filter) -> filter.filter(impact, departure, transitionsList),
                (previousList, filteredList) -> filteredList
            );
    }
}
