package ua.ddovgal.trackerkun.domain;

import lombok.Data;
import ua.ddovgal.trackerkun.api.MangaProvider;

import java.util.UUID;

/**
 * Manga
 */
@Data
public class Manga {
    /**
     * id
     */
    private UUID id;
    /**
     * title
     */
    private String title;
    /**
     * provider
     */
    private MangaProvider provider;
    /**
     * language
     */
    private String language;
    /**
     * sourceIdentifier
     */
    private String sourceIdentifier;
}
