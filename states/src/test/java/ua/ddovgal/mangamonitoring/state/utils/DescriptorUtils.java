package ua.ddovgal.mangamonitoring.state.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.LambdaDescribedTransitionDescriptor;
import ua.ddovgal.mangamonitoring.state.transition.LambdaDescribedTransitionDescriptor.Builder;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DescriptorUtils {

    public static <D extends State, A extends State, I extends Impact, R> TransitionDescriptor<D, A, I, R> descriptor(Class<D> departure,
                                                                                                                      Class<A> arrival) {
        Builder<D, A, I, R> builder = descriptorBuilder(departure, arrival);
        return builder.build();
    }

    public static <D extends State, A extends State, I extends Impact, R> Builder<D, A, I, R> descriptorBuilder(Class<D> departure,
                                                                                                                Class<A> arrival) {
        return LambdaDescribedTransitionDescriptor.<D, A, I, R>between(departure, arrival)
            .suitabilityCondition((a, b) -> true)
            .executionFunction((a, b) -> null)
            .impactResultBuilder(((a, b, c) -> null));
    }
}
