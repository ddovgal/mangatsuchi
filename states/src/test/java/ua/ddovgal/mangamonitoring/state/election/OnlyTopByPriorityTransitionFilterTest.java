package ua.ddovgal.mangamonitoring.state.election;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import static ua.ddovgal.mangamonitoring.state.utils.DescriptorUtils.descriptorBuilder;

class OnlyTopByPriorityTransitionFilterTest {

    private final TransitionFilter<Impact, Object> filter = new OnlyTopByPriorityTransitionFilter<>();

    @ParameterizedTest(name = "For {index} priorities set")
    @MethodSource("source_filter")
    void filter(List<Integer> priorities, List<Integer> expectedPriorities) {
        List<DepartureDefinedTransition<?, ?, Impact, Object>> transitions = priorities
            .stream()
            .map(OnlyTopByPriorityTransitionFilterTest::withPriority)
            .collect(Collectors.toList());

        List<DepartureDefinedTransition<?, ?, Impact, Object>> filteredList = filter.filter(null/*unused*/, null/*unused*/, transitions);

        assertThat(filteredList).extracting(DepartureDefinedTransition::getPriority).containsExactlyElementsOf(expectedPriorities);
    }

    private static Stream<Arguments> source_filter() {
        return Stream.of(
            Arguments.of(List.of(2, 0, 4, 1, 0, 4, 1, 1), List.of(4, 4)), // happy path
            Arguments.of(List.of(2, 0, 4, 1, 0, 4, 1, 1, 5), List.of(5)), // happy path 2
            Arguments.of(List.of(5, 0, 4, 1, 0, 4, 1, 0, 0), List.of(5)), // happy path 3
            Arguments.of(List.of(), List.of()),                           // empty transitions list input
            Arguments.of(List.of(1, 1, 1, 1), List.of(1, 1, 1, 1)),       // all transitions have same priority
            Arguments.of(List.of(1, 0, 0, 1), List.of(1, 1))              // the highest priority transitions placed on the sides
        );
    }

    private static DepartureDefinedTransition<State, State, Impact, Object> withPriority(int priority) {
        TransitionDescriptor<State, State, Impact, Object> descriptor = descriptorBuilder(State.class, State.class)
            .priority(priority)
            .build();
        return new DepartureDefinedTransition<>(descriptor, mock(State.class));
    }
}