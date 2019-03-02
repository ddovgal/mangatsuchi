package ua.ddovgal.trackerkun.domain;

/**
 * Type of some occurred manga-related event.
 */
public enum EventType {
    SYSTEM("Common notification which more like an announcement, not related to chapter or manga"),
    NEW_CHAPTER_RELEASED("When new (really new, not just update) chapter released"),
    LAST_CHAPTER_UPDATE("When new chapter released, but by some indicators it seems, that it's just some kind of update"),
    NO_CHAPTERS("When subscribed to manga that have no chapters"),
    CHAPTERS_REMOVED("When some number of chapters exists, but then they were deleted"),
    CHAPTERS_APPEARED("When there was no chapters and then they appeared"),
    CHAPTERS_INSIDE_INSERTION("When last chapter was nod changed, but chapter's quantity was increased");

    /**
     * Description of event.
     */
    private final String description;

    /**
     * Create {@link EventType} with description.
     *
     * @param description description of event.
     */
    EventType(String description) {
        this.description = description;
    }}
