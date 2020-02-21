package ua.ddovgal.mangamonitoring.api;

import lombok.Getter;

/**
 * Authentication data that describes user of some account in scope of certain consumer. Since it is expected for authentication data to be
 * serialized, it must be able to be created from username and identifier set.
 */
public abstract class AuthenticationData {

    /**
     * Username of user in scope of current consumer.
     */
    @Getter
    private String username;

    /**
     * Creates new authentication data object with username. In this case it is assumed that {@code username} serves as an identifier. It is
     * also expected that subclasses with constructors different to {@link #AuthenticationData(String, String)} will use supper call of this
     * particular constructor.
     *
     * @param username username of user in scope of consumer.
     */
    protected AuthenticationData(String username) {
        this.username = username;
    }

    /**
     * Creates a new authentication data object with {@code username} and from {@code identifier}. In this case it is expected that {@code
     * identifier} will be processed (parsed) into internal components. The only expected purpose of this constructor is deserialization
     * case.
     *
     * @param username   username of user in scope of consumer.
     * @param identifier identifier of user in scope of consumer.
     */
    public AuthenticationData(String username, String identifier) {
        this.username = username;
        processIdentifier(identifier);
    }

    /**
     * Returns user identifier of current authentication data, constructed from internal components. The only expected purpose of this
     * method is deserialization case.
     *
     * @return user identifier of current authentication data.
     */
    public abstract String getIdentifier();

    /**
     * Process (parses) {@code identifier} into internal components.
     *
     * @param identifier identifier of user in scope of consumer.
     */
    protected abstract void processIdentifier(String identifier);
}
