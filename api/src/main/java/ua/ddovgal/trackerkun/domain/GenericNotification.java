package ua.ddovgal.trackerkun.domain;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import ua.ddovgal.trackerkun.api.ConsumerNotifier;

/**
 * Class describing generic notification. Represents any possible set of prepared data that will be send to user by certain {@link
 * ConsumerNotifier}. It is expected that notifications of this type will be constructed manually by some privileged user, rather than
 * program.
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericNotification {

    /**
     * Typed parts, notification consists of. Consistence of object classes and type safety is achieved by the way parts could be add.
     *
     * @see GenericNotification#addPart(PartType, Object)
     */
    private Map<String, Object> parts = new HashMap<>();

    /**
     * Creates new notification without any parts. Entry point of notification creation, starts builder-like fluent creation flow.
     *
     * @return new notification without any parts.
     */
    public static GenericNotification empty() {
        return new GenericNotification();
    }

    /**
     * Adds new part by provided {@code type} and {@code data}. Provides type safety, so we could be always sure that classes of part
     * objects are valid and expected.
     *
     * @param type type of part, describing data to add.
     * @param data data of part to add.
     * @param <T>  java class of part object.
     *
     * @return same notification object with filled parts.
     */
    public <T> GenericNotification addPart(PartType<T> type, T data) {
        parts.put(type.name(), data);
        return this;
    }

    /**
     * Find part of provided {@code type} and return it already casted to known {@link PartType#dataClass} of {@code type}.
     *
     * @param type type of part to get.
     * @param <T>  java class of returned part object.
     *
     * @return found data casted to {@link PartType#dataClass} of {@code type}.
     */
    public <T> Optional<T> getPartOfType(PartType<T> type) {
        return Optional.ofNullable(parts.get(type.name())).map(data -> type.dataClass().cast(data));
    }

    /**
     * Notification part type. Describes part by name and specifies java class of data. There are some common types listed inside as
     * constant values. Each certain consumer could define its own type, but it's a concern of consumer to keep new type name uniqueness
     * among all other. For such cases better to name types with pattern {@code <consumerName>_<typeName>} (example: TELEGRAM_IMAGE_ALBUM).
     */
    @Getter
    @Accessors(fluent = true)
    @AllArgsConstructor(staticName = "of")
    public static class PartType<T> {

        /**
         * Plain text.
         */
        public static final PartType<String> PLAIN_TEXT = of("PLAIN_TEXT", String.class);

        /**
         * URL for resource that could be treated as image (jpeg, png).
         */
        public static final PartType<URL> IMAGE_URL = of("IMAGE_URL", URL.class);

        /**
         * File that could be treated as an image (jpeg, png).
         */
        public static final PartType<File> IMAGE_FILE = of("IMAGE_FILE", File.class);

        /**
         * File of any possible extension.
         */
        public static final PartType<File> FILE = of("FILE", File.class);

        private String name;
        private Class<T> dataClass;
    }
}
