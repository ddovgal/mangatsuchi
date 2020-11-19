package ua.ddovgal.mangamonitoring.state.exception;

/**
 * Throws to indicate that for any reason graph has an unacceptable structure and could cause incorrect behavior.
 */
public class UnacceptableGraphStructureException extends Exception {

    private static final String MESSAGE = "Can't create the graph because of unacceptable structure";

    /**
     * Constructs default exception with predefined message.
     */
    public UnacceptableGraphStructureException() {
        super(MESSAGE);
    }
}
