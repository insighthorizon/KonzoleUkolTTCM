package service;

import wrapper.ParsingResults;
import wrapper.ProcessingResults;
import wrapper.WarningMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * utility class
 * - zadny stav, pouze staticke bezstatove metody
 *
 * Controller has to call these methods to perform any data processing or file handling (persistance)
 */
public class NumbersService {
    private NumbersService() {} // never instantiate


    public static ProcessingResults performMainProcessing(String inputText) {
        final ParsingResults parsingResults = parseInputText(inputText);

        return new ProcessingResults(parsingResults.getWarnings(),
                formatOutput(filterNumbers(parsingResults.getParsedNumbers())));
    }


    private static ParsingResults parseInputText(String textInput) {
        final String TOKEN_SEPARATOR = "\\s+"; // whitespace regex
        List<String> warnings = new ArrayList<>();
        List<Integer> inputNumbers = new ArrayList<>();

        // Zvoleny format pro ukladani v souboru je stejny jako pro zadavani v konzoli - mezery jsou oddelovac
        final String[] inputTokens = textInput.split(TOKEN_SEPARATOR);
        // na inputNumbers pouzivam List<> misto pole, protoze predem nevime, kolik prvku bude mit - nejdrive je musime projit
        // my vime inputTokens.length, ale uzivatel mohl zadat i jine hodnoty nez cisla a ty je potreba zahodit
        for (String token : inputTokens) {
            try {
                inputNumbers.add(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                warnings.add(String.format("%nWarning: Hodnota \"" + token + "\" není validní číslo. Ve výsledcích nebude obsažena."));
            }
        }

        return new ParsingResults(warnings, inputNumbers);
    }


    /**
     * Dve varianty funkce:
     * - vstup ma sudy pocet hodnot  ... vystup jsou suda cisla obsazena ve vstupu
     * - vstup ma lichy pocet hodnot ... vystup jsou licha cisla obsazena ve vstupu
     *
     * @param inputNumbers vstup
     * @return vystup
     */
    private static int[] filterNumbers(List<Integer> inputNumbers) {
        /* Mame dve varianty. Bud chceme jen licha cisla, nebo jen suda cisla. Detekce sudych vs lichych cisel
        se lisi ciste v tom jestli vysledek modulo dvema ma nebo nema byt roven nule. == vs !=
        Pokud "do not reapeat yourself" za kazdou cenu, tak ...
         */
        final BiFunction<Integer, Integer, Boolean> compareInts = (inputNumbers.size() % 2 == 0) ?
                Integer::equals :
                (a, b) -> !a.equals(b);

        return inputNumbers.stream()
                .filter(n -> compareInts.apply(n % 2, 0)) // filtrujeme skrze delitelnost/nedelitelnost 2
                .mapToInt(Integer::intValue)
                .toArray();

        // puvodne jsem stream filtroval timto (urcite pochopitelnejsi kod, ale jsou tam dva vyrazy skoro stejne
//        Predicate<Integer> filrationPredicate = (inputNumbers.size() % 2 == 0) ?
//                n -> (n % 2 == 0) : // akceptuj jen suda cisla
//                n -> (n % 2 != 0); // akceptuj jen licha cisla

        // Puvodne jsem vymyslel tohle. Ale je tam dvakrat skoro stejna smycka, jenom s rozdilem boolean operatoru.
//        ArrayList<Integer> vystupniCisla = new ArrayList<>();
//        if (vstupniCisla.size() % 2 == 0) { // test lichosti
//            // suda cisla
//            for (Integer cislo : vstupniCisla)
//                if (cislo % 2 == 0)
//                    vystupniCisla.add(cislo);
//        } else {
//            // licha cisla
//            for (Integer cislo : vstupniCisla)
//                if (cislo % 2 != 0)
//                    vystupniCisla.add(cislo);
//        }
    }

    private static String formatOutput(int[] outputNumbers) {
        // Převod výstupu na text
        return Arrays.stream(outputNumbers)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
    }


}
