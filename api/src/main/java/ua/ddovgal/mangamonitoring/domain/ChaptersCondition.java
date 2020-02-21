package ua.ddovgal.mangamonitoring.domain;

import java.time.LocalDateTime;

import lombok.Data;

import ua.ddovgal.mangamonitoring.api.MangaProvider;

/**
 * Pojo that describes manga chapters overall condition.
 */
@Data
public class ChaptersCondition {

    /**
     * Latest known chapter of manga.
     */
    private Chapter latestChapter;

    /**
     * Datetime provided by {@link MangaProvider} that tells when {@code latestChapter} was updated (or added, if there were no updates
     * yet).
     */
    private LocalDateTime lastUpdated;

    /**
     * Total number of chapters. Equal to size of chapters list. It could be larger than {@code latestChapter}'s {@code releaseNumber}
     * value. There are cases when the chapter has, for example {@code releaseNumber} of {@code 44 pt. 3} but it's 49'th in the list, then
     * {@code chaptersQuantity} will be equal to 49.
     */
    private int chaptersQuantity;
}
