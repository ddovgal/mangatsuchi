package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.Account;
import ua.ddovgal.trackerkun.domain.Manga;

import java.util.List;

/**
 * SubscriptionService documentation
 */
public interface SubscriptionService {
    /**
     * subscribe documentation
     *
     * @param account
     * @param manga
     */
    void subscribe(Account account, Manga manga);

    /**
     * unsubscribe documentation
     *
     * @param account
     * @param manga
     */
    void unsubscribe(Account account, Manga manga);

    /**
     * getSubscriptionList documentation
     *
     * @param account
     * @return
     */
    List<Manga> getSubscriptionList(Account account);

    /**
     * getSubscribersList documentation
     *
     * @param manga
     * @return
     */
    List<Account> getSubscribersList(Manga manga);
}
