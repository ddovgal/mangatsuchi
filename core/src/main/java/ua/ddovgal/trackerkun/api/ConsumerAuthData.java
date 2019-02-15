package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.CommonAuthData;

/**
 * ConsumerAuthData documentation
 */
public abstract class ConsumerAuthData {
    /**
     * ConsumerAuthData documentation
     * need this to different to Subclass(CommonAuthData commonForm) could be
     */
    public ConsumerAuthData() {
    }

    /**
     * ConsumerAuthData documentation
     *
     * @param commonForm
     */
    public ConsumerAuthData(CommonAuthData commonForm) {
        processCommonForm(commonForm);
    }

    /**
     * processCommonForm documentation
     *
     * @param commonForm
     */
    abstract void processCommonForm(CommonAuthData commonForm);

    /**
     * toCommonForm documentation
     *
     * @return
     */
    public abstract CommonAuthData toCommonForm();
}
