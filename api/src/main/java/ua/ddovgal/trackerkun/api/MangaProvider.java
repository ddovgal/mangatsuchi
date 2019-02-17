package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.Chapter;
import ua.ddovgal.trackerkun.domain.ChaptersCondition;
import ua.ddovgal.trackerkun.domain.Manga;

import java.util.List;

/**
 * MangaProvider documentation
 */
public interface MangaProvider {
    /**
     * getName documentation
     *
     * @return
     */
    String getName();

    /**
     * getMainLanguage documentation
     *
     * @return
     */
    String getMainLanguage();

    /**
     * searchByTitle documentation
     *
     * @param title
     * @param offset
     * @param size
     * @return
     */
    List<Manga> searchByTitle(String title, int offset, int size);

    /**
     * getMangaChaptersCondition documentation
     *
     * @param manga
     * @return
     */
    ChaptersCondition getMangaChaptersCondition(Manga manga);

    /**
     * getGlobalIdentifier documentation
     *
     * @param manga
     * @return
     */
    String getGlobalIdentifier(Manga manga);

    /**
     * getGlobalIdentifier documentation
     *
     * @param manga
     * @return
     */
    String getGlobalIdentifier(Chapter manga);
}
