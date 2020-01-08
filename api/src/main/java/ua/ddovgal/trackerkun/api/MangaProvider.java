package ua.ddovgal.trackerkun.api;

import java.util.List;

import ua.ddovgal.trackerkun.domain.Chapter;
import ua.ddovgal.trackerkun.domain.ChaptersCondition;
import ua.ddovgal.trackerkun.domain.Manga;
import ua.ddovgal.trackerkun.exception.MangaProviderException;

/**
 * Representation of service which provides manga. It is not just simple descriptor of the resource. Classes implementing this interface
 * provides full functionality of that resource, like some kind of service. So that classes describes manga provider they are representing
 * and how to interact with them.
 */
public interface MangaProvider {

    /**
     * Get human-readable manga provider name.
     *
     * @return manga provider name.
     */
    String getName();

    /**
     * Get general provider manga language.
     *
     * @return general provider manga language.
     */
    String getMainLanguage();

    /**
     * Search for manga by it's title with specific offset in found list and size of list to return. If the size of found list is smaller
     * than the {@code size} than returned list will have that found-list size.
     *
     * @param title  title of manga to search.
     * @param offset offset in list that will be returned by provider as a response for search-by-name request.
     * @param size   size of list to return.
     *
     * @throws MangaProviderException in case provider didn't successfully returned the result, or it could not be consumed.
     */
    List<Manga> searchByTitle(String title, int offset, int size) throws MangaProviderException;

    /**
     * Get current condition of manga on resource. This method returns most actual manga chapters condition, not by retrieving from some
     * kind of storage, but an actually asking resource about chapters current condition.
     *
     * @param manga manga to find chapters condition.
     *
     * @return most actual manga chapters condition.
     *
     * @throws MangaProviderException in case provider didn't successfully returned the result, or it could not be consumed.
     */
    ChaptersCondition getChaptersCondition(Manga manga) throws MangaProviderException;

    /**
     * Get the global identifier of the manga. This identifier can identify manga not only in the context of the current provider. In fact,
     * this identifier contains in addition to the manga identifier also the provider identifier, allowing to globally identify the manga.
     *
     * @param manga manga to get the global identifier.
     *
     * @return global identifier of the manga.
     */
    String getGlobalIdentifier(Manga manga);

    /**
     * Get the global identifier of the chapter. This identifier can identify chapter not only in the context of it's manga or manga
     * provider. In fact, this identifier contains in addition to the chapter identifier also the provider and optionally manga identifiers,
     * allowing to globally identify the chapter.
     *
     * @param manga   manga which chapter belongs to.
     * @param chapter chapter to get the global identifier.
     *
     * @return global identifier of the chapter.
     */
    String getGlobalIdentifier(Manga manga, Chapter chapter);
}
