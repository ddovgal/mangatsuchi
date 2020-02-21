package ua.ddovgal.mangamonitoring.domain;

/**
 * Privilege that specifies scopes of allowed actions certain user can do.
 */
public enum Privilege {

    /**
     * Common user, common abilities.
     */
    COMMON_USER,

    /**
     * User that have more permissions and abilities than common user.
     */
    PRIVILEGED_USER,

    /**
     * Creator, It's me :)
     * <p>
     * Can create and destroy, has god abilities.
     */
    CREATOR
}
