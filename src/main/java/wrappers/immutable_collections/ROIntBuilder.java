package wrappers.immutable_collections;

import java.util.ArrayList;

/**
 * This can be used to collect from stream into ROIntegers:
 * .collect(Collector.of(
 *                         ROIntBuilder::new,
 *                         ROIntBuilder::add,
 *                         ROIntBuilder::combine,
 *                         ROIntBuilder::build
 *                 ));
 * <br>
 * It's completely useless. Because it works with a list anyway. So we can just do
 * .toList() instead and then instantiate ROIntegers from that.
 */
public class ROIntBuilder {
    ArrayList<Integer> data = new ArrayList<>();

    public void add(Integer i) {
        this.data.add(i);
    }

    public ROIntBuilder combine(ROIntBuilder other) {
        this.data.addAll(other.data);
        return this;
    }

    public ROIntegers build() {
        return new ROIntegers(this.data);
    }
}
