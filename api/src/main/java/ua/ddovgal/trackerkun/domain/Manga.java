package ua.ddovgal.trackerkun.domain;

import java.util.UUID;

import lombok.Data;

import ua.ddovgal.trackerkun.api.MangaProvider;

/**
 * Class describing manga.
 */
@Data
public class Manga {

    /**
     * Manga's ID.
     */
    private UUID id;
    /**
     * Manga's title.
     */
    private String title;
    /**
     * Provider of manga. Resource to which manga belongs.
     */
    private MangaProvider provider;
    /**
     * Manga's language.
     */
    private String language;
    /**
     * Manga's identifier in scope of {@link MangaProvider}.
     */
    private String sourceIdentifier;
}
