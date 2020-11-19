package ua.ddovgal.mangamonitoring.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.experimental.UtilityClass;

import ua.ddovgal.mangamonitoring.state.util.Pair;

@UtilityClass
public class GraphValidator {

    public final int OK_INDEX = -1;

    public int getUnreachableVertexIndex(SquareMatrix matrix) {
        int size = matrix.size;

        columns:
        for (int i = 0; i < size; i++) {
            boolean[] column = matrix.getColumn(i);
            for (boolean cell : column) {
                if (cell) {
                    continue columns;
                }
            }

            return i;
        }

        return OK_INDEX;
    }

    public int getEndingVertexIndex(SquareMatrix matrix) {
        int size = matrix.size;

        rows:
        for (int i = 0; i < size; i++) {
            boolean[] row = matrix.getRow(i);
            for (boolean cell : row) {
                if (cell) {
                    continue rows;
                }
            }

            return i;
        }

        return OK_INDEX;
    }

    public <T> Pair<SquareMatrix, List<Class<? extends State>>> buildTransitionMatrix(List<Transition<? extends State, ? extends State, T>> transitions) {
        int size = transitions.size();
        SquareMatrix matrix = new SquareMatrix(size);

        List<Class<? extends State>> states = new ArrayList<>(size);
        Map<Class<? extends State>, Integer> stateIndexes = new HashMap<>(size);
        for (Transition<? extends State, ? extends State, T> transition : transitions) {
            Class<? extends State> from = transition.getDepartureState();
            Class<? extends State> to = transition.getArrivalState();

            int row = getIfContainsOrPutNewStateIndex(stateIndexes, states, from);
            int column = getIfContainsOrPutNewStateIndex(stateIndexes, states, to);

            matrix.set(row, column);
        }

        return Pair.of(matrix, states);
    }

    private Integer getIfContainsOrPutNewStateIndex(Map<Class<? extends State>, Integer> stateIndexes,
                                                    List<Class<? extends State>> states,
                                                    Class<? extends State> state) {
        return Optional.ofNullable(stateIndexes.get(state))
                       .orElseGet(() -> {
                           int newIndex = stateIndexes.size();
                           stateIndexes.put(state, newIndex);
                           states.add(state);
                           return newIndex;
                       });
    }

    public class SquareMatrix {

        private final int size;
        private final boolean[][] matrix;

        public SquareMatrix(int size) {
            this.size = size;
            matrix = new boolean[size][size];
        }

        /*private SquareMatrix(boolean[][] matrix) {
            this.matrix = matrix;
            size = matrix.length;
        }

        public SquareMatrix copy() {
            boolean[][] rows = new boolean[size][];
            for (int i = 0; i < size; i++) {
                boolean[] oldRow = this.matrix[i];
                boolean[] newRow = Arrays.copyOf(oldRow, size);
                rows[i] = newRow;
            }
            return new SquareMatrix(rows);
        }*/

        public void set(int row, int column) {
            matrix[row][column] = true;
        }

        /*public void unset(int row, int column) {
            matrix[row][column] = false;
        }

        public boolean get(int row, int column) {
            return matrix[row][column];
        }*/

        public boolean[] getRow(int row) {
            return matrix[row];
        }

        public boolean[] getColumn(int column) {
            boolean[] assembledColumn = new boolean[size];
            for (int i = 0; i < size; i++) {
                assembledColumn[i] = matrix[i][column];
            }
            return assembledColumn;
        }
    }
}
