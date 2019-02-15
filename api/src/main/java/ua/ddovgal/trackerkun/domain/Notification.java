package ua.ddovgal.trackerkun.domain;

import lombok.Data;

/**
 * Notification
 */
@Data
public class Notification {
    /**
     * eventType
     */
    private EventType eventType;
    /**
     * manga
     */
    private Manga manga;
    /**
     * chapter
     */
    private Chapter chapter;
}
