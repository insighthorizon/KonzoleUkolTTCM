package wrappers.immutable_collections;


import wrappers.constants.ArgumentError;

import java.util.Collection;

/**
 * Since {@link ArgumentError} contains no references, this is guaranteed to be immutable on all levels.
 * @see ReadOnlyList
 */
public final class ROArgumentErrors extends ReadOnlyList<ArgumentError> {
    public ROArgumentErrors(Collection<ArgumentError> errors) {
        super(errors);
    }
}
