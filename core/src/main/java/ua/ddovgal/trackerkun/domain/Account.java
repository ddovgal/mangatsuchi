package ua.ddovgal.trackerkun.domain;

import lombok.Data;

import java.util.List;
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
     * commonAuthDataList documentation
     */
    private List<CommonAuthData> commonAuthDataList;
}
