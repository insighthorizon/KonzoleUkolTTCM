package processing;

import java.util.List;
import java.util.function.BiFunction;

/**
 * utility class
 * - zadny stav, pouze staticka bez-stavova metoda
 */
public class NumbersProcessor {

    private NumbersProcessor() {} // never instantiate

    /**
     * Dve varianty funkce:
     * - vstup ma sudy pocet hodnot  ... vystup jsou suda cisla obsazena ve vstupu
     * - vstup ma lichy pocet hodnot ... vystup jsou licha cisla obsazena ve vstupu
     *
     * @param inputNumbers vstup
     * @return vystup
     */
    public static int[] process(List<Integer> inputNumbers) {
        /* Mame dve varianty. Bud chceme jen licha cisla, nebo jen suda cisla. Detekce sudych vs lichych cisel
        se lisi ciste v tom jestli vysledek modulo dvema ma nebo nema byt roven nule. == vs !=
        Pokud "do not reapeat yourself" za kazdou cenu, tak ...
         */
        BiFunction<Integer, Integer, Boolean> compareInts = (inputNumbers.size() % 2 == 0) ?
                (a, b) -> a == b :
                (a, b) -> a != b;

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

}
