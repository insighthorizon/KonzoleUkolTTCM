package processing;

import wrapper.ParsingResults;

/**
 * utility class
 * - zadny stav, pouze staticka bez-stavova metoda
 */
public class InputParser {

    private final static String TOKEN_SEPARATOR = "\\s+"; // whitespace regex

    private InputParser() {} // never instantiate

    public static ParsingResults parse(String rawInput) {
        ParsingResults parsingResults = new ParsingResults();

        // Zvoleny format pro ukladani v souboru je stajny jako pro zadavani v konzoli - mezery jsou oddelovac
        String[] inputTokens = rawInput.split(TOKEN_SEPARATOR);
        // na vstup pouzivam List<> misto pole, protoze predem nevime, kolik prvku bude mit - nejdrive je musime projit
        // my vime inputTokens.length, ale uzivatel mohl zadat i jine hodnoty nez cisla a ty je potreba zahodit
        for (String s : inputTokens) {
            try {
                parsingResults.addParsedNumber(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                parsingResults.addWarning(String.format("%nWarning: Hodnota \"" + s + "\" není validní číslo. Ve výsledcích bude přeskočena."));
            }
        }

        return parsingResults;
    }

}
