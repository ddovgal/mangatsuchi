package ua.ddovgal.mangamonitoring.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represent implementation of the single case — application failed during startup.
 * <p/>
 * Fulfills listed in {@link Application#start(Object)} fail scenario requirements.
 *
 * @see ua.ddovgal.mangamonitoring.core.ApplicationExecution
 */
@RequiredArgsConstructor(staticName = "dueTo")
public class FailedApplicationExecution implements ApplicationExecution {

    private static final String EXCEPTION_MESSAGE = "Application failed already at the startup, hence never started";

    @Getter
    private final Exception fiascoCause;

    @Override
    public void waitTillCompletion() {
        // No need to wait for something because application didn't even start
    }

    @Override
    public void shutdown() {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public void shutdownCrashed(Exception crashCause) {
        throw new IllegalStateException(EXCEPTION_MESSAGE);
    }

    @Override
    public State getState() {
        return State.FAILED;
    }
}
