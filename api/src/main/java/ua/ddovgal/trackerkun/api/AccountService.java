package ua.ddovgal.trackerkun.api;

import java.util.Optional;
import java.util.stream.Stream;

import ua.ddovgal.trackerkun.domain.Account;

/**
 * Service to provide {@link Account} related operations.
 */
public interface AccountService {

    /**
     * Create completely new account with provided {@code authData}.
     *
     * @param authData auth data which identifies a user in consumer scope.
     *
     * @return new created account.
     */
    Account registerNewAccount(AuthenticationData authData);

    /**
     * Find possibly existing associated account by provided {@code authData}.
     *
     * @param authData auth data which identifies the user in consumer scope.
     *
     * @return optional account.
     */
    Optional<Account> getAssociatedAccount(AuthenticationData authData);

    /**
     * Get the stream of all active accounts.
     *
     * @return all active accounts stream.
     */
    Stream<Account> getAllAccounts();
}
