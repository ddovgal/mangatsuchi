package ua.ddovgal.mangamonitoring.domain;

import java.util.UUID;

import lombok.Data;

import ua.ddovgal.mangamonitoring.api.AuthenticationData;

/**
 * Pojo that describes user account in current application's scope. Actually this is description of single user, who have some consumer
 * identification data, so current application could identify user in consumer's scope. Each account also have some privilege to perform
 * certain actions.
 */
@Data
public class Account {

    /**
     * Account ID in current application's scope.
     */
    private UUID id;

    /**
     * Account privilege to perform certain actions.
     */
    private Privilege privilege;

    /**
     * Authentication data that describes user in consumer system scope.
     */
    private AuthenticationData authenticationData;
}
