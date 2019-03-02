package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.Account;
import ua.ddovgal.trackerkun.domain.Notification;

/**
 * Service to send notifications to certain consumer system.
 */
public interface ConsumerNotifier {

    /**
     * Notify {@code account} of {@code notification}.
     *
     * @param account      account of user to receive notification.
     * @param notification notification to send.
     */
    void notify(Account account, Notification notification);
}
