package ua.ddovgal.mangamonitoring.state.graph;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

import static ua.ddovgal.mangamonitoring.state.util.TransitionDescriptorUtils.getSimpleForm;

/**
 * Implementation that uses {@link Map} to compare {@link State} type (java Class) and list of it possible departing transitions.
 *
 * @param <I> type of the impact.
 * @param <R> type of the impact result.
 */
@Slf4j
public class ClassMappingStateGraph<I extends Impact, R> implements StateGraph<I, R> {

    // This map has some restriction for types that couldn't be described in Java.
    // Actually it's Map<Class<S_TYPE>, List<TransitionDescriptor<S_TYPE, ?, I, R>>> where S_TYPE is unique for each Entry,
    // but at the same time, within one Entry, key-side S_TYPE is equal to value-side S_TYPE.
    // Meaning that for Class<FooState> key actual value is List<TransitionDescriptor<FooState, ?, I, R>>,
    // for Class<BarState> key actual value is List<TransitionDescriptor<BarState, ?, I, R>> and so on.
    private final Map<Class<? extends State>, List<TransitionDescriptor<?, ?, I, R>>> stateAndItsDepartingTransitions;

    /**
     * Builds graph by the provided forming transition {@code descriptors}. While it will build graph for any descriptors list, even for
     * empty, it doesn't check that built graph will be valid and usable, hence {@code descriptors} should be already checked before passing
     * to this constructor (at least {@link GraphValidator#checkBaseAcceptability(List)} should be used).
     *
     * @param descriptors transition descriptors that defines the whole graph.
     */
    public ClassMappingStateGraph(List<TransitionDescriptor<?, ?, I, R>> descriptors) {
        log.info("Building from descriptors={}", getSimpleForm(descriptors));
        stateAndItsDepartingTransitions = descriptors.stream().collect(Collectors.groupingBy(TransitionDescriptor::getDepartureState));
    }

    /**
     * {@inheritDoc}
     * <p/>
     * It's safe to clarify {@code D} type for each {@link TransitionDescriptor} because {@link #stateAndItsDepartingTransitions} map is
     * designed in such a way, that for each {@link Map.Entry} the class of its {@code key} will be equal to {@code D} generic of each
     * {@link TransitionDescriptor} from {@code value} list. This is guarantied by the way {@link #stateAndItsDepartingTransitions} is
     * created (see {@link #ClassMappingStateGraph(List)}).
     */
    @SuppressWarnings("unchecked") // check description above
    @Override
    public <D extends State> List<DepartureDefinedTransition<D, ?, I, R>> getDepartingTransitions(D state) {
        return stateAndItsDepartingTransitions
            .getOrDefault(state.getClass(), List.of())
            .stream()
            .map(descriptor -> (TransitionDescriptor<D, ?, I, R>) descriptor)
            .map(descriptor -> new DepartureDefinedTransition<>(descriptor, state))
            .collect(Collectors.toList());
    }
}
