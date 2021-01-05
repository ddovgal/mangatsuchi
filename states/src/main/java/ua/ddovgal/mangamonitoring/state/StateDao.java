package ua.ddovgal.mangamonitoring.state;

import java.util.Optional;

import ua.ddovgal.mangamonitoring.api.AuthenticationData;
import ua.ddovgal.mangamonitoring.domain.Account;

/**
 * DAO required by {@link StateMachine} for {@link State} get/set operation.
 */
public interface StateDao {

    /**
     * Searches for the state by the provided {@code authData}.
     *
     * @param authData authentication data (which points to the assigned {@link Account}) to search for.
     *
     * @return persisted state for the provided {@code authData}.
     */
    Optional<State> getState(AuthenticationData authData);

    /**
     * Sets the provided {@code state} for the provided {@code authData}.
     *
     * @param authData authentication data (which points to the assigned {@link Account}) to search for.
     * @param state    state that need to be set.
     */
    void setState(AuthenticationData authData, State state);
}
