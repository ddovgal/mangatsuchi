package ua.ddovgal.trackerkun.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Notification
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
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

    /**
     * Optional notification message which is used and not null only when {@link #eventType}
     * is {@link EventType#SYSTEM}.
     * Expected that in that case only this message is describing notification and both manga
     * and chapter are null and unused.
     */
    private String optionalMessage;

    /**
     * Notification
     *
     * @param eventType
     * @param manga
     * @param chapter
     */
    public Notification(EventType eventType, Manga manga, Chapter chapter) {
        if (eventType == EventType.SYSTEM) {
            throw new IllegalArgumentException(EventType.SYSTEM + " eventType is not allowed to be used with this constructor");
        }

        this.eventType = eventType;
        this.manga = manga;
        this.chapter = chapter;
    }

    /**
     * Notification
     *
     * @param optionalMessage
     */
    public Notification(String optionalMessage) {
        this.optionalMessage = optionalMessage;
        eventType = EventType.SYSTEM;
    }
}
