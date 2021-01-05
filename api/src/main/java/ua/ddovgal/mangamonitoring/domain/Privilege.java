package ua.ddovgal.mangamonitoring.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Privilege that determines scope of allowed actions, certain user can do. Access level in other words.
 */
@RequiredArgsConstructor
public enum Privilege {

    /**
     * Common user, common abilities.
     */
    COMMON_USER(0),

    /**
     * User that have more permissions and abilities than common user.
     */
    PRIVILEGED_USER(1),

    /**
     * Creator, It's me :)
     * <p/>
     * Can create and destroy, has god abilities.
     */
    CREATOR(2);

    /**
     * Rank level of the privilege. The higher level for the stronger privilege and greater abilities.
     */
    @Getter
    private final int accessLevel;
}
