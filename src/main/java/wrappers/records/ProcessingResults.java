package wrappers.records;

import wrappers.immutable_collections.ROStrings;

public record ProcessingResults(String outputText, ROStrings warnings) {
}

