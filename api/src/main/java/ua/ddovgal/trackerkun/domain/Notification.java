package ua.ddovgal.trackerkun.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ua.ddovgal.trackerkun.api.ConsumerNotifier;

/**
 * Notification that will be send to user by certain {@link ConsumerNotifier}.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Notification {

    /**
     * Type of occurred event which to notify.
     */
    private EventType eventType;
    /**
     * Associated manga. Could be {@code null} if {@link #eventType} is {@link EventType#SYSTEM}.
     */
    private Manga manga;
    /**
     * Associated chapter. Could be {@code null}. For example when {@link #eventType} is {@link EventType#SYSTEM}, or there are no chapters
     * in this manga.
     */
    private Chapter chapter;

    /**
     * Optional notification message which is used and not {@code null} only when {@link #eventType} is {@link EventType#SYSTEM}. Expected
     * that in that case only this message is describing notification and both manga and chapter are {@code null} and unused.
     */
    private String optionalMessage;

    /**
     * Create notification of not {@link EventType#SYSTEM} type from not null manga and nullable chapter.
     *
     * @param eventType type of occurred event.
     * @param manga     notification associated manga.
     * @param chapter   notification associated chapter.
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
     * Create notification of {@link EventType#SYSTEM} type with notification message.
     *
     * @param message notification message.
     */
    public Notification(String message) {
        this.optionalMessage = message;
        eventType = EventType.SYSTEM;
    }
}
