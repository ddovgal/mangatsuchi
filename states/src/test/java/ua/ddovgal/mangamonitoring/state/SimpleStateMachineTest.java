package ua.ddovgal.mangamonitoring.state;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.ddovgal.mangamonitoring.api.AuthenticationData;
import ua.ddovgal.mangamonitoring.state.election.TransitionElector;
import ua.ddovgal.mangamonitoring.state.exception.TransitionElectionException;
import ua.ddovgal.mangamonitoring.state.exception.TransitionExecutionException;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleStateMachineTest {

    @Mock
    private StateDao stateDao;

    @Mock
    private TransitionElector<Impact, Object> transitionElector;

    @InjectMocks
    private SimpleStateMachine<Impact, Object> stateMachine;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    void handleImpact_happyPath(@Mock Impact impact) throws TransitionElectionException, TransitionExecutionException {
        AuthenticationData auth = mock(AuthenticationData.class);
        when(impact.getInitiatorAuthData()).thenReturn(auth);
        State current = mock(State.class);
        when(stateDao.getState(same(auth))).thenReturn(Optional.of(current));
        State arrived = mock(State.class);
        Object result = new Object();
        TransitionDescriptor<State, State, Impact, Object> descriptor = mockDescriptor();
        when(descriptor.execute(same(current), same(impact))).thenReturn(arrived);
        when(descriptor.buildImpactResult(same(current), any(), same(impact))).thenReturn(result);
        when(transitionElector.electTransition(same(current), same(impact)))
            .thenReturn(new DepartureDefinedTransition(descriptor, current));

        Object impactResult = stateMachine.handleImpact(impact);

        assertThat(impactResult).isSameAs(result);
        verify(stateDao).setState(same(auth), same(arrived));
        verifyNoMoreInteractions(stateDao, transitionElector);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    void handleImpact_noStateReturned_NeverActedBeforeStateUsed(@Mock Impact impact) throws TransitionElectionException {
        AuthenticationData auth = mock(AuthenticationData.class);
        when(impact.getInitiatorAuthData()).thenReturn(auth);
        when(stateDao.getState(same(auth))).thenReturn(Optional.empty());
        TransitionDescriptor<State, State, Impact, Object> descriptor = mockDescriptor();
        when(transitionElector.electTransition(isA(NoInteractionsBeforeState.class), same(impact)))
            .thenReturn(new DepartureDefinedTransition(descriptor, new NoInteractionsBeforeState()));

        assertThatNoException().isThrownBy(() -> stateMachine.handleImpact(impact));
        verify(stateDao).setState(same(auth), any());
        verifyNoMoreInteractions(stateDao, transitionElector);
    }

    @Test
    void handleImpact_cantElect_exceptionNotCaught(@Mock Impact impact) throws TransitionElectionException {
        AuthenticationData auth = mock(AuthenticationData.class);
        when(impact.getInitiatorAuthData()).thenReturn(auth);
        State current = mock(State.class);
        when(stateDao.getState(same(auth))).thenReturn(Optional.of(current));
        when(transitionElector.electTransition(same(current), same(impact)))
            .thenThrow(new TransitionElectionException(impact, current, List.of()));

        TransitionElectionException exception = catchThrowableOfType(() -> stateMachine.handleImpact(impact),
                                                                     TransitionElectionException.class);

        assertThat(exception).isNotNull();
        assertThat(exception.isZeroElected()).isTrue();
        assertThat(exception.getImpact()).isSameAs(impact);
        assertThat(exception.getDeparture()).isSameAs(current);
        assertThat(exception.getElectedDescriptors()).isEmpty();
        verifyNoMoreInteractions(stateDao, transitionElector);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    void handleImpact_exceptionWhileExecuting_exceptionNotCaught(@Mock Impact impact) throws TransitionElectionException, TransitionExecutionException {
        AuthenticationData auth = mock(AuthenticationData.class);
        when(impact.getInitiatorAuthData()).thenReturn(auth);
        State current = mock(State.class);
        when(stateDao.getState(same(auth))).thenReturn(Optional.of(current));
        TransitionDescriptor<State, State, Impact, Object> descriptor = mockDescriptor();
        when(descriptor.execute(same(current), same(impact))).thenThrow(new TransitionExecutionException("Oops"));
        when(transitionElector.electTransition(same(current), same(impact)))
            .thenReturn(new DepartureDefinedTransition(descriptor, current));

        assertThatExceptionOfType(TransitionExecutionException.class).isThrownBy(() -> stateMachine.handleImpact(impact));
        verifyNoMoreInteractions(stateDao, transitionElector);
    }

    @SuppressWarnings("unchecked")
    private static TransitionDescriptor<State, State, Impact, Object> mockDescriptor() {
        TransitionDescriptor<State, State, Impact, Object> descriptor = mock(TransitionDescriptor.class);
        // need to prevent NPE due to logging
        when(descriptor.getDepartureState()).thenReturn(State.class);
        when(descriptor.getArrivalState()).thenReturn(State.class);
        return descriptor;
    }
}