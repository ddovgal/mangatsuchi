package ua.ddovgal.trackerkun.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ChaptersCondition
 */
@Data
public class ChaptersCondition {
    /**
     * lastUpdated
     */
    private LocalDateTime lastUpdated;
    /**
     * chaptersQuantity
     */
    private int chaptersQuantity;
    /**
     * latestChapter
     */
    private Chapter latestChapter; //todo: maybe its logically not same class ?
}
