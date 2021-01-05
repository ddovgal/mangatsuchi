package ua.ddovgal.mangamonitoring.state.graph;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.experimental.UtilityClass;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.NoInteractionsBeforeState;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;
import ua.ddovgal.mangamonitoring.state.util.Pair;

/**
 * Utility class that provides different types of checks for the validity of the provided graph-forming transition descriptors list.
 */
@UtilityClass
public class GraphValidator {

    /**
     * Constant, saying "no issues" or OK value Used as a response by some checker methods that return int.
     */
    public static final int OK_INDEX = -1;

    /**
     * Makes basic checks to determine if it is possible to build a usable valid graph from the provided {@code descriptors} and returns
     * list of found violations. This check doesn't guarantee that any possible {@link StateGraph} created from the provided {@code
     * descriptors} will be valid, but it guarantees that if this check founds any violations then no graph will be valid.
     * <pre>
     * Method checks for:
     *  - unreachable states, should be absent;
     *  - dead-end states, should be absent;
     *  - initial transitions (that start from {@link NoInteractionsBeforeState}), should exist;
     * </pre>
     *
     * @param descriptors list that serves as a source for the graph creation.
     * @param <I>         type of the {@link Impact} object provided {@code descriptors} work with.
     * @param <R>         type of the {@code impactResult} object provided {@code descriptors} return.
     *
     * @return list of found violations.
     */
    public <I extends Impact, R> List<String> checkBaseAcceptability(List<TransitionDescriptor<?, ?, I, R>> descriptors) {
        List<String> violations = new ArrayList<>();

        Pair<SquareMatrix, List<Class<? extends State>>> matrixAndStates = buildTransitionMatrix(descriptors);
        List<Class<? extends State>> states = matrixAndStates.getRight();
        SquareMatrix matrix = matrixAndStates.getLeft();

        int guiltyStateIndex;

        guiltyStateIndex = GraphValidator.getUnreachableVertexIndex(matrix, states.indexOf(NoInteractionsBeforeState.class));
        if (guiltyStateIndex != GraphValidator.OK_INDEX) {
            violations.add(String.format("Provided descriptors form a graph with an unreachable state=%s",
                                         states.get(guiltyStateIndex).getSimpleName()));
        }
        guiltyStateIndex = GraphValidator.getEndingVertexIndex(matrix);
        if (guiltyStateIndex != GraphValidator.OK_INDEX) {
            violations.add(String.format("Provided descriptors form a graph with a dead-end state=%s",
                                         states.get(guiltyStateIndex).getSimpleName()));
        }

        boolean initialTransitionsExist = descriptors
            .stream()
            .anyMatch(descriptor -> descriptor.getDepartureState() == NoInteractionsBeforeState.class);
        if (!initialTransitionsExist) {
            violations.add("There is no initial (that has NoInteractionsBeforeState as a departure) transitions among the provided");
        }

        return violations;
    }

    /**
     * Builds from the provided {@code descriptors} list a square matrix and returns it together with list of state classes. This list
     * serves like the marker row for the matrix, meaning that row/column in a matrix with a particular index corresponds to the state in
     * the list with the {@code index} position.
     *
     * @param descriptors list that serves as a source for the graph creation.
     * @param <I>         type of the {@link Impact} object provided {@code descriptors} work with.
     * @param <R>         type of the {@code impactResult} object provided {@code descriptors} return.
     *
     * @return square matrix that represents graph, formed by the provided {@code descriptors} and the "marker row" list for that matrix.
     */
    public <I extends Impact, R> Pair<SquareMatrix, List<Class<? extends State>>> buildTransitionMatrix(List<TransitionDescriptor<?, ?, I, R>> descriptors) {
        // first need to determine, how many distinct states it has
        Map<Class<? extends State>, Integer> stateIndexes = new LinkedHashMap<>();
        for (TransitionDescriptor<?, ?, I, R> descriptor : descriptors) {
            // stateIndexes.size() serves as always actual index for another new state
            stateIndexes.putIfAbsent(descriptor.getDepartureState(), stateIndexes.size());
            stateIndexes.putIfAbsent(descriptor.getArrivalState(), stateIndexes.size());
        }

        SquareMatrix matrix = new SquareMatrix(stateIndexes.size());
        for (TransitionDescriptor<?, ?, I, R> descriptor : descriptors) {
            int row = stateIndexes.get(descriptor.getDepartureState());
            int column = stateIndexes.get(descriptor.getArrivalState());
            matrix.set(row, column);
        }

        List<Class<? extends State>> states = List.copyOf(stateIndexes.keySet());
        return Pair.of(matrix, states);
    }

    /**
     * Checks the provided {@code matrix} for the unreachable states. Also, it will skip check for the provided {@code initialStateIndex}
     * since initial states actually don't have arrival transitions.
     *
     * @param matrix            matrix to check.
     * @param initialStateIndex index of the initial state, for which check will be skipped.
     *
     * @return index of the unreachable state if it exists, or else {@link #OK_INDEX}.
     */
    public int getUnreachableVertexIndex(SquareMatrix matrix, int initialStateIndex) {
        int size = matrix.size;

        columns:
        for (int i = 0; i < size; i++) {
            // do not check initialStateIndex column
            if (i == initialStateIndex) {
                continue;
            }

            boolean[] column = matrix.getColumn(i);
            for (int j = 0; j < size; j++) {
                // self-directed transition doesn't count
                if (column[j] && j != i) {
                    continue columns;
                }
            }

            return i;
        }

        return OK_INDEX;
    }

    /**
     * Checks the provided {@code matrix} for the dead-end states.
     *
     * @param matrix matrix to check.
     *
     * @return index of the dead-end state if it exists, or else {@link #OK_INDEX}.
     */
    public int getEndingVertexIndex(SquareMatrix matrix) {
        int size = matrix.size;

        rows:
        for (int i = 0; i < size; i++) {
            boolean[] row = matrix.getRow(i);
            for (int j = 0; j < size; j++) {
                // self-directed transition doesn't count
                if (row[j] && j != i) {
                    continue rows;
                }
            }

            return i;
        }

        return OK_INDEX;
    }

    /**
     * Represents 2-dimensional array of booleans, matrix with an equal row and column dimension. Provides useful methods for the
     * manipulation.
     */
    @EqualsAndHashCode
    public static class SquareMatrix {

        private final int size;
        private final boolean[][] matrix;

        /**
         * Builds matrix of the provided {@code size}. All cells will be set to default {@code false} value.
         *
         * @param size size of the matrix to create.
         */
        public SquareMatrix(int size) {
            this.size = size;
            matrix = new boolean[size][size];
        }

        /**
         * Sets [{@code row}, {@code column}] to {@code true}.
         *
         * @param row    row to select.
         * @param column column to select
         */
        public void set(int row, int column) {
            matrix[row][column] = true;
        }

        /**
         * Returns the whole row for the provided {@code index}.
         *
         * @param index index of the row to get.
         *
         * @return full row of the provided {@code index}.
         */
        public boolean[] getRow(int index) {
            return matrix[index];
        }

        /**
         * Returns the whole column for the provided {@code index}.
         *
         * @param index index of the column to get.
         *
         * @return full column of the provided {@code index}.
         */
        public boolean[] getColumn(int index) {
            boolean[] assembledColumn = new boolean[size];
            for (int i = 0; i < size; i++) {
                assembledColumn[i] = matrix[i][index];
            }
            return assembledColumn;
        }
    }
}
