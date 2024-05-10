import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Just a wrapper class
 */
public class ParsingResults {
    private List<Integer> parsedNumbers = new ArrayList<>();
    private List<String> warnings = new ArrayList<>(); // Upozornění na všechny stringy co nejdou naparsovat jako int

    /**
     * @return unmodifiable list of the parsed numbers
     */
    public List<Integer> getParsedNumbers() {
        return Collections.unmodifiableList(parsedNumbers);
    }

    public void addParsedNumber(Integer n) {
        parsedNumbers.add(n);
    }

    /**
     * @return unmodofiable list of warnings
     */
    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

}

