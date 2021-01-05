package ua.ddovgal.mangamonitoring.state;

/**
 * Describes state of the {@link StateMachine}. This is an empty interface used only like a useful marker. Each implementation class,
 * besides the fact that simply by its creation it defines a new state, due to the absence of requirements from the interface also able to
 * has its own internal data, which allows describing machine state not only as a particular type from the defined set, but also as a state
 * of some set of data.
 *
 * @see StateMachine for state machine a general concept.
 */
public interface State {}
