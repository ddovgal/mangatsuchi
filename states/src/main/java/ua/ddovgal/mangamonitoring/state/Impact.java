package ua.ddovgal.mangamonitoring.state;

import ua.ddovgal.mangamonitoring.api.AuthenticationData;

/**
 * Impact describes some kind of request or update that was come into the {@link StateMachine} from an external system.
 *
 * @see StateMachine for state machine a general concept.
 */
public interface Impact {

    /**
     * Returns an authentication data for an external impact initiator.
     *
     * @return authentication data for an external impact initiator.
     */
    AuthenticationData getInitiatorAuthData();
}
