package ua.ddovgal.mangamonitoring.state.election;

import java.util.ArrayList;
import java.util.List;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

/**
 * Filter implementation which leaves only transitions that have {@link TransitionDescriptor#getPriority()} equal to the highest in the
 * provided {@code transitions} list.
 * <p/>
 * For example, for transitions list with priorities {@code [2, 4, 0, 1, 0, 4, 1, 1]} the highest priority is {@code 4}, hence only
 * transitions with priority equal to {@code 4} will remain, so filtered list will be {@code [4, 4]}.
 */
public class OnlyTopByPriorityTransitionFilter<I extends Impact, R> implements TransitionFilter<I, R> {

    @Override
    public List<DepartureDefinedTransition<?, ?, I, R>> filter(I impact,
                                                               State state,
                                                               List<DepartureDefinedTransition<?, ?, I, R>> transitions) {
        int maxPriority = -1;
        List<DepartureDefinedTransition<?, ?, I, R>> topPriorityTransitions = new ArrayList<>();

        for (DepartureDefinedTransition<?, ?, I, R> transition : transitions) {
            int priority = transition.getPriority();
            if (priority > maxPriority) {
                maxPriority = priority;
                topPriorityTransitions = new ArrayList<>();
                topPriorityTransitions.add(transition);
            } else if (priority == maxPriority) {
                topPriorityTransitions.add(transition);
            }
            // nothing to do if priority < maxPriority
        }

        return topPriorityTransitions;
    }
}
