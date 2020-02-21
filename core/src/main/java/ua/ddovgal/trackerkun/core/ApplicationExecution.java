package ua.ddovgal.trackerkun.core;

/**
 * Represents application execution after its startup, serves as monitoring and control object.
 */
public interface ApplicationExecution {

    /**
     * Blocks a current thread and waits until a running application completes. Doesn't block in case the application already completed its
     * execution (such case is valid if application is in one of the states: {@link State#FAILED}, {@link State#CRASHED}, {@link
     * State#COMPLETED}).
     *
     * @throws InterruptedException if the current thread was interrupted while waiting.
     * @see #shutdown() to gat info about the way to complete execution.
     */
    void waitTillCompletion() throws InterruptedException;

    /**
     * Stops running application execution. Transfers execution to {@link State#SHUTTING_DOWN} state while shutting down and then finally to
     * {@link State#COMPLETED}. In case of exception while shutting down, catches it but ignores and still transfers execution to {@link
     * State#COMPLETED} state. Furthermore, it essentially unlocks blocked by {@link #waitTillCompletion()} threads.
     * <p>
     * This method is thread-safe, meaning that only one thread at the same time will be able to actually stop execution.
     *
     * @throws IllegalStateException in case the application is not in {@link State#RUNNING} state.
     * @see #waitTillCompletion()
     */
    void shutdown();

    /**
     * Stops running application execution due to unrecoverable crash. Transfers execution to {@link State#SHUTTING_DOWN} state while
     * shutting down and then finally to {@link State#CRASHED}. In addition to normal shutdown specifies {@code crashCause} as further
     * {@link #getFiascoCause()} method call result. In case of exception while shutting down, catches it but ignores and still transfers
     * execution to {@link State#CRASHED} state. Furthermore, it essentially unlocks blocked by {@link #waitTillCompletion()} threads.
     * <p>
     * This method is thread-safe, meaning that only one thread at the same time will be able to actually stop execution.
     * <p>
     * Actual implementation must set fiasco cause before state change as {@code state} serves as "thread-safer" for non thread-safe fiasco
     * cause (see {@link #getFiascoCause()}).
     *
     * @throws IllegalStateException in case the application is not in {@link State#RUNNING} state.
     * @see #waitTillCompletion()
     */
    void shutdownCrashed(Exception crashCause);

    /**
     * Returns current execution state.
     * <p>
     * This method is thread-safe, meaning that you will get actual state.
     *
     * @return current execution state.
     */
    State getState();

    /**
     * Returns exception that caused execution fiasco.
     * <p>
     * Method should be used only after getting exclusively {@link State#FAILED} or {@link State#CRASHED} states from {@link #getState()}
     * call. Returns {@code null} for all other states.
     * <p>
     * This method is non thread-safe because it is expected that it will be called after thread-safe {@link #getState()} and fiasco cause
     * will be set before state change.
     *
     * @return exception that caused execution fiasco.
     */
    Exception getFiascoCause();

    /**
     * Represents state of application after start. Expected that initial state is determined by the end of {@link
     * Application#start(Object)} method work.
     */
    enum State {

        /**
         * Application successfully started and running.
         */
        RUNNING,

        /**
         * Application is shutting down at this moment.
         */
        SHUTTING_DOWN,

        /**
         * Application execution completed without errors.
         */
        COMPLETED,

        /**
         * Application crashed while was running and currently stopped.
         */
        CRASHED,

        /**
         * Application failed startup stage and didn't even run.
         */
        FAILED
    }
}
