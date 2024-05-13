package wrappers.immutable_collections;

import java.util.Collection;

/**
 * Since {@link String} contains no references, this is guaranteed to be immutable on all levels.
 * @see ReadOnlyList
 */
public final class ROStrings extends ReadOnlyList<String> {
    public ROStrings(Collection<String> strings) {
        super(strings);
    }
}
