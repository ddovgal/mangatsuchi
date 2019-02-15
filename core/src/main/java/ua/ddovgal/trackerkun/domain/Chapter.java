package ua.ddovgal.trackerkun.domain;

import lombok.Data;

/**
 * Chapter
 */
@Data
public class Chapter {
    /**
     * title
     */
    private String title;
    /**
     * releaseListNumber
     */
    private int releaseListNumber; //todo: do i need this field ? cus there is `ownFormatReleaseNumber`
    /**
     * ownFormatReleaseNumber
     */
    private String ownFormatReleaseNumber;
    /**
     * sourceIdentifier
     */
    private String sourceIdentifier;
}
