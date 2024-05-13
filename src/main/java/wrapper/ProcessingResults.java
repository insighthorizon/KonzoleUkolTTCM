package wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Just a wrapper class
 */
public class ProcessingResults extends WarningMessages {
    private final String outputText;

    public ProcessingResults(List<String> warnings, String outputText) {
        super(warnings);
        this.outputText = outputText;
    }

    public String getOutputText() {
        return outputText;
    }

}

