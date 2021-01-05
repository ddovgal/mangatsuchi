package ua.ddovgal.mangamonitoring.api;

import java.util.List;

import ua.ddovgal.mangamonitoring.domain.Chapter;
import ua.ddovgal.mangamonitoring.domain.ChaptersCondition;
import ua.ddovgal.mangamonitoring.domain.Manga;
import ua.ddovgal.mangamonitoring.exception.MangaProviderException;

/**
 * Representation of any kind of resource that provides manga.
 * <p/>
 * It is not just simple descriptor of the resource. Implementations of this interface provides full functionality of that resource, like
 * some kind of service. So that implementations describes manga provider they are representing and how to interact with them.
 */
public interface MangaProvider {

    /**
     * Returns human-readable manga provider name.
     *
     * @return manga provider name.
     */
    String getName();

    /**
     * Returns general provider manga language.
     *
     * @return general provider manga language.
     */
    String getMainLanguage();

    /**
     * Searches for manga by its title with specific {@code offset} in found list wherein specifying the {@code size} of the return list. If
     * the size of found list is less than the expected than returned list will have that found-list size.
     *
     * @param title  title of manga to search.
     * @param offset offset in the list that will be returned by provider as a response for search-by-name request.
     * @param size   expected size of list to receive.
     *
     * @throws MangaProviderException in case provider didn't successfully respond or if the result couldn't be consumed.
     */
    List<Manga> searchByTitle(String title, int offset, int size) throws MangaProviderException;

    /**
     * Returns current condition of the {@code manga}. Returns most actual chapters condition, not by retrieving it from some kind of
     * storage, but an actually asking resource about most actual condition.
     *
     * @param manga manga to find chapters condition.
     *
     * @return most actual manga chapters condition.
     *
     * @throws MangaProviderException in case provider didn't successfully respond or if the result couldn't be consumed.
     */
    ChaptersCondition getChaptersCondition(Manga manga) throws MangaProviderException;

    /**
     * Returns the global identifier of the manga. This identifier can identify manga not only in the context of the current provider. In
     * fact, this identifier in addition to the manga's identifier also contains the provider's identifier, allowing to globally identify
     * the manga.
     *
     * @param manga manga to get the global identifier.
     *
     * @return global identifier of the manga.
     */
    String getGlobalIdentifier(Manga manga);

    /**
     * Returns the global identifier of the chapter. This identifier can identify chapter not only in the context of it's manga or manga
     * provider. In fact, this identifier in addition to the chapter's identifier also contains the provider's one and optionally manga's,
     * allowing to globally identify the chapter.
     *
     * @param manga   manga which chapter belongs to.
     * @param chapter chapter to get the global identifier.
     *
     * @return global identifier of the chapter.
     */
    String getGlobalIdentifier(Manga manga, Chapter chapter);
}
