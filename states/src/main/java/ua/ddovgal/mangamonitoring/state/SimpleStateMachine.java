package ua.ddovgal.mangamonitoring.state;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import ua.ddovgal.mangamonitoring.state.election.TransitionElector;
import ua.ddovgal.mangamonitoring.state.exception.TransitionElectionException;
import ua.ddovgal.mangamonitoring.state.exception.TransitionExecutionException;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionResult;

import static ua.ddovgal.mangamonitoring.state.util.TransitionDescriptorUtils.getSimpleForm;

/**
 * Describes a very simple implementation of the {@link StateMachine} which acts more like orchestrator that delegates certain steps of the
 * impact handling to its dependent components.
 *
 * @param <I> type of the incoming impact that should be handled.
 * @param <R> type of the impact result.
 */
@Slf4j
@AllArgsConstructor
public class SimpleStateMachine<I extends Impact, R> implements StateMachine<I, R> {

    @NonNull
    protected final StateDao stateDao;
    @NonNull
    private final TransitionElector<I, R> transitionElector;

    @Override
    public R handleImpact(I impact) throws TransitionElectionException, TransitionExecutionException {
        log.info("Handling impact={}", impact);
        State currentState = stateDao.getState(impact.getInitiatorAuthData()).orElse(new NoInteractionsBeforeState());
        log.info("currentState={}", currentState);
        DepartureDefinedTransition<?, ?, I, R> electedTransition = transitionElector.electTransition(currentState, impact);
        log.info("electedDescriptor={}", getSimpleForm(electedTransition.getDescriptor()));
        TransitionResult<?, ?, I, R> transitionResult = electedTransition.execute(impact);
        stateDao.setState(impact.getInitiatorAuthData(), transitionResult.getArrival());
        R impactResult = transitionResult.buildImpactResult();
        log.info("Handle complete; arrival={}; impactResult={}", transitionResult.getArrival(), impactResult);
        return impactResult;
    }
}
