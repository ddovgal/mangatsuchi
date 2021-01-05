package ua.ddovgal.mangamonitoring.state.transition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import ua.ddovgal.mangamonitoring.domain.Privilege;
import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.exception.TransitionExecutionException;

/**
 * Describes transition descriptor with predefined departure state object. The only goal for this class is to retain type safety of the
 * departure state type.
 * <p/>
 * In the runtime it won't be known actual classes of the state objects, they all will be hidden behind the parent {@link State} type
 * reference. However, particular {@link TransitionDescriptor} implementations require departure state objects of particular classes. This
 * wrapper class solves that issue in a way that it holds {@link #departure} object of particular class, that is required by also held
 * {@link #descriptor}. That's guarantee that they will be compatible. Then, departure-dependent methods just delegates call to held {@link
 * #descriptor} while using held {@link #departure} as a method parameter. As a result, those methods will not require departure object and
 * will never cause class cast exception.
 *
 * @param <D> type of the departure state.
 * @param <A> type of the arrival state.
 * @param <I> type of the impact.
 * @param <R> type of the impact result.
 */
@RequiredArgsConstructor
public final class DepartureDefinedTransition<D extends State, A extends State, I extends Impact, R> {

    @Getter
    private final TransitionDescriptor<D, A, I, R> descriptor;
    private final D departure;

    /**
     * Returns priority of the held transition {@link #descriptor}.
     *
     * @return priority of the held transition {@link #descriptor}.
     */
    public int getPriority() {
        return descriptor.getPriority();
    }

    /**
     * Returns minimal required privilege the impact initiator required to have to make the held transition {@link #descriptor}.
     *
     * @return minimal minimal required privilege the impact initiator required to have to make the held transition {@link #descriptor}.
     */
    public Privilege getRequiredPrivilege() {
        return descriptor.getRequiredPrivilege();
    }

    /**
     * Checks if the held transition {@link #descriptor} can be made with the held {@link #departure} and provided {@code impact}.
     *
     * @param impact impact to check held transition {@link #descriptor} suitability.
     *
     * @return {@code true} if the held transition {@link #descriptor} can be made with the held {@link #departure} and provided {@code
     * impact}, or else {@code false}.
     */
    public boolean isSuitable(I impact) {
        return descriptor.isSuitable(departure, impact);
    }

    /**
     * Performs transition and returns object with the transition results. Actually, all it does is a {@link
     * TransitionDescriptor#execute(State, Impact)} call on a held {@link #descriptor} and putting obtained result into the {@link
     * TransitionResult} object.
     *
     * @param impact impact to execute held transition.
     *
     * @return object with the transition results.
     *
     * @throws TransitionExecutionException if transition failed for any possible case.
     * @see TransitionDescriptor#execute(State, Impact) for actual method details.
     * @see TransitionResult for the transition results detailed information.
     */
    public TransitionResult<D, A, I, R> execute(I impact) throws TransitionExecutionException {
        A arrival = descriptor.execute(departure, impact);
        return new TransitionResult<>(descriptor, impact, departure, arrival);
    }
}
