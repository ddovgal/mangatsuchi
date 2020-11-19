package ua.ddovgal.mangamonitoring.state;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.NonNull;

public interface StateGraph<T> {

    <S extends State> Stream<LinkedTransition<S, State, T>> getDepartures(S state);

    @AllArgsConstructor
    final class LinkedTransition<S1 extends State, S2 extends State, U> {

        @NonNull
        private Transition<S1, S2, U> transition;

        @NonNull
        private S1 departureState;

        public boolean isSuitable(U update) {
            return transition.isSuitable(departureState, update);
        }

        public Transit initTransit(U update) {
            return new Transit(update);
        }

        public final class Transit {

            private S2 arrivalState;
            private U update;

            private Transit(U update) {
                arrivalState = transition.buildArrivalState(update);
                this.update = update;
            }

            public void leaveOldState() {
                transition.departFrom(departureState, update);
            }

            public S2 arriveNewState() {
                arrivalState.arrive();
                return arrivalState;
            }

            public void presentNewState() {
                transition.consumeNewState(arrivalState, update);
            }
        }
    }
}
