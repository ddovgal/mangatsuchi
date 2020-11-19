package ua.ddovgal.mangamonitoring.state;

import ua.ddovgal.mangamonitoring.api.AuthenticationData;
import ua.ddovgal.mangamonitoring.state.exception.TransitionDeterminationException;

public interface StateMachine<T> {

    void handleUpdate(AuthenticationData authData, T update) throws TransitionDeterminationException;

    /*interface Graph<T> {

//        Stream<Transition<? extends State, ? extends State, T>> getDepartureTransitions(State state);
        Stream<LinkedTransition<State, State, T>> getDeparturesOf(State state);

        @AllArgsConstructor
        class LinkedTransition<S1 extends State, S2 extends State, U> {

            @Getter
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

            public class Transit {

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
    }*/
}
