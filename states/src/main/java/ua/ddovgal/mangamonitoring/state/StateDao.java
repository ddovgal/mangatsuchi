package ua.ddovgal.mangamonitoring.state;

import java.util.Optional;

import ua.ddovgal.mangamonitoring.api.AuthenticationData;

public interface StateDao {
    Optional<State> getState(AuthenticationData authData);
    void setState(AuthenticationData authData, State state);
}
