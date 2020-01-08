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
     * Manga ID.
     */
    private UUID id;

    /**
     * Manga title.
     */
    private String title;

    /**
     * Provider of manga. Resource to which manga belongs.
     */
    private MangaProvider provider;

    /**
     * Manga language.
     */
    private String language;

    /**
     * Manga identifier in scope of {@link MangaProvider}.
     */
    private String sourceIdentifier;
}
