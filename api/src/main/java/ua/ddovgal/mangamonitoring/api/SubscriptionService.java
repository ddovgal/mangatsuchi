package ua.ddovgal.mangamonitoring.api;

import java.util.List;
import java.util.UUID;

import ua.ddovgal.mangamonitoring.domain.Account;
import ua.ddovgal.mangamonitoring.domain.Manga;
import ua.ddovgal.mangamonitoring.exception.InvalidIdProvidedException;

/**
 * Service that provides ability to subscription related operations.
 * <p>
 * The subscription can be understood as request of the {@link Account} to receive the notifications related to the certain {@link Manga}.
 * In stored data scope words it's just a pair of {@link Account} and {@link Manga}, additional properties are not so important.
 */
public interface SubscriptionService {

    /**
     * Subscribes {@link Account} of provided {@code accountId} to provided {@code manga}. In other words it creates subscription of {@link
     * Account} with {@code accountId} to {@code manga}.
     *
     * @param accountId id of account which is subscribing.
     * @param manga     manga subscribe to.
     *
     * @throws InvalidIdProvidedException in case when there is no {@link Account} with {@code accountId} ID.
     */
    void subscribe(UUID accountId, Manga manga) throws InvalidIdProvidedException;

    /**
     * Unsubscribes {@link Account} of provided {@code accountId} from provided {@code manga}. In other words it's an opposite operation to
     * {@link SubscriptionService#subscribe(UUID, Manga)} method.
     *
     * @param accountId id of account which is unsubscribing.
     * @param manga     manga unsubscribe to.
     *
     * @throws InvalidIdProvidedException in case when there is no {@link Account} with {@code accountId} ID.
     * @see SubscriptionService#subscribe(UUID, Manga) for detailed description of subscription action.
     */
    void unsubscribe(UUID accountId, Manga manga) throws InvalidIdProvidedException;

    /**
     * Returns all manga {@link Account} with {@code accountId} is subscribed for.
     *
     * @param accountId id of account.
     *
     * @return list of manga account subscribed for.
     *
     * @throws InvalidIdProvidedException in case when there is no {@link Account} with {@code accountId} ID.
     */
    List<Manga> getSubscribedManga(UUID accountId) throws InvalidIdProvidedException;
}
