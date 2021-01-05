package ua.ddovgal.mangamonitoring.state.transition;

import java.util.function.BiPredicate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import ua.ddovgal.mangamonitoring.domain.Privilege;
import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.exception.TransitionExecutionException;
import ua.ddovgal.mangamonitoring.state.util.ExecutionFunction;
import ua.ddovgal.mangamonitoring.state.util.TriFunction;

/**
 * Implementation that uses lambda function interface objects to provide implementations of the methods required by the interface. This
 * implementation is useful when need to create a descriptor "in place", specifying only 1-2 expressions for each method. Provides fluent
 * way to build transition descriptor with {@link Builder} class.
 *
 * @param <D> type of the departure state.
 * @param <A> type of the arrival state.
 * @param <I> type of the impact.
 * @param <R> type of the impact result.
 *
 * @see Builder for the building steps details.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class LambdaDescribedTransitionDescriptor<D extends State, A extends State, I extends Impact, R>
    implements TransitionDescriptor<D, A, I, R> {

    @Getter
    @NonNull
    private final Class<D> departureState;

    @Getter
    @NonNull
    private final Class<A> arrivalState;

    @Getter
    private final int priority;

    @Getter
    private final Privilege requiredPrivilege;

    @NonNull
    private final BiPredicate<D, I> suitabilityCondition;

    @NonNull
    private final ExecutionFunction<D, I, A> executionFunction;

    @NonNull
    private final TriFunction<D, A, I, R> impactResultBuilder;

    /**
     * Transition descriptor building starter method. Returns builder object to complete build process.
     *
     * @param from state class object that will be used as a {@code departureState}.
     * @param to   state class object that will be used as a {@code arrivalState}.
     * @param <D>  type of the result departure state.
     * @param <A>  type of the result arrival state.
     * @param <I>  type of the result impact.
     * @param <R>  type of the result impact result.
     *
     * @return transition descriptor builder object.
     */
    public static <D extends State, A extends State, I extends Impact, R> Builder<D, A, I, R> between(Class<D> from, Class<A> to) {
        return new Builder<>(from, to);
    }

    @Override
    public boolean isSuitable(D departure, I impact) {
        return suitabilityCondition.test(departure, impact);
    }

    @Override
    public A execute(D departure, I impact) throws TransitionExecutionException {
        return executionFunction.apply(departure, impact);
    }

    @Override
    public R buildImpactResult(D departure, A arrival, I impact) {
        return impactResultBuilder.apply(departure, arrival, impact);
    }

    /**
     * Builder-like class for {@link LambdaDescribedTransitionDescriptor}. Provides fluent way to build transition descriptor.
     *
     * @param <D> type of the built transition descriptor departure state.
     * @param <A> type of the built transition descriptor arrival state.
     * @param <I> type of the built transition descriptor impact.
     * @param <R> type of the built transition descriptor impact result.
     */
    @Accessors(chain = true, fluent = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Builder<D extends State, A extends State, I extends Impact, R> {

        @NonNull
        private final Class<D> departureStateClass;

        @NonNull
        private final Class<A> arrivalStateClass;

        @Setter
        private int priority = TransitionDescriptor.NORMAL_PRIORITY;

        @Setter
        private Privilege requiredPrivilege = Privilege.COMMON_USER;

        @Setter
        private BiPredicate<D, I> suitabilityCondition;

        @Setter
        private ExecutionFunction<D, I, A> executionFunction;

        @Setter
        private TriFunction<D, A, I, R> impactResultBuilder;

        /**
         * Completes build process and returns packed transition descriptor result.
         *
         * @return packed transition descriptor result.
         */
        public TransitionDescriptor<D, A, I, R> build() {
            return new LambdaDescribedTransitionDescriptor<>(departureStateClass, arrivalStateClass, priority, requiredPrivilege,
                                                             suitabilityCondition, executionFunction, impactResultBuilder);
        }
    }
}
