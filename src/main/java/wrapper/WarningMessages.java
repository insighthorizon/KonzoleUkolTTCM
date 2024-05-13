package wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class WarningMessages {
    private final List<String> warnings; // Upozornění na všechny stringy co nejdou naparsovat jako int

    public WarningMessages(List<String> warnings) {
        this.warnings = warnings;
    }

    /**
     * @return unmodofiable list of warnings
     */
    public List<String> getWarnings() {
//        List<String> warningsCopy = new ArrayList<>();
//        for (String s : warnings)
//            warningsCopy.add(s);
        return Collections.unmodifiableList(warnings);
    }


}
