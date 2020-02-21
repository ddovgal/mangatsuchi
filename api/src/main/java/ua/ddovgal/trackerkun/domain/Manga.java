package ua.ddovgal.trackerkun.domain;

import java.util.UUID;

import lombok.Data;

import ua.ddovgal.trackerkun.api.MangaProvider;

/**
 * Pojo that describes manga.
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
     * Provider of manga, resource to which manga belongs.
     */
    private MangaProvider provider;

    /**
     * Manga language.
     */
    private String language;

    /**
     * Manga identifier in {@link MangaProvider}'s scope.
     */
    private String sourceIdentifier;
}
