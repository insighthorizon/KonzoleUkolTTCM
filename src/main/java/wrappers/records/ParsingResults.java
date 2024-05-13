package wrappers.records;

import wrappers.immutable_collections.ROIntegers;
import wrappers.immutable_collections.ROStrings;

public record ParsingResults(ROIntegers parsedNumbers, ROStrings warnings) {
}
