package ua.ddovgal.mangamonitoring.state.util;

import lombok.Value;

/**
 * What can I say ?.. It's a pair.
 *
 * @param <L> left object type.
 * @param <R> right object type.
 */
@Value(staticConstructor = "of")
public class Pair<L, R> {

    L left;
    R right;
}
