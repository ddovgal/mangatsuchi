package ua.ddovgal.trackerkun.core;

import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

import ua.ddovgal.trackerkun.core.component.ChaptersConditionMonitor;
import ua.ddovgal.trackerkun.core.component.UserInputListener;
import ua.ddovgal.trackerkun.core.exception.ApplicationStartupException;

/**
 * First actually manga-monitoring related {@link Application} partial implementation that use self-defined {@link BaseEnvironment} as
 * environment and hence already knows how to be launched.
 * <p>
 * Extends {@link RelaxedRequirementsApplication} so its start and stop method can focus on actual application logic.
 * <p>
 * Actual final implementation should just create own extended environment and implement {@link Application#getEnvironment()} in any
 * convenient way: it could require already prepared and built environment as input and then just return it, or build a new environment
 * object inside {@link Application#getEnvironment()} method itself.
 *
 * @see RelaxedRequirementsApplication for start and stop methods requirements relaxation.
 * @see BaseEnvironment for involved components additional info.
 */
@Slf4j
public abstract class BaseMangaMonitoringApplication
    extends RelaxedRequirementsApplication<BaseMangaMonitoringApplication.BaseEnvironment> {

    @Override
    protected void startWithRollbackPossible(BaseEnvironment environment,
                                             Consumer<Runnable> rollbackStack) throws ApplicationStartupException {
        UserInputListener userInputListener = environment.getUserInputListener();
        ChaptersConditionMonitor chaptersConditionMonitor = environment.getChaptersConditionMonitor();

        rollbackStack.accept(userInputListener::stop);
        userInputListener.start();

        rollbackStack.accept(chaptersConditionMonitor::stop);
        chaptersConditionMonitor.start();
    }

    @Override
    protected void stopDefiningStages(BaseEnvironment runningEnvironment, Consumer<Runnable> stoppingStages) {
        ChaptersConditionMonitor chaptersConditionMonitor = runningEnvironment.getChaptersConditionMonitor();
        UserInputListener userInputListener = runningEnvironment.getUserInputListener();

        stoppingStages.accept(chaptersConditionMonitor::stop);
        stoppingStages.accept(userInputListener::stop);
    }

    /**
     * Base environment, requires only necessary for {@link BaseMangaMonitoringApplication} components: {@link UserInputListener} as
     * requests handler i.e user interactions and {@link ChaptersConditionMonitor} as a chapters' tracker. Those two components intended to
     * be main acting units of manga-monitoring application and run each in his own thread.
     * <p>
     * Actual implementations should provide necessary base components and could extend base environment with own private components.
     */
    interface BaseEnvironment {

        ChaptersConditionMonitor getChaptersConditionMonitor();
        UserInputListener getUserInputListener();
    }
}
