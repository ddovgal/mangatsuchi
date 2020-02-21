package ua.ddovgal.trackerkun.domain;

import lombok.Data;

import ua.ddovgal.trackerkun.api.MangaProvider;

/**
 * Pojo that describes chapter of some {@link Manga}.
 */
@Data
public class Chapter {

    /**
     * Chapter title.
     */
    private String title;

    /**
     * Chapter number. Because some mangas have numbering in their own format, this field is a {@link String} type. Could be in any numeric
     * (or even not numeric) format. Describes number/position of chapter in its manga's scope.
     */
    private String releaseNumber;

    /**
     * Chapter identifier. Could be identifier in scope of just manga or whole manga provider. It depends on {@link MangaProvider} of the
     * {@link Manga} chapter belongs to.
     */
    private String sourceIdentifier;
}
