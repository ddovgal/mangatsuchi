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
     * It's safe to cast {@code D} generic type for the {@code descriptor} due to the following reasons.
     * <br>
     * It's safe to cast {@code D} generic type for the {@code descriptor} due to the following reasons. The casted object is used only by
     * the {@link DepartureDefinedTransition} constructor, meaning that it is quite enough only that its parameters will be valid. Types of
     * the parameters for that constructor are [TransitionDescriptor<D, A, I, R> arg1, D arg2]. This means that {@code D} type of the first
     * argument must be equal to the actual class of the second argument. For our arguments, this means that the actual class of the {@code
     * state} must be equal to {@code D} type of the {@code descriptor}. And this is already achieved due to the fact that descriptor got
     * from the {@link #stateAndItsDepartingTransitions} map. It's designed in a way, that for each of its {@link Map.Entry}s, the {@code
     * key} (which is already a Class) will be exactly the same as {@code D} generic type of each {@link TransitionDescriptor} from {@code
     * value} list. This is guaranteed by the way {@link #stateAndItsDepartingTransitions} is created, see {@link
     * #ClassMappingStateGraph(List)}.
     */
    @SuppressWarnings("unchecked") // check the description above
    @Override
    public List<DepartureDefinedTransition<?, ?, I, R>> getDepartingTransitions(State state) {
        return stateAndItsDepartingTransitions
            .getOrDefault(state.getClass(), List.of())
            .stream()
            // this cast is necessary only to be able to create DepartureDefinedTransition
            .map(descriptor -> (TransitionDescriptor<State, ?, I, R>) descriptor)
            .map(descriptor -> new DepartureDefinedTransition<>(descriptor, state))
            .collect(Collectors.toList());
    }
}
