package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.Account;
import ua.ddovgal.trackerkun.domain.Chapter;
import ua.ddovgal.trackerkun.domain.Manga;
import ua.ddovgal.trackerkun.domain.SystemNotification;

/**
 * Service to send notifications to certain consumer system. Contains methods for sending notification for each event type.
 */
public interface ConsumerNotifier {

    /**
     * Send system {@code notification} to provided {@code account}. It is expected that data for system notifications will be provided by
     * some privileged user, by sending manually written message, hence notification of this type is triggered by real user rather then
     * system.
     *
     * @param account      account of user to receive notification.
     * @param notification system notification to send.
     */
    void sendSystemNotification(Account account, SystemNotification notification);

    /**
     * Notify provided {@code account} that some number of new (really new, not just update) {@code manga}'s chapters released.
     *
     * @param account       account of user to receive notification.
     * @param latestChapter latest among released chapters.
     * @param manga         notification associated manga.
     */
    void notifyChaptersReleased(Account account, Chapter latestChapter, Manga manga);

    /**
     * Notify provided {@code account} that new chapter released, but by some indicators it seems, that it's just some kind of update.
     *
     * @param account        account of user to receive notification.
     * @param updatedChapter notification associated updated chapter.
     * @param manga          notification associated manga.
     */
    void notifyLastChapterUpdated(Account account, Chapter updatedChapter, Manga manga);

    /**
     * Notify provided {@code account} that some number of chapters were deleted.
     *
     * @param account account of user to receive notification.
     * @param manga   notification associated manga.
     */
    void notifyChaptersDeleted(Account account, Manga manga);

    /**
     * Notify provided {@code account} that in provided {@code manga}'s {@link MangaProvider} has no chapters, and now they have appeared.
     *
     * @param account       account of user to receive notification.
     * @param latestChapter latest among appeared chapters.
     * @param manga         notification associated manga.
     */
    void notifyChaptersAppeared(Account account, Chapter latestChapter, Manga manga);

    /**
     * Notify provided {@code account} that in provided {@code manga} last chapter was not changed, but number of chapters has been
     * increased. Hence chapters were inserted somewhere within.
     *
     * @param account account of user to receive notification.
     * @param manga   notification associated manga.
     */
    void notifyChaptersAdded(Account account, Manga manga);
}
