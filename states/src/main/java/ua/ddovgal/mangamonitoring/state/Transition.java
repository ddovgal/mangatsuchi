package ua.ddovgal.mangamonitoring.state;

public interface Transition<S1 extends State, S2 extends State, U> {

    Class<S1> getDepartureState();

    Class<S2> getArrivalState();

    boolean isSuitable(S1 departingState, U update);

    S2 buildArrivalState(U update);

    void departFrom(S1 departingState, U update);

    void consumeNewState(S2 newState, U update);
}
