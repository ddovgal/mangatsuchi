package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.Account;
import ua.ddovgal.trackerkun.domain.Notification;

/**
 * NotificationConsumer documentation
 */
public interface NotificationConsumer {
    /**
     * send documentation
     *
     * @param account
     * @param notification
     */
    void send(Account account, Notification notification);

    /**
     * ready documentation
     */
    void ready();
}
