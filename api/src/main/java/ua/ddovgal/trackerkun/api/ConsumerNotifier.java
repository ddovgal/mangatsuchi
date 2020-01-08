package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.Chapter;
import ua.ddovgal.trackerkun.domain.GenericNotification;
import ua.ddovgal.trackerkun.domain.Manga;

/**
 * Service to send notifications to certain consumer. Contains methods for sending each notification type.
 *
 * @param <T> type of {@link AuthenticationData} current notifier works with.
 */
public interface ConsumerNotifier<T extends AuthenticationData> {
    //    /**
    //     * Send plain test message to provided {@code account}. Because of it's simplicity it is expected that every possible consumer will be
    //     * able to provide this type of notification. Intended to be used for something like fallback type of notification or some general
    //     * purpose notification.
    //     *
    //     * @param account account of user to receive notification.
    //     * @param message text to send.
    //     */
    //    void notifyWithPlainText(Account account, String message);
    //
    //    /**
    //     * Send prepared {@code message} to provided {@code account}. This method intended to just forward received message, hence {@code
    //     * message} should be already prepared for send and known by certain notifier class.
    //     *
    //     * @param <T>     type of message objects for certain notifier.
    //     * @param account account of user to receive notification.
    //     * @param message message object to send.
    //     */
    //    <T> void notifyWithMessage(Account account, T message);
    /**
     * Send generic {@code notification} to user by provided {@code authData}. It is expected that data for generic notifications will be
     * provided by some privileged user, by sending manually written message, hence notification of this type is triggered by real user
     * rather then system. Also it is expected to use this method for system-related notifications only.
     *
     * @param authData     authentication data describing account to receive notification in the scope of a current notifier.
     * @param notification system notification to send.
     *
     * @see GenericNotification for more details about notification and it's propose.
     */
    void sendGenericNotification(T authData, GenericNotification notification);

    /**
     * Notify user by provided {@code authData} that some number of new (really new, not just updated) {@code manga} chapters released.
     *
     * @param authData      authentication data describing account to receive notification in the scope of a current notifier.
     * @param latestChapter latest among released chapters.
     * @param manga         notification associated manga.
     */
    void notifyChaptersReleased(T authData, Chapter latestChapter, Manga manga);

    /**
     * Notify user by provided {@code authData} that new chapter released, but by some indicators it seems, that it's just some kind of
     * update.
     *
     * @param authData       authentication data describing account to receive notification in the scope of a current notifier.
     * @param updatedChapter notification associated updated chapter.
     * @param manga          notification associated manga.
     */
    void notifyLastChapterUpdated(T authData, Chapter updatedChapter, Manga manga);

    /**
     * Notify user by provided {@code authData} that some number of chapters was deleted.
     *
     * @param authData authentication data describing account to receive notification in the scope of a current notifier.
     * @param manga    notification associated manga.
     */
    void notifyChaptersDeleted(T authData, Manga manga);

    /**
     * Notify user by provided {@code authData} that in provided {@code manga} {@link MangaProvider} didn't have chapters, and now they have
     * appeared.
     *
     * @param authData      authentication data describing account to receive notification in the scope of a current notifier.
     * @param latestChapter latest among appeared chapters.
     * @param manga         notification associated manga.
     */
    void notifyChaptersAppeared(T authData, Chapter latestChapter, Manga manga);

    /**
     * Notify user by provided {@code authData} that provided {@code manga} last chapter has not been changed, but number of chapters has
     * been increased. Hence chapters were inserted somewhere within.
     *
     * @param authData authentication data describing account to receive notification in the scope of a current notifier.
     * @param manga    notification associated manga.
     */
    void notifyChaptersAdded(T authData, Manga manga);
}
