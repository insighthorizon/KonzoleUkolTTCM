package wrappers.immutable_collections;

import java.util.Collection;

/**
 * Since {@link Integer} links to no mutable objects, this is guaranteed to be immutable on all levels.
 * @see ReadOnlyList
 */
public final class ROIntegers extends ReadOnlyList<Integer>{
    public ROIntegers(Collection<Integer> integers) {
        super(integers);
    }
}
