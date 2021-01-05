package ua.ddovgal.mangamonitoring.state.transition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;

/**
 * Data holder class that describes successfully completed transition with its result. The only goal for this class is to retain type safety
 * of the {@link #departure} and {@link #arrival} state types.
 * <p/>
 * In the runtime it won't be known actual classes of the state objects, they all will be hidden behind the parent {@link State} type
 * reference. However, particular {@link TransitionDescriptor} implementations require departure and arrival state objects of particular
 * classes. This wrapper class solves that issue in a way that it holds {@link #departure} and {@link #arrival} objects, each of particular
 * class, that is required by also held {@link #descriptor}. That's guarantee that they will be compatible. Then, the only one provided
 * method just calls {@link TransitionDescriptor#buildImpactResult(State, State, Impact)} on held {@link #descriptor} while using held
 * {@link #departure}, {@link #arrival} and {@link #impact} as method parameters. As a result, this method will never cause class cast
 * exception.
 *
 * @param <D> type of the held departure state.
 * @param <A> type of the held arrival state.
 * @param <I> type of the held impact.
 * @param <R> type of the impact result it can build.
 */
@RequiredArgsConstructor
public class TransitionResult<D extends State, A extends State, I extends Impact, R> {

    private final TransitionDescriptor<D, A, I, R> descriptor;
    private final I impact;
    private final D departure;
    @Getter
    private final A arrival;

    /**
     * Builds impact result from the held {@link #departure}, {@link #arrival} and {@link #impact} using held {@link #descriptor}.
     *
     * @return new impact result, built from the held {@link #departure}, {@link #arrival} and {@link #impact} using held {@link
     * #descriptor}.
     */
    public R buildImpactResult() {
        return descriptor.buildImpactResult(departure, arrival, impact);
    }
}
