package ua.ddovgal.mangamonitoring.state.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

/**
 * Utility class that provides useful methods to work with {@link TransitionDescriptor}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransitionDescriptorUtils {

    /**
     * Returns simple string representation of the provided {@code transitions} list.
     *
     * @param transitions transitions list to get simple form.
     *
     * @return simple string representation of the provided {@code transitions} list.
     */
    public static String getDescriptorsSimpleForm(List<? extends DepartureDefinedTransition<?, ?, ?, ?>> transitions) {
        return getSimpleForm(transitions.stream().map(DepartureDefinedTransition::getDescriptor));
    }

    /**
     * Returns simple string representation of the provided {@code descriptors} list.
     *
     * @param descriptors transition descriptors list to get simple form.
     *
     * @return simple string representation of the provided {@code descriptors} list.
     */
    public static String getSimpleForm(List<? extends TransitionDescriptor<?, ?, ?, ?>> descriptors) {
        return getSimpleForm(descriptors.stream());
    }

    /**
     * Returns simple string representation of the provided transition {@code descriptor}.
     *
     * @param descriptor transition descriptor to get simple form.
     *
     * @return simple string representation of the provided transition {@code descriptor}.
     */
    public static String getSimpleForm(TransitionDescriptor<?, ?, ?, ?> descriptor) {
        return String.format("{direction=%s; privilege=%s; priority=%d}",
                             getDirection(descriptor),
                             descriptor.getRequiredPrivilege(),
                             descriptor.getPriority());
    }

    /**
     * Returns direction representation of the provided transition {@code descriptor}.
     *
     * @param descriptor transition descriptor to get direction.
     *
     * @return direction representation of the provided transition {@code descriptor}.
     */
    public static String getDirection(TransitionDescriptor<?, ?, ?, ?> descriptor) {
        return descriptor.getDepartureState().getSimpleName() + "-" + descriptor.getArrivalState().getSimpleName();
    }

    private static String getSimpleForm(Stream<? extends TransitionDescriptor<?, ?, ?, ?>> descriptorsStream) {
        return descriptorsStream
            .map(TransitionDescriptorUtils::getSimpleForm)
            .collect(Collectors.joining(", ", "[", "]"));
    }
}
