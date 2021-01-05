package ua.ddovgal.mangamonitoring.core;

/**
 * Represents a very abstract and generic application. It's quite simple and has only three methods, so to be better understandable and for
 * more information just check each one description.
 *
 * @param <E> type of environment of current application. For environment details see {@link #getEnvironment()} description.
 */
public interface Application<E> {

    /**
     * Provides prepared and ready to be used environment. It could build environment during method call or just return already built one.
     * <p>
     * Environment is something like a wrapper for all important for work components, plus it serves like the configuration during startup.
     *
     * @return built and ready to be used application environment.
     */
    E getEnvironment();

    /**
     * Launches application with provided environment. Here, {@code environment} acts like startup configuration. Application itself doesn't
     * have states, but here to be able to explain simpler, lets say that before calling this method application or {@code environment} is
     * in "not yet started" state, while method is running it's in "starting" state and after method completion state becomes "running" or
     * "failed to start" in case of exception occurs.
     * <pre>
     * Method implementation MUST comply with the following rules:
     *  - be non-blocking. It only makes application run, don't wait till it's complete;
     *  - do the startup in current thread only, synchronously.
     *    Spawn a new thread only for long-live operations (like requests' listener, background updated job, etc.);
     *  - do not throw any runtime exceptions while the startup, even if some component failed its startup.
     *    In case of the startup still fails at some phase (if something couldn't be started, or got an exception), then returnable {@code
     *    applicationExecution} must:
     *     - be in {@link ApplicationExecution.State#FAILED} state;
     *     - return guilty exception as a result of {@link ApplicationExecution#getFiascoCause()} call;
     *     - immediately release the calling thread on {@link ApplicationExecution#waitTillCompletion()} call;
     *    You could use already implemented {@link FailedApplicationExecution} for such a case. Also, in such case you own are responsible
     *    for some kind of rollback, stopping already started things, etc. Application should not be running, and at the same time be
     *    incorrectly working. Either a successful start and a running application or non-running failed application.
     * </pre>
     * After successful start method returns {@code applicationExecution} object which represents started application "session" ({@link
     * SimpleApplicationExecution} implementation will be suitable for most cases). This object also could be used by threads spawned during
     * startup to shut down whole application (using {@link ApplicationExecution#shutdownCrashed(Exception)}) in case of unrecoverable crash
     * inside the thread which will affect the whole application and could raise errors in other threads or make application incorrectly
     * working.
     *
     * @param environment environment of current application, in this scope it's also like some kind of config.
     *
     * @return started application execution object.
     */
    ApplicationExecution start(E environment);

    /**
     * Stops the application by provided environment. It is expected to close connections, descriptors, make cleanup inside this method.
     * <pre>
     * Method implementation must comply with the following rules:
     *  - do stop in current thread only, synchronously;
     *  - do not throw any runtime exceptions while stopping, even if some component failed its stopping.
     *    In case of the stop still fails at some phase (if something couldn't be stopped, or got an exception), then it should be treated
     *    like it can't be stopped anyhow, banally nothing can be done. Hence, just ignore it, let it be as it is and continue to stop other
     *    components.
     * </pre>
     *
     * @param runningEnvironment environment with which application successfully started.
     *
     * @return false in case of the stop failed at some phase (something couldn't be stopped, or it threw an exception), otherwise true as a
     * successful result.
     */
    boolean stop(E runningEnvironment);
}
