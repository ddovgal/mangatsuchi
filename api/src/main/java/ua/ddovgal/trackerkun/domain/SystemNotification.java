package ua.ddovgal.trackerkun.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

import ua.ddovgal.trackerkun.api.ConsumerNotifier;

/**
 * Class describing system notification that will be send to user by certain {@link ConsumerNotifier}. It is expected that notifications of
 * this type will be constructed manually by some privileged user, rather than program.
 */
@Data
@AllArgsConstructor
public class SystemNotification {

    /**
     * Typed parts, notification consists of.
     */
    private List<Part> parts;

    /**
     * Enum containing common, expected to be able to engaged by any {@link ConsumerNotifier} types.
     */
    public enum CommonPartType {
        /**
         * Plain text. Data class: {@link String}.
         */
        PLAIN_TEXT,
        /**
         * URL for resource that could be treated as image (jpeg, png). Data class: {@link java.net.URL}.
         */
        IMAGE_URL,
        /**
         * File that could be treated as an image (jpeg, png). Data class: {@link java.io.File}.
         */
        IMAGE_FILE,
        /**
         * File of any possible extension. Data class: {@link java.io.File}.
         */
        FILE
    }

    /**
     * Class describing typed part of notification, like plain text, image, file.
     */
    @Data
    @AllArgsConstructor
    public static class Part {

        /**
         * Notification part type. There are some common types, listed in {@link CommonPartType} enum, but each certain consumer system
         * could define and put data of its own type. For such cases better to name type with pattern {@code <consumerName>_<typeName>}.
         * Example: TELEGRAM_IMAGE_ALBUM
         */
        public String type;
        /**
         * Actual hold data.
         */
        public Object data;
    }
}
