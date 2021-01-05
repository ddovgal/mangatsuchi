package ua.ddovgal.mangamonitoring.state.utils;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import ua.ddovgal.mangamonitoring.state.Impact;
import ua.ddovgal.mangamonitoring.state.State;
import ua.ddovgal.mangamonitoring.state.transition.TransitionDescriptor;

import static ua.ddovgal.mangamonitoring.state.utils.DescriptorUtils.descriptor;

/**
 * Just dummy empty states set for test proposes.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class States {

    /**
     * <pre>
     * Forms the following graph
     *
     *             +---------+
     *             |  BEGIN  |
     *             +----+----+
     *                  | 1
     *                  v
     *             +----+----+
     *      +---+->+   FOO   +---+
     *      |   ^  +----+----+   | 8
     *      |   |       | 2      |
     *      | 6 | 7     |        |
     *      |   |       v        |
     *      |   |  +----+----+   |
     *      |   +--+   BAR   |   |
     *      |      +-+-----+-+   |
     *      |        ^     | 3   |
     *      |        |     |     |
     *      |        | 4   v     |
     *      |      +-+-----+-+   |
     *      +------+   BAZ   +<--+
     *             +-+-----+-+
     *               | 5   ^
     *               |     |
     *               +-----+
     *
     * Generated with http://asciiflow.com/
     * </pre>
     */
    public static final List<TransitionDescriptor<?, ?, Impact, Object>> TEST_GRAPH_DESCRIPTORS = List.of(
        descriptor(BeginState.class, FooState.class),
        descriptor(FooState.class, BarState.class),
        descriptor(BarState.class, BazState.class),
        descriptor(BazState.class, BarState.class),
        descriptor(BazState.class, BazState.class),
        descriptor(BazState.class, FooState.class),
        descriptor(BarState.class, FooState.class),
        descriptor(FooState.class, BazState.class)
    );

    public static final class BeginState implements State {}
    public static final class FooState implements State {}
    public static final class BarState implements State {}
    public static final class BazState implements State {}
}
