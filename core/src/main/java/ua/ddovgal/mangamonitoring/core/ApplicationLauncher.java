package ua.ddovgal.mangamonitoring.core;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to launch applications.
 */
@Slf4j
@UtilityClass
public class ApplicationLauncher {

    /**
     * Starts provided {@code application} and in case of successful start registers shutdown hook that stops started application.
     * <p/>
     * Method is non-blocking.
     *
     * @param application application to launch.
     * @param <E>         type of environment of launching application.
     * @param <A>         type of launching application.
     *
     * @return launched application execution object.
     */
    public <E, A extends Application<E>> ApplicationExecution launch(A application) {
        final E environment = application.getEnvironment();

        log.info("Starting application...");
        ApplicationExecution applicationExecution = application.start(environment);

        if (applicationExecution.getState() == ApplicationExecution.State.RUNNING) {
            log.info("Application successfully started");
            Runtime.getRuntime().addShutdownHook(new Thread(applicationExecution::shutdown, application.getClass().getName() + "-stopper"));
        } else {
            log.error("Start of application failed", applicationExecution.getFiascoCause());
        }

        return applicationExecution;
    }
}
