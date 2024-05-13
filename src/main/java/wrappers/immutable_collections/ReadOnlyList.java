package wrappers.immutable_collections;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * If T is immutable, this is guaranteed to be immutable.
 * IF T has mutable fields and reference those are saved somewhere outside of this class,
 * then this collection isn't guaranteed to be immutable. It's about the depth of the copy.
 * This class handles only the level of depth (so does ImmutableList).
 * @param <T>
 */
public abstract class ReadOnlyList<T> implements Iterable<T>{
    // Samotne Guava ImmutableList mi nestaci - to resi problem az v runtime, nechceme mit zadny pristup k "unsupported operations"
    // unmodifiable collection - take az v runtime
    private final T[] storedEntries;
    private final int size;


    @SuppressWarnings("unchecked")
    public ReadOnlyList(Collection<T> collection) {
        this.storedEntries = (T[]) collection.toArray();
        this.size = collection.size();
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.stream(storedEntries).iterator();
    }

    public boolean isEmpty() {
        return storedEntries.length == 0;
    }

    public int size() {
        return size;
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

}

/*
Example of how it can go wrong:
        ArrayList<String> a = new ArrayList<>(Arrays.asList("1", "2"));
        List<String> b = new ArrayList<>(Arrays.asList("alpha", "beta"));

        ImmutableList<List<String>> ss = ImmutableList.copyOf(List.of(a,b));

        for (List<String> s : ss) {
            System.out.println(s);
        }
//        prints:
//        [1, 2]
//        [alpha, beta]

        System.out.println();
        a.add("3");
        b.add("gamma");

        for (List<String> s : ss) {
            System.out.println(s);
        }
//        prints:
//        [1, 2, 3]
//        [alpha, beta, gamma]

// of course if Lists a and b are coppied too, then it would already be a deep copy and it would be alright.
 */