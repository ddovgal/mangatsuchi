package ua.ddovgal.trackerkun.core;

import ua.ddovgal.trackerkun.core.exception.ApplicationStartupException;

/**
 * Describes component which can be started separately after it becomes configured and can perform some actions when it needs to be
 * stopped.
 */
public interface ManagedComponent {

    /**
     * Starts component making it actually working.
     * <pre>
     * Method implementation must comply with the following rules:
     *  - do startup, all that is required to finally make component working in current thread only.
     *    Spawn a new threads only in case of successful startup and for long-live operations only;
     *  - be non-blocking. It only makes component working, don't wait till it's complete;
     *  - in case of the component couldn't be started or wouldn't be able to work properly, be sure to throw startup exception.
     * </pre>
     *
     * @throws ApplicationStartupException in case of the component couldn't be started or wouldn't be able to work properly.
     */
    void start() throws ApplicationStartupException;

    /**
     * Stops component making it non-working. Intended for the end of work actions e.g. clean-up, closing connections, descriptors, make
     * cleanup.
     * <p>
     * Any unhandled missed runtime exception from method implementation will be treated as like component failed while was stopping and
     * then it can't be stopped in any possible way.
     */
    void stop();
}
