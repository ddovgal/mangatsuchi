package ua.ddovgal.mangamonitoring.state.election;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.exception.TransitionElectionException;
import ua.ddovgal.mangamonitoring.state.graph.StateGraph;
import ua.ddovgal.mangamonitoring.state.transition.DepartureDefinedTransition;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;
import ua.ddovgal.mangamonitoring.state.utils.States.BarState;
import ua.ddovgal.mangamonitoring.state.utils.States.FooState;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static ua.ddovgal.mangamonitoring.state.utils.DescriptorUtils.descriptorBuilder;
import static ua.ddovgal.mangamonitoring.state.utils.States.BazState;

@ExtendWith(MockitoExtension.class)
class SimpleTransitionElectorTest {

    @Mock
    private StateGraph<Impact, Object> stateGraph;

    @Mock
    private List<TransitionFilter<Impact, Object>> filters;

    @InjectMocks
    private SimpleTransitionElector<Impact, Object> elector;

    @Test
    void electTransition_happyPath(@Mock State departure, @Mock Impact impact) throws TransitionElectionException {
        DepartureDefinedTransition<State, BazState, Impact, Object> transition = transition(departure, BazState.class, true);
        defineGraphWillReturnTransitionsForState(departure,
                                                 transition(departure, FooState.class, true),
                                                 transition(departure, BarState.class, false),
                                                 transition);
        defineFilters((a, b, list) -> list,
                      (a, b, transitions) -> transitions.subList(1, 2) /*only last element list*/);

        DepartureDefinedTransition<?, ?, Impact, Object> electedTransition = elector.electTransition(departure, impact);

        assertThat(electedTransition).isSameAs(transition);
        verifyNoMoreInteractions(stateGraph, filters);
    }

    @Test
    void electTransition_graphReturnedEmptyList_throwsTransitionElectionException(@Mock State departure, @Mock Impact impact) {
        defineGraphWillReturnTransitionsForState(departure /*empty list*/);
        defineFilters((a, b, list) -> list,
                      (a, b, transitions) -> new ArrayList<>(transitions) /*actually it doesn't matter*/);

        assertThatExceptionOfType(TransitionElectionException.class).isThrownBy(() -> elector.electTransition(departure, impact));
        verifyNoMoreInteractions(stateGraph, filters);
    }

    @Test
    void electTransition_nooneWasSuitable_throwsTransitionElectionException(@Mock State departure, @Mock Impact impact) {
        defineGraphWillReturnTransitionsForState(departure,
                                                 transition(departure, FooState.class, false),
                                                 transition(departure, BarState.class, false));
        defineFilters((a, b, list) -> list,
                      (a, b, transitions) -> new ArrayList<>(transitions) /*actually it doesn't matter*/);

        assertThatExceptionOfType(TransitionElectionException.class).isThrownBy(() -> elector.electTransition(departure, impact));
        verifyNoMoreInteractions(stateGraph, filters);
    }

    @Test
    void electTransition_emptyFiltersList_causesNoExceptions(@Mock State departure, @Mock Impact impact) throws TransitionElectionException {
        DepartureDefinedTransition<State, BarState, Impact, Object> transition = transition(departure, BarState.class, true);
        defineGraphWillReturnTransitionsForState(departure,
                                                 transition(departure, FooState.class, false),
                                                 transition);
        defineFilters(/*no filters*/);

        DepartureDefinedTransition<?, ?, Impact, Object> electedTransition = elector.electTransition(departure, impact);

        assertThat(electedTransition).isSameAs(transition);
        verifyNoMoreInteractions(stateGraph, filters);
    }

    @Test
    void electTransition_firstFilterRejectedAllTransitions_throwsTransitionElectionException(@Mock State departure, @Mock Impact impact) {
        defineGraphWillReturnTransitionsForState(departure,
                                                 transition(departure, FooState.class, true),
                                                 transition(departure, BarState.class, false),
                                                 transition(departure, BazState.class, true));
        defineFilters((a, b, list) -> List.of(),
                      (a, b, list) -> list);

        assertThatExceptionOfType(TransitionElectionException.class).isThrownBy(() -> elector.electTransition(departure, impact));
        verifyNoMoreInteractions(stateGraph, filters);
    }

    @Test
    void electTransition_moreThanOneTransitionRemained_throwsTransitionElectionException(@Mock State departure, @Mock Impact impact) {
        defineGraphWillReturnTransitionsForState(departure,
                                                 transition(departure, FooState.class, true),
                                                 transition(departure, BarState.class, true));
        defineFilters();

        TransitionElectionException exception = catchThrowableOfType(() -> elector.electTransition(departure, impact),
                                                                     TransitionElectionException.class);

        assertThat(exception).isNotNull();
        assertThat(exception.getImpact()).isSameAs(impact);
        assertThat(exception.getDeparture()).isSameAs(departure);
        assertThat(exception.getElectedDescriptors()).hasSize(2);
        verifyNoMoreInteractions(stateGraph, filters);
    }

    @Test
    void electTransition_zeroTransitionsRemained_throwsTransitionElectionException(@Mock State departure, @Mock Impact impact) {
        defineGraphWillReturnTransitionsForState(departure,
                                                 transition(departure, FooState.class, true),
                                                 transition(departure, BarState.class, true));
        defineFilters((a, b, list) -> List.of() /*leave nothing*/);

        TransitionElectionException exception = catchThrowableOfType(() -> elector.electTransition(departure, impact),
                                                                     TransitionElectionException.class);

        assertThat(exception).isNotNull();
        assertThat(exception.getImpact()).isSameAs(impact);
        assertThat(exception.getDeparture()).isSameAs(departure);
        assertThat(exception.getElectedDescriptors()).isEmpty();
        verifyNoMoreInteractions(stateGraph, filters);
    }

    @SafeVarargs
    private void defineGraphWillReturnTransitionsForState(State departure,
                                                          DepartureDefinedTransition<State, ?, Impact, Object>... transitions) {
        when(stateGraph.getDepartingTransitions(same(departure))).thenReturn(Arrays.asList(transitions));
    }

    @SafeVarargs
    private void defineFilters(TransitionFilter<Impact, Object>... defiedFilters) {
        when(filters.stream()).thenReturn(Arrays.stream(defiedFilters));
    }

    @SuppressWarnings("unchecked")
    private static <D extends State, A extends State> DepartureDefinedTransition<D, A, Impact, Object> transition(D departure,
                                                                                                                  Class<A> arrival,
                                                                                                                  boolean isSuitable) {
        TransitionDescriptor<D, A, Impact, Object> descriptor = descriptorBuilder((Class<D>) departure.getClass(), arrival)
            .suitabilityCondition((a, b) -> isSuitable)
            .build();
        return new DepartureDefinedTransition<>(descriptor, departure);
    }
}