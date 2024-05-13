package services;

import wrappers.immutable_collections.ROIntegers;
import wrappers.immutable_collections.ROStrings;
import persistence.FileHandler;
import wrappers.records.ParsingResults;
import wrappers.records.ProcessingResults;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * utility class
 * - zadny stav, pouze staticke bezstatove metody
 * <br>
 * Controller has to call these methods to perform any data processing or file handling (persistance)
 */
public class NumbersService {
    private NumbersService() {} // never instantiate

    public static String getFileInput(String inputFilePath) throws FileNotFoundException {
        return FileHandler.loadFromFile(inputFilePath);
    }

    public static void saveFile(String outputFilePath, String outputText) throws IOException {
        FileHandler.overwriteFile(outputFilePath, outputText);
    }


    public static ProcessingResults performMainProcessing(String inputText) {
        final ParsingResults parsingResults = parseInputText(inputText);

        return new ProcessingResults(formatOutput(filterNumbers(parsingResults.parsedNumbers())),
                parsingResults.warnings());
    }


    private static ParsingResults parseInputText(String textInput) {
        // Zvoleny format pro ukladani v souboru je stejny jako pro zadavani v konzoli - mezery jsou oddelovac
        final String TOKEN_SEPARATOR = "\\s+"; // whitespace regex
        List<String> warnings = new ArrayList<>();
        List<Integer> inputNumbers = new ArrayList<>();
        // na inputNumbers pouzivam List<> misto pole, protoze predem nevime, kolik prvku bude mit - nejdrive je musime projit
        // my vime inputTokens.length, ale uzivatel mohl zadat i jine hodnoty nez cisla a ty je potreba zahodit

        final String[] inputTokens = textInput.split(TOKEN_SEPARATOR);
        for (final String token : inputTokens) {
            try {
                inputNumbers.add(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                warnings.add(String.format("%nWarning: Hodnota \"" + token + "\" není validní číslo. Ve výsledcích nebude obsažena."));
            }
        }

        return new ParsingResults(new ROIntegers(inputNumbers), new ROStrings(warnings));
    }


    /**
     * Dve varianty funkce:
     * - vstup ma sudy pocet hodnot  ... vystup jsou suda cisla obsazena ve vstupu
     * - vstup ma lichy pocet hodnot ... vystup jsou licha cisla obsazena ve vstupu
     *
     * @param inputNumbers vstup
     * @return vystup
     */
    private static ROIntegers filterNumbers(ROIntegers inputNumbers) {
        /* Mame dve varianty. Bud chceme jen licha cisla, nebo jen suda cisla. Detekce sudych vs lichych cisel
        se lisi ciste v tom jestli vysledek modulo dvema ma nebo nema byt roven nule. == vs !=
        Pokud "do not reapeat yourself" za kazdou cenu, tak ...
         */
        final BiFunction<Integer, Integer, Boolean> compareInts = (inputNumbers.size() % 2 == 0) ?
                Integer::equals :
                (a, b) -> !a.equals(b);

        return new ROIntegers(inputNumbers.stream()
                .filter(n -> compareInts.apply(n % 2, 0)) // filtrujeme skrze delitelnost/nedelitelnost 2
                .toList());
    }

    private static String formatOutput(ROIntegers outputNumbers) {
        // Převod výstupu na text
        return outputNumbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

}
