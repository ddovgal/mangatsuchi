package ua.ddovgal.mangamonitoring.state;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.AllArgsConstructor;
import lombok.Lombok;

import ua.ddovgal.mangamonitoring.api.AccountService;
import ua.ddovgal.mangamonitoring.api.AuthenticationData;
import ua.ddovgal.mangamonitoring.domain.Account;
import ua.ddovgal.mangamonitoring.domain.Privilege;
import ua.ddovgal.mangamonitoring.state.election.OnlyPermittedTransitionFilter;
import ua.ddovgal.mangamonitoring.state.election.OnlyTopByPriorityTransitionFilter;
import ua.ddovgal.mangamonitoring.state.election.SimpleTransitionElector;
import ua.ddovgal.mangamonitoring.state.election.TransitionElector;
import ua.ddovgal.mangamonitoring.state.election.TransitionFilter;
import ua.ddovgal.mangamonitoring.state.exception.TransitionElectionException;
import ua.ddovgal.mangamonitoring.state.exception.TransitionExecutionException;
import ua.ddovgal.mangamonitoring.state.graph.ClassMappingStateGraph;
import ua.ddovgal.mangamonitoring.state.graph.StateGraph;
import ua.ddovgal.mangamonitoring.state.transition.LambdaDescribedTransitionDescriptor.Builder;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;
import ua.ddovgal.mangamonitoring.state.utils.DescriptorUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static ua.ddovgal.mangamonitoring.state.utils.DescriptorUtils.descriptorBuilder;
import static ua.ddovgal.mangamonitoring.state.utils.States.BarState;
import static ua.ddovgal.mangamonitoring.state.utils.States.FooState;

@ExtendWith(MockitoExtension.class)
class SemiIntegrationTest {

    @Mock
    private StateDao mockStateDao;

    @Mock
    private AccountService mockAccountService;

    // AuthenticationData that will be used for each impact
    private static final AuthenticationData TEST_AUTH_DATA = mock(AuthenticationData.class);

    /**
     * <pre>
     * Forms the following base graph
     *
     *             +---------+
     *             |   NIB   |
     *             +----+----+
     *                  | 1
     *                  v
     *             +----+----+
     *         +-->+   FOO   +---+
     *         |   +---------+   | 2
     *         |                 |
     *         |                 |
     *       3 |   +---------+   |
     *         +---+   BAR   +<--+
     *             +---------+
     *
     * Generated with http://asciiflow.com/
     * </pre>
     */
    private static final List<TransitionDescriptor<?, ?, TestImpact, String>> DESCRIPTORS = List.of(
        // --- 1; NIB->FOO
        baseBuilder(NoInteractionsBeforeState.class, FooState.class)
            .impactResultBuilder((a, b, impact) -> impact.data + "NIB-FOO")
            .build(),

        // --- 2; FOO->BAR
        baseBuilder(FooState.class, BarState.class)
            .impactResultBuilder((a, b, impact) -> impact.data + "FOO-BAR")
            .build(),

        // --- 3; BAR->FOO
        baseBuilder(BarState.class, FooState.class)
            .impactResultBuilder((a, b, impact) -> impact.data + "BAR-FOO")
            .build()
    );

    // ======== test methods

    @SuppressWarnings("unchecked")
    @Test
    void simpleCycle_happyPath() throws TransitionElectionException, TransitionExecutionException {
        StateMachine<TestImpact, String> stateMachine = constructStateMachine(DESCRIPTORS);

        // define states of the ticks
        // 3 ticks: NIB->FOO->BAR->FOO
        when(mockStateDao.getState(same(TEST_AUTH_DATA))).thenReturn(
            Optional.empty(), // NoInteractionsBeforeState
            Optional.of(new FooState()),
            Optional.of(new BarState())
        );

        // define accounts that will be retrieved
        // the first try nothing is found due to being in NIB, then COMMON_USER
        when(mockAccountService.getAssociatedAccount(same(TEST_AUTH_DATA))).thenReturn(
            Optional.empty(),
            accountWithPrivilege(Privilege.COMMON_USER)
        );

        String impactResult;

        // ---- tick 1: NIB->FOO
        impactResult = stateMachine.handleImpact(TestImpact.of("1-"));
        verifyTickResults(impactResult, "1-NIB-FOO", FooState.class);
        clearInvocations(mockAccountService, mockStateDao);

        // ---- tick 2: FOO->BAR
        impactResult = stateMachine.handleImpact(TestImpact.of("2-"));
        verifyTickResults(impactResult, "2-FOO-BAR", BarState.class);
        clearInvocations(mockAccountService, mockStateDao);

        // ---- tick 3: BAR->FOO
        impactResult = stateMachine.handleImpact(TestImpact.of("3-"));
        verifyTickResults(impactResult, "3-BAR-FOO", FooState.class);
    }

    @Test
    void transitionHasSuitabilityCondition_impactsOntoElection() throws TransitionElectionException, TransitionExecutionException {
        // two FOO->BAR transitions with different suitability conditions
        List<TransitionDescriptor<?, ?, TestImpact, String>> descriptors = List.of(
            baseBuilder(FooState.class, BarState.class).build(),
            baseBuilder(FooState.class, BarState.class)
                .suitabilityCondition((a, impact) -> impact.data == null)
                .build()
        );
        StateMachine<TestImpact, String> stateMachine = constructStateMachine(descriptors);

        // system will be in FOO state, have COMMON_USER account
        when(mockStateDao.getState(same(TEST_AUTH_DATA))).thenReturn(Optional.of(new FooState()));
        when(mockAccountService.getAssociatedAccount(same(TEST_AUTH_DATA))).thenReturn(accountWithPrivilege(Privilege.COMMON_USER));

        // ---- null data - second transition also is suitable
        assertThatExceptionOfType(TransitionElectionException.class).isThrownBy(() -> stateMachine.handleImpact(TestImpact.of(null)));
        clearInvocations(mockAccountService, mockStateDao);

        // ---- notNull data - only first transition is suitable
        stateMachine.handleImpact(TestImpact.of("notNull"));
    }

    @Test
    void manyPossibleTransitions_throwsTransitionElectionException() {
        // two FOO->... transitions
        List<TransitionDescriptor<?, ?, TestImpact, String>> descriptors = List.of(
            baseBuilder(FooState.class, FooState.class).build(),
            baseBuilder(FooState.class, BarState.class).build()
        );
        StateMachine<TestImpact, String> stateMachine = constructStateMachine(descriptors);

        // system will be in FOO state, have COMMON_USER account
        when(mockStateDao.getState(same(TEST_AUTH_DATA))).thenReturn(Optional.of(new FooState()));
        when(mockAccountService.getAssociatedAccount(same(TEST_AUTH_DATA))).thenReturn(accountWithPrivilege(Privilege.COMMON_USER));

        TransitionElectionException exception = catchThrowableOfType(() -> stateMachine.handleImpact(TestImpact.IGNORE),
                                                                     TransitionElectionException.class);

        assertThat(exception).isNotNull();
        assertThat(exception.isZeroElected()).isFalse();
    }

    @Test
    void zeroPossibleTransitions_throwsTransitionElectionException() {
        // FOO->BAR transition that will never be satisfied
        List<TransitionDescriptor<?, ?, TestImpact, String>> descriptors = List.of(
            baseBuilder(FooState.class, BarState.class)
                .suitabilityCondition((a, b) -> false)
                .build()
        );
        StateMachine<TestImpact, String> stateMachine = constructStateMachine(descriptors);

        // system will be in FOO state, have COMMON_USER account
        when(mockStateDao.getState(same(TEST_AUTH_DATA))).thenReturn(Optional.of(new FooState()));
        when(mockAccountService.getAssociatedAccount(same(TEST_AUTH_DATA))).thenReturn(accountWithPrivilege(Privilege.COMMON_USER));

        TransitionElectionException exception = catchThrowableOfType(() -> stateMachine.handleImpact(TestImpact.IGNORE),
                                                                     TransitionElectionException.class);

        assertThat(exception).isNotNull();
        assertThat(exception.isZeroElected()).isTrue();
    }

    @Test
    void transitionWithHigherPriority_superiors() throws TransitionElectionException, TransitionExecutionException {
        // two FOO->... transitions with different priorities
        List<TransitionDescriptor<?, ?, TestImpact, String>> descriptors = List.of(
            baseBuilder(FooState.class, FooState.class).build(),
            baseBuilder(FooState.class, BarState.class)
                .priority(TransitionDescriptor.NORMAL_PRIORITY + 1)
                .build()
        );
        StateMachine<TestImpact, String> stateMachine = constructStateMachine(descriptors);

        // system will be in FOO state, have COMMON_USER account
        when(mockStateDao.getState(same(TEST_AUTH_DATA))).thenReturn(Optional.of(new FooState()));
        when(mockAccountService.getAssociatedAccount(same(TEST_AUTH_DATA))).thenReturn(accountWithPrivilege(Privilege.COMMON_USER));

        // FOO->BAR has higher priority
        stateMachine.handleImpact(TestImpact.IGNORE);

        verify(mockStateDao).setState(same(TEST_AUTH_DATA), isA(BarState.class));
    }

    @Test
    void hasHigherPrivilege_allowsMoreTransitions() throws TransitionElectionException, TransitionExecutionException {
        // two FOO->... transitions with different required privileges
        List<TransitionDescriptor<?, ?, TestImpact, String>> descriptors = List.of(
            baseBuilder(FooState.class, FooState.class).build(),
            baseBuilder(FooState.class, BarState.class)
                .requiredPrivilege(Privilege.PRIVILEGED_USER)
                // giving higher priority to be automatically preferred if allowed
                .priority(TransitionDescriptor.NORMAL_PRIORITY + 1)
                .build()
        );
        StateMachine<TestImpact, String> stateMachine = constructStateMachine(descriptors);

        // system always will be in FOO state
        when(mockStateDao.getState(same(TEST_AUTH_DATA))).thenReturn(Optional.of(new FooState()));

        // ---- with COMMON_USER privilege
        when(mockAccountService.getAssociatedAccount(same(TEST_AUTH_DATA))).thenReturn(accountWithPrivilege(Privilege.COMMON_USER));

        // only FOO->FOO allowed
        stateMachine.handleImpact(TestImpact.IGNORE);

        verify(mockStateDao).setState(same(TEST_AUTH_DATA), isA(FooState.class));
        clearInvocations(mockAccountService, mockStateDao);

        // ---- with PRIVILEGED_USER privilege
        when(mockAccountService.getAssociatedAccount(same(TEST_AUTH_DATA))).thenReturn(accountWithPrivilege(Privilege.PRIVILEGED_USER));

        // both FOO->FOO and FOO->BAR are allowed
        // but elected is FOO->BAR one due to higher priority
        stateMachine.handleImpact(TestImpact.IGNORE);

        verify(mockStateDao).setState(same(TEST_AUTH_DATA), isA(BarState.class));
    }

    // ======== helper methods

    private StateMachine<TestImpact, String> constructStateMachine(List<TransitionDescriptor<?, ?, TestImpact, String>> descriptors) {
        List<TransitionFilter<TestImpact, String>> filters = List.of(new OnlyPermittedTransitionFilter<>(mockAccountService),
                                                                     new OnlyTopByPriorityTransitionFilter<>());
        StateGraph<TestImpact, String> stateGraph = new ClassMappingStateGraph<>(descriptors);
        TransitionElector<TestImpact, String> transitionElector = new SimpleTransitionElector<>(stateGraph, filters);
        return new SimpleStateMachine<>(mockStateDao, transitionElector);
    }

    /**
     * Returns extended {@link DescriptorUtils#descriptorBuilder(Class, Class)} which returns {@code non-null} arrival object.
     * <p/>
     * Because of the way it's implemented (reflection), class of {@code arrival} must have default constructor.
     */
    private static <D extends State, A extends State> Builder<D, A, TestImpact, String> baseBuilder(Class<D> departure, Class<A> arrival) {
        Builder<D, A, TestImpact, String> builder = descriptorBuilder(departure, arrival);
        return builder.executionFunction((a, b) -> {
            try {
                return arrival.getDeclaredConstructor().newInstance();
            } catch (Throwable e) {
                throw Lombok.sneakyThrow(e);
            }
        });
    }

    private static Optional<Account> accountWithPrivilege(Privilege privilege) {
        Account account = new Account();
        account.setPrivilege(privilege);
        return Optional.of(account);
    }

    private void verifyTickResults(String actualResult, String expectedResult, Class<? extends State> newState) {
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(mockStateDao).setState(same(TEST_AUTH_DATA), isA(newState));
        verifyNoMoreInteractions(mockAccountService, mockStateDao);
    }

    @AllArgsConstructor(staticName = "of")
    private final static class TestImpact implements Impact {

        public static final TestImpact IGNORE = new TestImpact(null);
        private final String data;

        @Override
        public AuthenticationData getInitiatorAuthData() {
            return TEST_AUTH_DATA;
        }
    }
}
