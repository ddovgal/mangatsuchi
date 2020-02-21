package ua.ddovgal.trackerkun.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

import ua.ddovgal.trackerkun.core.exception.ApplicationStartupException;

/**
 * Abstract class that relaxes rather stringent requirements of {@link Application#start(Object)} and {@link Application#stop(Object)}
 * methods for exceptional cases. Instead of mentioned two methods defines own two, that are exception and fail-safe.
 */
@Slf4j
public abstract class RelaxedRequirementsApplication<E> implements Application<E> {

    private final LinkedList<Runnable> rollbacks = new LinkedList<>();
    private final List<Runnable> stoppingStages = new LinkedList<>();

    @Override
    public ApplicationExecution start(E environment) {
        try {
            startWithRollbackPossible(environment, rollbacks::add);
        } catch (Exception e) {
            Iterator<Runnable> rollbacksIterator = rollbacks.descendingIterator();
            runTasksConsequentlyExceptionsSafe(rollbacksIterator,
                                               ex -> log.error("Got exception while was stopping crashed on startup application", ex));
            return FailedApplicationExecution.dueTo(e);
        }

        return SimpleApplicationExecution.of(this, environment);
    }

    @Override
    public boolean stop(E runningEnvironment) {
        // actually just fills stopping stages
        stopDefiningStages(runningEnvironment, stoppingStages::add);

        AtomicBoolean successfullyStopped = new AtomicBoolean(false);
        runTasksConsequentlyExceptionsSafe(stoppingStages.iterator(), e -> {
            log.error("Got exception while was stopping application", e);
            successfullyStopped.set(false);
        });

        return successfullyStopped.get();
    }

    /**
     * A relaxed version of {@link Application#start(Object)} that allows fails and exception throwing.
     * <p>
     * The main concept of this method is that it always knows how be in case of exception. In addition to the familiar {@code environment}
     * it also provides {@code rollbackStack} that appends action that should be executed to do the rollback. And inside the method
     * implementation {@code rollbackStack} is called with rollback action right before executing actual one, in the process of steps are
     * completed within the method. In case of exception, rollback is carried out by reversed sequential execution of actions that were
     * already registered by {@code rollbackStack} till the moment of exception (thats why it's called stack). Then the structure of this
     * method implementation will differ from common {@link Application#start(Object)} one by the presence of gradual definition of rollback
     * actions (by additionally {@code rollbackStack} calls in other words).
     *
     * @param environment   environment of current application, in this scope it's also like some kind of config.
     * @param rollbackStack consumer that will register rollback action.
     *
     * @throws ApplicationStartupException in case of startup fails at some phase, some component couldn't be started.
     * @see BaseMangaMonitoringApplication as an implementation example.
     * @see Application#start(Object)  to get additional info about the original implementing method.
     */
    protected abstract void startWithRollbackPossible(E environment, Consumer<Runnable> rollbackStack) throws ApplicationStartupException;

    /**
     * A relaxed version of {@link Application#stop(Object)} that allows fails and exception throwing by the separate steps, but still
     * forbids exception throwing by method itself.
     * <p>
     * The main concept of this method is that instead of simply running all stopping at once, whole process splits into independent stages
     * which are registered by {@code stoppingStages} and then subsequently executed, wrapped by exception handling. Such a splitting
     * achieves ability to continue the stopping in case of failure on some stages.
     * <p>
     * Keep in mind that it is expected, that method implementation will specify step actions only, rather than actually running them. You
     * are still able to share data between steps using entire method scope variables, but than you should be twice careful and implement
     * the stopping actions given the fact that those variables could be set incorrectly due a preceding steps' failure.
     *
     * @param runningEnvironment environment with which application successfully started.
     * @param stoppingStages     consumer that will register independent stopping action.
     *
     * @see BaseMangaMonitoringApplication as an implementation example.
     * @see Application#stop(Object)  to get additional info about the original implementing method.
     */
    protected abstract void stopDefiningStages(E runningEnvironment, Consumer<Runnable> stoppingStages);

    private void runTasksConsequentlyExceptionsSafe(Iterator<Runnable> tasksIterator, Consumer<Exception> exceptionHandler) {
        while (tasksIterator.hasNext()) {
            Runnable action = tasksIterator.next();
            tasksIterator.remove();
            try {
                action.run();
            } catch (Exception e) {
                exceptionHandler.accept(e);
            }
        }
    }
}
