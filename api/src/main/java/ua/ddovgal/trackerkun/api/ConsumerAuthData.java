package ua.ddovgal.trackerkun.api;

/**
 * ConsumerAuthData documentation
 */
public abstract class ConsumerAuthData {

    private String username;

    /**
     * ConsumerAuthData documentation
     * need this for constructors different to {@link #ConsumerAuthData(String, String)} would be able to exist
     */
    public ConsumerAuthData(String username) {
        this.username = username;
    }

    /**
     * ConsumerAuthData documentation
     *
     * @param username
     * @param identifier
     */
    public ConsumerAuthData(String username, String identifier) {
        this.username = username;
        processIdentifier(identifier);
    }

    /**
     * getUsername documentation
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * processIdentifier documentation
     *
     * @param identifier
     */
    abstract void processIdentifier(String identifier);

    /**
     * getIdentifier documentation
     *
     * @return
     */
    public abstract String getIdentifier();
}
