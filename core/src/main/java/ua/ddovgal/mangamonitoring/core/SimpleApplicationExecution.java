package ua.ddovgal.mangamonitoring.core;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Default and simple execution implementation. For more details check an implemented interface and its methods description.
 *
 * @param <E> type of environment of an executing application.
 *
 * @see ua.ddovgal.mangamonitoring.core.ApplicationExecution
 */
@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class SimpleApplicationExecution<E> implements ApplicationExecution {

    private final Application<E> application;
    private final E environment;

    private final AtomicReference<State> state = new AtomicReference<>(State.RUNNING);
    private final CountDownLatch runningLatch = new CountDownLatch(1);

    @Getter
    private Exception fiascoCause = null; // intended to be read after state, wherein is set before state, hence it could be thread-unsafe

    @Override
    public void waitTillCompletion() throws InterruptedException {
        runningLatch.await();
    }

    @Override
    public void shutdown() {
        // null crashCause means normal non-exceptional shutdown
        shutdown(null);
    }

    @Override
    public void shutdownCrashed(Exception crashCause) {
        shutdown(crashCause);
    }

    @Override
    public State getState() {
        return state.get();
    }

    /**
     * Thread-safe, meaning that only one thread at the same time will be able to actually stop execution.
     * <p>
     * Null crashCause means non-exceptional shutdown.
     */
    private void shutdown(Exception crashCause) {
        if (!state.compareAndSet(State.RUNNING, State.SHUTTING_DOWN)) {
            throw new IllegalStateException("Application already in not RUNNING state");
        }

        boolean shadowingCrashed = Objects.nonNull(crashCause);
        if (shadowingCrashed) {
            log.error("Application unrecoverably crashed which required shutdown", crashCause);
            fiascoCause = crashCause;
        }

        log.info("Shutting down application...");
        boolean stoppedSuccessfully = application.stop(environment);

        if (stoppedSuccessfully) {
            log.info("Application successfully shut down");
        } else {
            log.error(shadowingCrashed ? "Shutdown of already crashed application failed" : "Shutdown of application failed");
        }

        state.set(shadowingCrashed ? State.CRASHED : State.COMPLETED);
        runningLatch.countDown();
    }
}
