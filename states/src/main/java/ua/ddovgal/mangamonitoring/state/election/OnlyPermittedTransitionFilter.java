package ua.ddovgal.mangamonitoring.state.election;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ua.ddovgal.mangamonitoring.api.AccountService;
import ua.ddovgal.mangamonitoring.domain.Account;
import ua.ddovgal.mangamonitoring.domain.Privilege;
import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;

/**
 * Filter implementation which rejects all transitions that require {@link Privilege} with {@link Privilege#getAccessLevel()} higher than
 * the provided {@link Impact#getInitiatorAuthData()} assigned {@link Account} has.
 */
@Slf4j
@RequiredArgsConstructor
public class OnlyPermittedTransitionFilter<I extends Impact, R> implements TransitionFilter<I, R> {

    private final AccountService accountService;

    @Override
    public List<DepartureDefinedTransition<?, ?, I, R>> filter(I impact,
                                                               State state,
                                                               List<DepartureDefinedTransition<?, ?, I, R>> transitions) {
        // account is optional because there is a possibility of the impact initiator never acted before
        Optional<Account> account = accountService.getAssociatedAccount(impact.getInitiatorAuthData());
        log.info("account={}", account.map(Account::toString).orElse(null));
        // hence, default privilege is used for that case
        Privilege privilege = account.map(Account::getPrivilege).orElse(Privilege.COMMON_USER);
        return transitions
            .stream()
            .filter(transition -> privilege.getAccessLevel() >= transition.getRequiredPrivilege().getAccessLevel())
            .collect(Collectors.toList());
    }
}
