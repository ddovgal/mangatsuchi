package ua.ddovgal.trackerkun.domain;

import lombok.Getter;
import ua.ddovgal.trackerkun.api.ConsumerAuthData;

/**
 * CommonAuthData
 */
public final class CommonAuthData {

    /**
     * consumerType
     */
    private Class<? extends ConsumerAuthData> consumerType;

    /**
     * commonFormIdentifier
     */
    @Getter
    private String commonFormIdentifier;

    /**
     * CommonAuthData
     *
     * @param consumerType
     * @param commonFormIdentifier
     */
    public CommonAuthData(Class<? extends ConsumerAuthData> consumerType, String commonFormIdentifier) {
        this.consumerType = consumerType;
        this.commonFormIdentifier = commonFormIdentifier;
    }

    /**
     * isRepresentationOf
     *
     * @param consumerAuthDataClass
     * @return
     */
    public boolean isRepresentationOf(Class<? extends ConsumerAuthData> consumerAuthDataClass) {
        return this.consumerType.equals(consumerAuthDataClass);
    }
}
