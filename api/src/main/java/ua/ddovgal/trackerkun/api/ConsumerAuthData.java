package ua.ddovgal.trackerkun.api;

/**
 * Authentication data describing account's user in scope of certain consumer system.
 */
public abstract class ConsumerAuthData {

    /**
     * Username of user in scope of current consumer system.
     */
    private String username;

    /**
     * Create new authentication data object with username. In this case it is assumed that {@code username} is an identifier. It is also
     * expected that subclasses with constructors different to {@link #ConsumerAuthData(String, String)} will call this constructor.
     *
     * @param username username of user in scope of consumer system.
     */
    public ConsumerAuthData(String username) {
        this.username = username;
    }

    /**
     * Create new authentication data object with username and from identifier. In this case it is expected that {@code identifier} will be
     * processed/parsed into internal components.
     *
     * @param username   username of user in scope of consumer system.
     * @param identifier identifier of user in scope of consumer system.
     */
    public ConsumerAuthData(String username, String identifier) {
        this.username = username;
        processIdentifier(identifier);
    }

    /**
     * Get username of current authentication data.
     *
     * @return username of current authentication data.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get user's identifier of current authentication data.
     *
     * @return user's identifier of current authentication data.
     */
    public abstract String getIdentifier();

    /**
     * Process/parse {@code identifier} into internal components.
     *
     * @param identifier identifier of user in scope of consumer system.
     */
    abstract void processIdentifier(String identifier);
}
