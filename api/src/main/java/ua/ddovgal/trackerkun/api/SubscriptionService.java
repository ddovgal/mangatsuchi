package ua.ddovgal.trackerkun.api;

import java.util.UUID;
import java.util.stream.Stream;

import ua.ddovgal.trackerkun.domain.Account;
import ua.ddovgal.trackerkun.domain.Manga;
import ua.ddovgal.trackerkun.exception.InvalidIdProvidedException;

/**
 * Service to provide subscription related operations. The subscription is meant as request of the {@link Account} to receive the
 * notifications related to {@link Manga}. In other words it's just a pair of {@link Account} and {@link Manga}, additional properties are
 * not so important.
 */
public interface SubscriptionService {

    /**
     * Subscribe to manga {@link Account}'s ID. In another words it's create subscription of {@link Account} with {@code accountId} to
     * {@code manga}.
     *
     * @param accountId id of account which is subscribing.
     * @param manga     manga subscribe to.
     *
     * @throws InvalidIdProvidedException in case when there is no {@link Account} with {@code accountId} ID.
     */
    void subscribe(UUID accountId, Manga manga) throws InvalidIdProvidedException;

    /**
     * Unsubscribe to manga {@link Account}'s ID. In another words it is operation opposite to {@link SubscriptionService#subscribe(UUID,
     * Manga)} method. Read detailed description of subscription action there.
     *
     * @param accountId id of account which is unsubscribing.
     * @param manga     manga unsubscribe to.
     *
     * @throws InvalidIdProvidedException in case when there is no {@link Account} with {@code accountId} ID.
     * @see SubscriptionService#subscribe(UUID, Manga).
     */
    void unsubscribe(UUID accountId, Manga manga) throws InvalidIdProvidedException;

    /**
     * Get all manga which {@link Account} with {@code accountId} is subscribed for.
     *
     * @param accountId ID of account.
     *
     * @return stream of manga which is account subscribed for.
     *
     * @throws InvalidIdProvidedException in case when there is no {@link Account} with {@code accountId} ID.
     */
    Stream<Manga> getSubscribedManga(UUID accountId) throws InvalidIdProvidedException;
}
