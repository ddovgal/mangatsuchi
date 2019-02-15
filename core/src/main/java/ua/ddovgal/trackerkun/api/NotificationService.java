package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.Account;
import ua.ddovgal.trackerkun.domain.Notification;

/**
 * NotificationService documentation
 */
public interface NotificationService {
    /**
     * notify documentation
     *
     * @param account
     * @param notification
     */
    void notify(Account account, Notification notification);
}
