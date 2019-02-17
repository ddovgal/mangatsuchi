package ua.ddovgal.trackerkun.api;

/**
 * ConsumerAuthData documentation
 */
public abstract class ConsumerAuthData {
    /**
     * ConsumerAuthData documentation
     * need this for constructors different to {@link #ConsumerAuthData(String)} would be able to exist
     */
    public ConsumerAuthData() {
    }

    /**
     * ConsumerAuthData documentation
     *
     * @param commonForm
     */
    public ConsumerAuthData(String commonForm) {
        processCommonForm(commonForm);
    }

    /**
     * processCommonForm documentation
     *
     * @param commonForm
     */
    abstract void processCommonForm(String commonForm);

    /**
     * toCommonForm documentation
     *
     * @return
     */
    public abstract String toCommonForm();
}
