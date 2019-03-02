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
