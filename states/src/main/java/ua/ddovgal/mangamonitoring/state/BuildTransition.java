package ua.ddovgal.mangamonitoring.state;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class BuildTransition<S1 extends State, S2 extends State, U> implements Transition<S1, S2, U> {

    @Getter
    @NonNull
    private Class<S1> departureState;

    @Getter
    @NonNull
    private Class<S2> arrivalState;

    @NonNull
    private BiPredicate<S1, U> suitabilityCondition;

    @NonNull
    private Function<U, S2> arrivalStateProvider;

    @NonNull
    private BiConsumer<S1, U> departingAction;

    @NonNull
    private BiConsumer<S2, U> newStateConsumer;

    public static <S1 extends State, S2 extends State, U> TransitionBuilder<S1, S2, U> between(Class<S1> from, Class<S2> to) {
        return new TransitionBuilder<>(from, to);
    }

    @Override
    public boolean isSuitable(S1 departingState, U update) {
        return suitabilityCondition.test(departingState, update);
    }

    @Override
    public S2 buildArrivalState(U update) {
        return arrivalStateProvider.apply(update);
    }

    @Override
    public void departFrom(S1 departingState, U update) {
        departingAction.accept(departingState, update);
    }

    @Override
    public void consumeNewState(S2 newState, U update) {
        newStateConsumer.accept(newState, update);
    }

    @Accessors(chain = true, fluent = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class TransitionBuilder<S1 extends State, S2 extends State, U> {

        @NonNull
        private final Class<S1> departureStateClass;

        @NonNull
        private final Class<S2> arrivalStateClass;

        @Setter
        private BiPredicate<S1, U> suitabilityCondition;

        @Setter
        private Function<U, S2> arrivalStateProvider;

        @Setter
        private BiConsumer<S1, U> departingAction = (currentState, update) -> {};

        @Setter
        private BiConsumer<S2, U> newStateConsumer = (newState, update) -> {};

        public Transition<S1, S2, U> complete() {
            return new BuildTransition<>(departureStateClass, arrivalStateClass, suitabilityCondition, arrivalStateProvider,
                                         departingAction, newStateConsumer);
        }
    }
}
