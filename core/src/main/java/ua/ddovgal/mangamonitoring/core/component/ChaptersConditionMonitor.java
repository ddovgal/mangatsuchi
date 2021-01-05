package ua.ddovgal.mangamonitoring.core.component;

import java.util.function.BiConsumer;

import ua.ddovgal.mangamonitoring.core.ManagedComponent;
import ua.ddovgal.mangamonitoring.domain.ChaptersCondition;
import ua.ddovgal.mangamonitoring.domain.Manga;

/**
 * The main purpose of this component is to monitor manga chapter conditions. Acts like some data provider you can subscribe to. The
 * principle of work is different for each implementation, but the general thing is that it emits chapters condition to subscribed
 * consumers, and then they should handle and process emitted actual chapter condition.
 */
public interface ChaptersConditionMonitor extends ManagedComponent {

    /**
     * Subscribe to chapter condition events, which are represents state of manga chapters at the moment of emitting.
     *
     * @param consumer subscriber of chapter condition events.
     */
    void subscribe(BiConsumer<Manga, ChaptersCondition> consumer);
}
