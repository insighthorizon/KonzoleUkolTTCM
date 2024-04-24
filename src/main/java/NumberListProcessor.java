import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * utility trida
 * - zadny stav, pouze staticka bez-stavova metoda
 */
public class NumberListProcessor {

    /**
     * Dve varianty funkce:
     * - vstup ma sudy pocet hodnot  ... vystup jsou suda cisla obsazena ve vstupu
     * - vstup ma lichy pocet hodnot ... vystup jsou licha cisla obsazena ve vstupu
     * @param inputNumbers vstup
     * @return vystup
     */
    public static int[] processNumbers(ArrayList<Integer> inputNumbers) {
    // pouzijeme Stream API
    Predicate<Integer> filrationPredicate = inputNumbers.size() % 2 == 0 ?
            n -> (n % 2 == 0): // akceptuj suda cisla
            n -> (n % 2 != 0); // akceptuj licha cisla

    return inputNumbers.stream()
            .filter(filrationPredicate)
            .mapToInt(Integer::intValue)
            .toArray();

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

}
