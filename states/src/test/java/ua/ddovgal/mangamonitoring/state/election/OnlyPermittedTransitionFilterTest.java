package ua.ddovgal.mangamonitoring.state.election;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.ddovgal.mangamonitoring.api.AccountService;
import ua.ddovgal.mangamonitoring.api.AuthenticationData;
import ua.ddovgal.mangamonitoring.domain.Account;
import ua.ddovgal.mangamonitoring.domain.Privilege;
import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static ua.ddovgal.mangamonitoring.state.utils.DescriptorUtils.descriptorBuilder;

@ExtendWith(MockitoExtension.class)
class OnlyPermittedTransitionFilterTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private OnlyPermittedTransitionFilter<Impact, Object> filter;

    @Test
    void filter_happyPath(@Mock Impact impact, @Mock State state) {
        AuthenticationData authData = mock(AuthenticationData.class);
        when(impact.getInitiatorAuthData()).thenReturn(authData);
        Account account = mock(Account.class);
        Privilege privilege = Privilege.PRIVILEGED_USER;
        when(account.getPrivilege()).thenReturn(privilege);
        when(accountService.getAssociatedAccount(same(authData))).thenReturn(Optional.of(account));
        List<DepartureDefinedTransition<?, ?, Impact, Object>> transitions = transitionsWithPrivileges(Privilege.COMMON_USER,
                                                                                                       Privilege.PRIVILEGED_USER,
                                                                                                       Privilege.CREATOR);

        List<DepartureDefinedTransition<?, ?, Impact, Object>> filteredList = filter.filter(impact, state, transitions);

        assertThat(filteredList)
            .hasSize(2)
            .extracting(DepartureDefinedTransition::getRequiredPrivilege)
            .allMatch(p -> p.getAccessLevel() <= privilege.getAccessLevel());
        verifyNoMoreInteractions(accountService);
    }

    @Test
    void filter_noAssociatedAccountFound_commonUserPrivilegeUsed(@Mock Impact impact, @Mock State state) {
        AuthenticationData authData = mock(AuthenticationData.class);
        when(impact.getInitiatorAuthData()).thenReturn(authData);
        when(accountService.getAssociatedAccount(same(authData))).thenReturn(Optional.empty());
        List<DepartureDefinedTransition<?, ?, Impact, Object>> transitions = transitionsWithPrivileges(Privilege.COMMON_USER,
                                                                                                       Privilege.PRIVILEGED_USER,
                                                                                                       Privilege.CREATOR);

        List<DepartureDefinedTransition<?, ?, Impact, Object>> filteredList = filter.filter(impact, state, transitions);

        assertThat(filteredList)
            .singleElement()
            .extracting(DepartureDefinedTransition::getRequiredPrivilege)
            .isSameAs(Privilege.COMMON_USER);
        verifyNoMoreInteractions(accountService);
    }

    @ParameterizedTest(name = "For {index} privileges set")
    @MethodSource("source_filter_differentAccessLevelsHandling")
    void filter_differentAccessLevelsHandling(Privilege privilege, int filteredListSize, @Mock Impact impact, @Mock State state) {
        AuthenticationData authData = mock(AuthenticationData.class);
        when(impact.getInitiatorAuthData()).thenReturn(authData);
        Account account = mock(Account.class);
        when(account.getPrivilege()).thenReturn(Privilege.PRIVILEGED_USER);
        when(accountService.getAssociatedAccount(same(authData))).thenReturn(Optional.of(account));
        List<DepartureDefinedTransition<?, ?, Impact, Object>> transitions = transitionsWithPrivileges(privilege);

        List<DepartureDefinedTransition<?, ?, Impact, Object>> filteredList = filter.filter(impact, state, transitions);

        assertThat(filteredList).hasSize(filteredListSize);
        verifyNoMoreInteractions(accountService);
    }

    private static Stream<Arguments> source_filter_differentAccessLevelsHandling() {
        return Stream.of(
            Arguments.of(Privilege.CREATOR, 0),         // required access level is higher
            Arguments.of(Privilege.PRIVILEGED_USER, 1), // required access level is equal
            Arguments.of(Privilege.COMMON_USER, 1)      // required access level is lower
        );
    }

    private static List<DepartureDefinedTransition<?, ?, Impact, Object>> transitionsWithPrivileges(Privilege... privileges) {
        return Arrays.stream(privileges).map(OnlyPermittedTransitionFilterTest::withPrivilege).collect(Collectors.toList());
    }

    private static DepartureDefinedTransition<State, State, Impact, Object> withPrivilege(Privilege privilege) {
        TransitionDescriptor<State, State, Impact, Object> descriptor = descriptorBuilder(State.class, State.class)
            .requiredPrivilege(privilege)
            .build();
        return new DepartureDefinedTransition<>(descriptor, mock(State.class));
    }
}