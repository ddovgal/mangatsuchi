package ua.ddovgal.mangamonitoring.state.graph;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;
import ua.ddovgal.mangamonitoring.state.utils.States;
import ua.ddovgal.mangamonitoring.state.utils.States.BarState;
import ua.ddovgal.mangamonitoring.state.utils.States.BazState;
import ua.ddovgal.mangamonitoring.state.utils.States.BeginState;
import ua.ddovgal.mangamonitoring.state.utils.States.FooState;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import static ua.ddovgal.mangamonitoring.state.utils.DescriptorUtils.descriptor;
import static ua.ddovgal.mangamonitoring.state.utils.States.TEST_GRAPH_DESCRIPTORS;

@SuppressWarnings("rawtypes")
class ClassMappingStateGraphTest {

    @Test
    void testConstructor_descriptorsFormValidGraph_happyPath() {
        assertThatNoException().isThrownBy(() -> new ClassMappingStateGraph<>(TEST_GRAPH_DESCRIPTORS));
    }

    @ParameterizedTest(name = "For {index} test set")
    @MethodSource("source_getDepartingTransitions_descriptorsFormValidGraph_happyPath")
    void getDepartingTransitions_descriptorsFormValidGraph_happyPath(State state, List<Class<? extends State>> arrivalClasses) {
        ClassMappingStateGraph<Impact, Object> graph = new ClassMappingStateGraph<>(TEST_GRAPH_DESCRIPTORS);

        assertThat(graph.getDepartingTransitions(state))
            .extracting(DepartureDefinedTransition::getDescriptor)
            .allMatch(descriptor -> descriptor.getDepartureState() == state.getClass())
            .extracting(descriptor -> (Class) descriptor.getArrivalState())
            .containsExactlyInAnyOrderElementsOf(arrivalClasses);
    }

    /**
     * See {@link States#TEST_GRAPH_DESCRIPTORS} image.
     */
    private static Stream<Arguments> source_getDepartingTransitions_descriptorsFormValidGraph_happyPath() {
        return Stream.of(
            Arguments.of(new BeginState(), List.of(FooState.class)),
            Arguments.of(new FooState(), List.of(BarState.class, BazState.class)),
            Arguments.of(new BarState(), List.of(BazState.class, FooState.class)),
            Arguments.of(new BazState(), List.of(BarState.class, BazState.class, FooState.class))
        );
    }

    @Test
    void getDepartingTransitions_descriptorsFormInvalidGraph_returnsExpectedValues() {
        List<TransitionDescriptor<?, ?, Impact, Object>> descriptors =  List.of(
            descriptor(BeginState.class, BeginState.class),
            descriptor(FooState.class, BarState.class)
        );
        ClassMappingStateGraph<Impact, Object> graph = new ClassMappingStateGraph<>(descriptors);

        // self directed only, isolated
        assertThatForStateArrivalClassesWillBe(graph, new BeginState(), BeginState.class);
        // no arrivals
        assertThatForStateArrivalClassesWillBe(graph, new FooState(), BarState.class);
        // no departures
        assertThatForStateArrivalClassesWillBe(graph, new BarState());
        // doesn't even present in graph
        assertThatForStateArrivalClassesWillBe(graph, new BazState());
    }

    @Test
    void getDepartingTransitions_descriptorsContainMoreThatOneTransitionWithSameDirection_returnsExpectedValues() {
        List<TransitionDescriptor<?, ?, Impact, Object>> descriptors = List.of(
            descriptor(BeginState.class, FooState.class), // duplicate
            descriptor(BeginState.class, FooState.class), // duplicate
            descriptor(BeginState.class, BarState.class),
            descriptor(BarState.class, FooState.class),   // duplicate
            descriptor(BarState.class, FooState.class),   // duplicate
            descriptor(FooState.class, BeginState.class)
        );
        ClassMappingStateGraph<Impact, Object> graph = new ClassMappingStateGraph<>(descriptors);

        assertThatForStateArrivalClassesWillBe(graph, new BeginState(), FooState.class, FooState.class, BarState.class);
        assertThatForStateArrivalClassesWillBe(graph, new FooState(), BeginState.class);
        assertThatForStateArrivalClassesWillBe(graph, new BarState(), FooState.class, FooState.class);
    }

    @SafeVarargs
    private void assertThatForStateArrivalClassesWillBe(StateGraph<?, ?> graph, State state, Class<? extends State>... arrivalClasses) {
        assertThat(graph.getDepartingTransitions(state))
            .extracting(transition -> (Class) transition.getDescriptor().getArrivalState())
            .containsExactlyInAnyOrder(arrivalClasses);
    }
}