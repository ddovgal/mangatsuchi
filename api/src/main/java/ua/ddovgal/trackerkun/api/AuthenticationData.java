package ua.ddovgal.trackerkun.api;

/**
 * Authentication data describing user of some account in scope of certain consumer. All authentication data classes must be able to be
 * created from username and identifier set, to be able to deserialize.
 */
public abstract class AuthenticationData {

    /**
     * Username of user in scope of current consumer.
     */
    private String username;

    /**
     * Create new authentication data object with username. In this case it is assumed that {@code username} is an identifier too. It is
     * also expected that subclasses with constructors different to {@link #AuthenticationData(String, String)} will use supper call of this
     * particular constructor.
     *
     * @param username username of user in scope of consumer.
     */
    protected AuthenticationData(String username) {
        this.username = username;
    }

    /**
     * Create new authentication data object with username and from identifier. In this case it is expected that {@code identifier} will be
     * processed (parsed) into internal components. The only expected purpose of this constructor is deserialization.
     *
     * @param username   username of user in scope of consumer.
     * @param identifier identifier of user in scope of consumer.
     */
    public AuthenticationData(String username, String identifier) {
        this.username = username;
        processIdentifier(identifier);
    }

    /**
     * Get username of current authentication data.
     *
     * @return username of current authentication data.
     */
    //TODO: @Getter ?
    public String getUsername() {
        return username;
    }

    /**
     * Get user identifier of current authentication data, constructed from internal components. The only expected case for this method is
     * serialization.
     *
     * @return user identifier of current authentication data.
     */
    public abstract String getIdentifier();

    /**
     * Process (parse) {@code identifier} into internal components.
     *
     * @param identifier identifier of user in scope of consumer.
     */
    protected abstract void processIdentifier(String identifier);
}
