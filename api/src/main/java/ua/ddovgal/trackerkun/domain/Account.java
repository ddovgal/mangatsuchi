package ua.ddovgal.trackerkun.domain;

import lombok.Data;
import ua.ddovgal.trackerkun.api.ConsumerAuthData;

import java.util.UUID;

/**
 * Account documentation
 */
@Data
public class Account {
    /**
     * id documentation
     */
    private UUID id;
    /**
     * privilege documentation
     */
    private Privilege privilege;
    /**
     * consumerAuthData documentation
     */
    private ConsumerAuthData consumerAuthData;
}
