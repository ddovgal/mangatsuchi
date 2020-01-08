package ua.ddovgal.trackerkun.domain;

import java.util.UUID;

import lombok.Data;

import ua.ddovgal.trackerkun.api.AuthenticationData;

/**
 * Class describing user account in scope of our system. Actually this is description of single user, who have some consumer identification
 * data, so current system could identify user in consumer scope when will notify it. Each account also have privilege to perform certain
 * actions.
 */
@Data
public class Account {

    /**
     * Account ID scope of our system.
     */
    private UUID id;

    /**
     * Account privilege to perform certain actions.
     */
    private Privilege privilege;

    /**
     * Authentication data describing user in scope of certain consumer system.
     */
    private AuthenticationData authenticationData;
}
