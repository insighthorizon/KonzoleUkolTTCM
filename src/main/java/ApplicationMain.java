import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
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
        // Tato metoda by zatim mela resit clontrol flow, error handling a I/O

        // validace cmd line argumentů
        ArgValidationResults argValidationResults = validateArguments(args);
        for (ArgumentError argumentError : argValidationResults.errors) // vypis odchycenych chyb
            System.err.println(argumentError.getErrorMessage());
        if (!argValidationResults.errors.isEmpty()) { // ať si uživatel zadá argumenty správně
            System.out.println("%nKonec programu z důvodu chybných vstupů. Zkuste to znovu s opravenými vstupy.");
            return;
        }


        // ZISKANI VSTUPU (standard input, nebo soubor)
        String rawInput = "";
        if (argValidationResults.inputFilePath.isEmpty()) { // varianta nacteni z konzole (standard input)
            System.out.println("Prosim zadejte cela cisla oddelena mezerami, potom stisknete ENTER. Jine vstupy nez cela cisla budou ignorovany.");
            Scanner standardIOscanner = new Scanner(System.in);
            rawInput = standardIOscanner.nextLine().trim();
            standardIOscanner.close();
        } else { // varianta nacteni ze souboru
            try {

                File file = Paths.get(argValidationResults.inputFilePath).toFile();
                Scanner fileScanner = new Scanner(file /*new File(argValidationResults.inputFilePath)*/);
                while (fileScanner.hasNext()) {
                    rawInput += fileScanner.nextLine();
                }
                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.err.println("Vstupni soubor nenalezen: " + argValidationResults.inputFilePath);
                System.exit(1);
            }
        }

        // NAPARSOVANI VSTUPU
        // Zvoleny format pro ukladani v souboru je stajny jako pro zadavani v konzoli - mezery jsou oddelovac
        String[] inputTokens = rawInput.split("\\s+");
        // na vstup pouzivam List<> misto pole, protoze predem nevime, kolik prvku bude mit - nejdrive je musime projit
        // my vime inputValues.length, ale uzivatel mohl zadat i jine hodnoty nez cisla a ty je potreba zahodit
        ArrayList<Integer> inputNumbers = new ArrayList<>();
        for (String s : inputTokens) {
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
        if (argValidationResults.outputFilePath.isEmpty()) { // varianta vypis do konzole (standard output)
            System.out.println("=====Vystup====");
            System.out.println(outputString);
        } else { // varianta ulozeni do souboru
            try {
                File file = new File(argValidationResults.outputFilePath);
                if (file.createNewFile())
                    System.out.println("Soubor vytvoren: " + file.getName());

                FileWriter fileWriter = new FileWriter(argValidationResults.outputFilePath);
                fileWriter.write(outputString);
                fileWriter.close();
                System.out.println("Vystup zapsan do souboru: " + argValidationResults.outputFilePath);

            } catch (IOException e) {
                System.err.println("Nastala chyba pri vytvareni nebo nacitani vystupniho souboru.");
                System.exit(1);
            }
        }

    }

    private static ArgValidationResults validateArguments(String[] args) {
        ArgValidationResults argValidationResults = new ArgValidationResults();

        // Posouzení počtu argumentů:
        if (args.length == 0) {
            argValidationResults.errors.add(ArgumentError.NO_ARGS);
            return argValidationResults;
        }
        if (args.length > 2)
            argValidationResults.errors.add(ArgumentError.TOO_MANY_ARGS);

        // Validace argumentu
        try {
            argValidationResults.theNumber = Integer.parseInt(args[0]);
            // pokud předchozí řádek nebyl odchycen jako vyjímka, tak n je cislo
            if (argValidationResults.theNumber <= 0) { // podle specifikace je pouze kladne cislo spravne
                argValidationResults.errors.add(ArgumentError.WRONG_NUMBER);
            }
        } catch (NumberFormatException e) {
            argValidationResults.inputFilePath = args[0]; // vstup ma byt ze souboru, nacitame cestu
        }


        // Validace druhého argumentu (pripadne ziskani cesty k vystupnimu souboru)
        argValidationResults.outputFilePath = (args.length == 2) ? args[1] : ""; // prazdny String znamena ze vystup bude do standard output


        // validace cesty neexistujiciho souboru
        // https://www.baeldung.com/java-validate-filename
        // use io or nio? https://www.baeldung.com/java-path-vs-file
        //Paths.get(argValidationResults.inputFilePath);

        File file = new File(argValidationResults.inputFilePath);
        boolean created = false;
        try {
            file.getCanonicalPath(); // https://www.javainuse.com/java/java-file-is-valid
            //created = file.createNewFile();
        } catch (IOException e) {
            System.out.println("BAAAAAAAAAAD");
        } finally {
            if (created)
                file.delete();
        }


        return argValidationResults;
    }

}
