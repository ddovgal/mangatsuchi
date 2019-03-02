package ua.ddovgal.trackerkun.domain;

import java.time.LocalDateTime;

import lombok.Data;

import ua.ddovgal.trackerkun.api.MangaProvider;

/**
 * Class describing some manga chapters overall condition.
 */
@Data
public class ChaptersCondition {

    /**
     * Latest known chapter of manga.
     */
    private Chapter latestChapter;
    /**
     * Datetime, provided by {@link MangaProvider}, telling when last in chapters list chapter was updated (or added, if there were no
     * updates yet).
     */
    private LocalDateTime lastUpdated;
    /**
     * Total number of chapters. Equal to size of chapters list. It could be larger than {@code latestChapter}'s {@code releaseNumber}
     * value. There could be a situation when the chapter will have {@code releaseNumber} of {@code 44 pt. 3} and be 49'th in the list, so
     * {@code chaptersQuantity} would be 49.
     */
    private int chaptersQuantity;
}
