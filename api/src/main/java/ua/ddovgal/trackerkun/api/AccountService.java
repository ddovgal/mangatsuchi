package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.Account;

import java.util.stream.Stream;

/**
 * AccountService documentation
 */
public interface AccountService {
    /**
     * createAccountWithAuthData documentation
     *
     * @param consumerAuthData
     */
    void createAccountWithAuthData(ConsumerAuthData consumerAuthData);

    /**
     * addAuthData documentation
     *
     * @param account
     * @param consumerAuthData
     */
    void addAuthData(Account account, ConsumerAuthData consumerAuthData);

    /**
     * getAccountOfAuthData documentation
     *
     * @param consumerAuthData
     * @return
     */
    Account getAccountOfAuthData(ConsumerAuthData consumerAuthData);

    /**
     * getAllAccounts documentation
     *
     * @return
     */
    Stream<Account> getAllAccounts();
}
