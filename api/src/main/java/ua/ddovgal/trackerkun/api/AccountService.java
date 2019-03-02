package ua.ddovgal.trackerkun.api;

import java.util.Optional;
import java.util.stream.Stream;

import ua.ddovgal.trackerkun.domain.Account;

/**
 * Service to provide {@link Account} related operations.
 */
public interface AccountService {

    /**
     * Create a new account with provided {@link ConsumerAuthData}.
     *
     * @param consumerAuthData auth data which identifies a user in consumer's system.
     *
     * @return new created account.
     */
    Account createAccountWithAuthData(ConsumerAuthData consumerAuthData);

    //region If there will be a multi-ConsumerAuthData, than uncomment
    //    /**
    //     * Attaches additional {@link ConsumerAuthData} to existing {@link Account}.
    //     *
    //     * @param accountId        existing account's ID.
    //     * @param consumerAuthData attached additional auth data which identifies the user in consumer's system.
    //     *
    //     * @throws InvalidIdProvidedException in case when there is no {@link Account} with {@code accountId} ID.
    //     */
    //    void attachAuthData(UUID accountId, ConsumerAuthData consumerAuthData) throws InvalidIdProvidedException;
    //endregion

    /**
     * Find possibly existing account by auth data identifying the user in consumer's system.
     *
     * @param consumerAuthData auth data which identifies the user in consumer's system.
     *
     * @return optional account.
     */
    Optional<Account> getAccountOfAuthData(ConsumerAuthData consumerAuthData);

    /**
     * Get {@link Stream<Account>} of all active accounts.
     *
     * @return all active accounts.
     */
    Stream<Account> getAllAccounts();
}
