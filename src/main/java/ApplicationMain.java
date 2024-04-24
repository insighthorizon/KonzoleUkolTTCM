import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Veskera prace s IO je v main metode.
 * Vsechny metody volane z ostatnich trid jsou bez-stavove a nedelaji zadne IO.
 */
public class ApplicationMain {
    public static void main(String[] args) {

        // OVERENI ARGUMENTU
        // kontrola nespravneho poctu argumentu
        if(!(args.length == 1 || args.length == 2)) {
            System.out.println("\nNevalidni pocet parametru.");
            return;
        }

        // VYHODNOCENI PRNVIHO ARGUMENTU (je to validni cislo, ci cesta k souboru?)
        try {
            int n = Integer.parseInt(args[0]);
            if (n <= 0) {
                System.out.println("Prvni parametr musi byt kladne cele cislo.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Prvni parametr musi byt cele cislo.");
            return;
        }

        // TODO VYHODNOCENI ARGUMENTU
        String vstupniSoubor = "";
        String vystupniSoubor = "";


        // ZISKANI VSTUPU
        String rawInput;
        if (vstupniSoubor.isEmpty()) { // varianta nacteni z konzole (standard input)
            System.out.println("Prosim zadejte cela cisla oddelena mezerami, potom stisknete ENTER. Jine hodnoty nez cisla budou ignorovany.");
            Scanner standardIOscanner = new Scanner(System.in);
            rawInput = standardIOscanner.nextLine().trim();
            // TODO varianta nacteni ze souboru
        } else {
            rawInput = "";
        }

        // NAPARSOVANI VSTUPU
        String[] inputValues = rawInput.split("\\s+");
        // na vstup pouzivam List<> misto pole, protoze predem nevime, kolik prvku bude mit - nejprv je musime projit
        // my vime inputValues.length, ale uzivatel mohl zadat i jine hodnoty nez cisla a ty je potreba zahodit
        ArrayList<Integer> inputNumbers = new ArrayList<>();
        for (String s : inputValues) {
            try {
                inputNumbers.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                System.out.println("Upozorneni: Hodnota \"" + s + "\" neni validni. Nebude vubec uvazovana.");
            }
        }

        // ZPRACOVANI CISEL
        int[] vystupniCisla = NumberListProcessor.processNumbers(inputNumbers);

        // NAFORMATOVANI VYSTUPU
        String vystup = Arrays.stream(vystupniCisla)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));

        // PREZENTACE VYSTUPU
        if (vystupniSoubor.isEmpty()) { // varianta vypis do konzole (standard output)
            System.out.println("=====Vystup====");
            System.out.println(vystup);
        } else { // TODO varianta ulozeni do souboru

        }

    }
}
