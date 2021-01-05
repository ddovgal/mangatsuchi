package ua.ddovgal.mangamonitoring.state.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.NoInteractionsBeforeState;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.graph.GraphValidator.SquareMatrix;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;
import ua.ddovgal.mangamonitoring.state.util.Pair;
import ua.ddovgal.mangamonitoring.state.utils.States;
import ua.ddovgal.mangamonitoring.state.utils.States.BarState;
import ua.ddovgal.mangamonitoring.state.utils.States.BazState;
import ua.ddovgal.mangamonitoring.state.utils.States.BeginState;
import ua.ddovgal.mangamonitoring.state.utils.States.FooState;

import static org.assertj.core.api.Assertions.assertThat;

import static ua.ddovgal.mangamonitoring.state.graph.GraphValidator.OK_INDEX;
import static ua.ddovgal.mangamonitoring.state.util.Pair.of;
import static ua.ddovgal.mangamonitoring.state.utils.DescriptorUtils.descriptor;
import static ua.ddovgal.mangamonitoring.state.utils.States.TEST_GRAPH_DESCRIPTORS;

class GraphValidatorTest {

    @Test
    void checkBaseAcceptability() {
        List<String> violations;

        ArrayList<TransitionDescriptor<?, ?, Impact, Object>> descriptors = new ArrayList<>(TEST_GRAPH_DESCRIPTORS);
        // TEST_GRAPH_DESCRIPTORS has BeginState as initial, but in real world initial is NIB, hence need to change
        descriptors.set(0, descriptor(NoInteractionsBeforeState.class, FooState.class));
        violations = GraphValidator.checkBaseAcceptability(descriptors);
        assertThat(violations).isEmpty();

        violations = GraphValidator.checkBaseAcceptability(withoutTransitions(1, 3, 4, 6, 8));
        assertThat(violations)
            .hasSize(3)
            .haveExactly(1, fromPredicate(s -> s.contains("unreachable")))
            .haveExactly(1, fromPredicate(s -> s.contains("dead-end")))
            .haveExactly(1, fromPredicate(s -> s.contains("no initial")));
    }

    @ParameterizedTest(name = "For {index} transitions set")
    @MethodSource("source_buildTransitionMatrix")
    void buildTransitionMatrix(List<TransitionDescriptor<?, ?, Impact, Object>> descriptors,
                               List<Class<? extends State>> expectedStates,
                               SquareMatrix expectedMatrix) {
        Pair<SquareMatrix, List<Class<? extends State>>> matrixAndStates = GraphValidator.buildTransitionMatrix(descriptors);
        SquareMatrix matrix = matrixAndStates.getLeft();
        List<Class<? extends State>> states = matrixAndStates.getRight();

        assertThat(matrix).isEqualTo(expectedMatrix);
        assertThat(states).isEqualTo(expectedStates);
    }

    private static Stream<Arguments> source_buildTransitionMatrix() {
        return Stream.of(
            Arguments.of(
                TEST_GRAPH_DESCRIPTORS,               // BEGIN - 0; FOO - 1; BAR - 2; BAZ - 3
                List.of(BeginState.class, FooState.class, BarState.class, BazState.class),
                specifyingCells(4, of(0, 1), of(1, 2), of(2, 3), of(3, 2), of(3, 3), of(3, 1), of(2, 1), of(1, 3))
            ),
            Arguments.of(
                withoutTransitions(1, 2, 5, 7),       // BAR - 0; BAZ - 1; FOO - 2
                List.of(BarState.class, BazState.class, FooState.class),
                specifyingCells(3, of(0, 1), of(1, 0), of(1, 2), of(2, 1))
            ),
            Arguments.of(
                withoutTransitions(1, 2, 3, 4, 5, 7), // BAZ - 0; FOO - 1
                List.of(BazState.class, FooState.class),
                specifyingCells(2, of(0, 1), of(1, 0))
            )
        );
    }

    @ParameterizedTest(name = "For {index} transitions set")
    @MethodSource("source_getUnreachableVertexIndex")
    void getUnreachableVertexIndex(List<TransitionDescriptor<?, ?, Impact, Object>> descriptors, int expectedIndex) {
        Pair<SquareMatrix, List<Class<? extends State>>> matrixAndStates = GraphValidator.buildTransitionMatrix(descriptors);
        SquareMatrix matrix = matrixAndStates.getLeft();
        List<Class<? extends State>> states = matrixAndStates.getRight();

        int actualIndex = GraphValidator.getUnreachableVertexIndex(matrix, states.indexOf(BeginState.class));

        assertThat(actualIndex).isEqualTo(expectedIndex);
    }

    private static Stream<Arguments> source_getUnreachableVertexIndex() {
        return Stream.of(
            Arguments.of(TEST_GRAPH_DESCRIPTORS, OK_INDEX),
            Arguments.of(withoutTransitions(2, 6, 7, 8), OK_INDEX), // should be BEGIN, but it's initial
            Arguments.of(withoutTransitions(1, 6, 7), 0),           // FOO, but without BEGIN its index will be 0
            Arguments.of(withoutTransitions(2, 4), 2),              // BAR
            Arguments.of(withoutTransitions(3, 5, 8), 3),           // BAZ
            Arguments.of(withoutTransitions(3, 8), 3),              // BAZ, because self-direction is ignored
            Arguments.of(withoutTransitions(3, 4, 5), OK_INDEX)
        );
    }

    @ParameterizedTest(name = "For {index} transitions set")
    @MethodSource("source_getEndingVertexIndex")
    void getEndingVertexIndex(List<TransitionDescriptor<?, ?, Impact, Object>> descriptors, int expectedIndex) {
        SquareMatrix matrix = GraphValidator.buildTransitionMatrix(descriptors).getLeft();

        int actualIndex = GraphValidator.getEndingVertexIndex(matrix);

        assertThat(actualIndex).isEqualTo(expectedIndex);
    }

    private static Stream<Arguments> source_getEndingVertexIndex() {
        return Stream.of(
            Arguments.of(TEST_GRAPH_DESCRIPTORS, OK_INDEX),
            // nothing can create to make BEGIN ending state
            Arguments.of(withoutTransitions(2, 8), 1),             // FOO
            Arguments.of(withoutTransitions(3, 7), 2),             // BAR
            Arguments.of(withoutTransitions(4, 5, 6), 3),          // BAZ
            Arguments.of(withoutTransitions(4, 6), 3),             // BAZ, because self-direction is ignored
            Arguments.of(withoutTransitions(5, 6, 7, 8), OK_INDEX) // will cycle BAR->BAZ->BAR
        );
    }

    /**
     * Returns copied {@link States#TEST_GRAPH_DESCRIPTORS} without transitions with the provided numbers.
     * <br>
     * WARNING: transitionNumber = transitionIndex + 1
     */
    private static List<TransitionDescriptor<?, ?, Impact, Object>> withoutTransitions(int... transitionNumbers) {
        List<TransitionDescriptor<?, ?, Impact, Object>> result = new ArrayList<>(TEST_GRAPH_DESCRIPTORS.size() - transitionNumbers.length);
        for (int i = 0; i < TEST_GRAPH_DESCRIPTORS.size(); i++) {
            int finalI = i;
            // if we are on excluded index
            if (IntStream.of(transitionNumbers).anyMatch(number -> number - 1 == finalI)) {
                continue;
            }
            result.add(TEST_GRAPH_DESCRIPTORS.get(i));
        }
        return result;
    }

    /**
     * Creates a matrix with specified size, having {@code true} only in provided cellCoordinates.
     */
    @SafeVarargs
    private static SquareMatrix specifyingCells(int size, Pair<Integer, Integer>... cellCoordinates) {
        SquareMatrix matrix = new SquareMatrix(size);
        for (Pair<Integer, Integer> rowAndCol : cellCoordinates) {
            matrix.set(rowAndCol.getLeft(), rowAndCol.getRight());
        }
        return matrix;
    }

    private static <T> Condition<T> fromPredicate(Predicate<T> predicate) {
        return new Condition<>(predicate, null);
    }
}