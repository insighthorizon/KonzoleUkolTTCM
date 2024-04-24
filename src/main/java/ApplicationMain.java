import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Program prijima 1 az 2 argumenty ...
 * Veskera prace s IO je v teto tride.
 * Ostatni pouzite tridy nedelaji zadne IO a jsou bez-stavove.
 * <p>
 * Pokud vystupni soubor neexistuje, tak se vytvori.
 * Oddelovac hodnot v souboru je mezera, stejne tak je to udelano ve standard IO.
 */
public class ApplicationMain {
    public static void main(String[] args) {
        // VALIDACE ARGUMENTU
        FilePaths filePaths = validateArguments(args);

        // ZISKANI VSTUPU (standard input, nebo soubor)
        String rawInput = "";
        if (filePaths.inputFilePath.isEmpty()) { // varianta nacteni z konzole (standard input)
            System.out.println("Prosim zadejte cela cisla oddelena mezerami, potom stisknete ENTER. Jine hodnoty nez cisla budou ignorovany.");
            Scanner standardIOscanner = new Scanner(System.in);
            rawInput = standardIOscanner.nextLine().trim();
            standardIOscanner.close();
        } else { // varianta nacteni ze souboru
            try {
                Scanner fileScanner = new Scanner(new File(filePaths.inputFilePath));
                while (fileScanner.hasNext()) {
                    rawInput += fileScanner.nextLine();
                }
                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.err.println("Vstupni soubor nenalezen: " + filePaths.inputFilePath);
                System.exit(1);
            }
        }

        // NAPARSOVANI VSTUPU
        // Zvoleny format pro ukladani v souboru je stajny jako pro zadavani v konzoli - mezery jsou oddelovac
        String[] inputValues = rawInput.split("\\s+");
        // na vstup pouzivam List<> misto pole, protoze predem nevime, kolik prvku bude mit - nejdrive je musime projit
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
        int[] outputNumbers = NumberListProcessor.processNumbers(inputNumbers);

        // NAFORMATOVANI VYSTUPU
        String outputString = Arrays.stream(outputNumbers)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));

        // VYDANI VYSTUPU (standard output nebo zapis do souboru)
        if (filePaths.outputFilePath.isEmpty()) { // varianta vypis do konzole (standard output)
            System.out.println("=====Vystup====");
            System.out.println(outputString);
        } else { // varianta ulozeni do souboru
            try {
                File file = new File(filePaths.outputFilePath);
                if (file.createNewFile())
                    System.out.println("Soubor vytvoren: " + file.getName());

                FileWriter fileWriter = new FileWriter(filePaths.outputFilePath);
                fileWriter.write(outputString);
                fileWriter.close();
                System.out.println("Vystup zapsan do souboru: " + filePaths.outputFilePath);

            } catch (IOException e) {
                System.err.println("Nastala chyba pri vytvareni nebo nacitani vystupniho souboru.");
                System.exit(1);
            }
        }

    }

    private static FilePaths validateArguments(String[] args) {
         FilePaths filePaths = new FilePaths();

        // OVERENI POCTU ARGUMENTU
        // kontrola nespravneho poctu argumentu
        if (!(args.length == 1 || args.length == 2)) {
            System.err.println("\nNevalidni pocet parametru.");
            System.exit(1);
        }

        // VYHODNOCENI PRVNIHO ARGUMENTU (je to validni cislo, ci cesta k souboru?)
        filePaths.inputFilePath = ""; // pokud zustane prazdny string, vstup je ze standard input
        try {
            int n = Integer.parseInt(args[0]);
            if (n <= 0) {
                System.err.println("Pokud je prvni argument cele cislo, tak musi byt kladne.");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            filePaths.inputFilePath = args[0]; // vstup ma byt ze souboru, nacitame cestu
        }

        // VYHODNOCENI DRUHEHO ARGUMENTU (pripadne ziskani cesty k vystupnimu souboru)
        filePaths.outputFilePath = (args.length == 2) ? args[1] : ""; // prazdny String znamena ze vystup bude do standard output

        return filePaths;
    }

}
