package ua.ddovgal.trackerkun.api;

import ua.ddovgal.trackerkun.domain.Account;
import ua.ddovgal.trackerkun.domain.CommonAuthData;

import java.util.List;
import java.util.function.Function;

/**
 * AccountService documentation
 */
public interface AccountService {
    /**
     * getAuthDataOfConsumer documentation
     *
     * @param account
     * @param consumerAuthDataClass
     * @param converter
     * @param <T>
     * @return
     */
    <T extends ConsumerAuthData> T getAuthDataOfConsumer(Account account, Class<T> consumerAuthDataClass, Function<CommonAuthData, T> converter);

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
     * getAllAccountsAuthDataOfConsumer documentation
     *
     * @param consumerAuthDataClass
     * @param converter
     * @param <T>
     * @return
     */
    <T extends ConsumerAuthData> List<T> getAllAccountsAuthDataOfConsumer(Class<T> consumerAuthDataClass, Function<CommonAuthData, T> converter);
}
