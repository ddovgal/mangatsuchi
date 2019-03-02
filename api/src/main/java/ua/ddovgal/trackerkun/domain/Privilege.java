package ua.ddovgal.trackerkun.domain;

/**
 * Privilege allowing to perform certain actions.
 */
public enum Privilege {
    /**
     * Common user.
     */
    COMMON_USER,
    /**
     * User that have more permissions than common user.
     */
    PRIVILEGED_USER,
    /**
     * Creator, It's me :) Has god abilities in this scope.
     */
    CREATOR
}
