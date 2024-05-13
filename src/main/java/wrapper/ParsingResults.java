package wrapper;

import java.util.Collections;
import java.util.List;

public class ParsingResults extends WarningMessages {

    private final List<Integer> parsedNumbers;

    public ParsingResults(List<String> warnings, List<Integer> parsedNumbers) {
        super(warnings);
        this.parsedNumbers = parsedNumbers;
    }

    /**
     * @return unmodifiable list of the parsed numbers
     */
    public List<Integer> getParsedNumbers() {
        //        List<Integer> parsedNumbersCopy = new ArrayList<>();
        //        for (Integer n : parsedNumbers)
        //            parsedNumbersCopy.add(n);
        return Collections.unmodifiableList(parsedNumbers);
    }

}
