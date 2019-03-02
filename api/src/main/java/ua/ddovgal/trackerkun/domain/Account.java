package ua.ddovgal.trackerkun.domain;

import java.util.UUID;

import lombok.Data;

import ua.ddovgal.trackerkun.api.ConsumerAuthData;

/**
 * Class describing account in current system. Actually this is description of single user, who have some consumer identification data, so
 * current system could identify user in consumer's scope when will notify it. Each account also have privilege to perform certain actions.
 */
@Data
public class Account {

    /**
     * Account's ID  in current system.
     */
    private UUID id;
    /**
     * Account's privilege to perform certain actions.
     */
    private Privilege privilege;
    /**
     * Authentication data describing account's user in scope of certain consumer system.
     */
    private ConsumerAuthData consumerAuthData;
}
