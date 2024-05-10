import java.io.*;
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

        // validace command line argumentů
        ArgValidationResults argValidationResults = validateArguments(args);

        for (ArgumentError argumentError : argValidationResults.getErrors()) // výpis problémů odchycených při validaci
            System.err.println(argumentError.getErrorMessage());

        if (!argValidationResults.getErrors().isEmpty()) { // ukončení programu z důvodu špatných argumentů
            System.out.println(String.format(
                    "%nKonec programu z důvodu chybných vstupů." +
                    "Zkuste to znovu s opravenými vstupy."));
            return;
        }

        // Načtení vstupu (standard input, nebo soubor)
        String rawInput = "";
        if (argValidationResults.getInputFilePath().isEmpty()) { // varianta nacteni z konzole (standard input)
            System.out.println("Prosím zadejte celá čísla oddělená mezerami," +
                    "potom stisknete ENTER.");
            Scanner standardIOscanner = new Scanner(System.in);
            rawInput = standardIOscanner.nextLine().trim();
            standardIOscanner.close();
        } else { // varianta nacteni ze souboru
            try {
                Scanner fileScanner = new Scanner(new File(argValidationResults.getInputFilePath()));
                StringBuilder rawInputBuilder = new StringBuilder();
                while (fileScanner.hasNext()) {
                    rawInputBuilder.append(fileScanner.nextLine());
                }
                fileScanner.close();
                rawInput = rawInputBuilder.toString();
            } catch (FileNotFoundException e) {
                System.err.println("Vstupni soubor nenalezen: " + argValidationResults.getInputFilePath());
                System.exit(1);
            }
        }

        // Naparsování vstupu
        ParsingResults parsingResults = parseInput(rawInput);
        for (String warning : parsingResults.getWarnings())
            System.out.println(warning);


        // ZPRACOVANI CISEL
        int[] outputNumbers = NumberListProcessor.process(parsingResults.getParsedNumbers());

        // Naformátování výstupu
        String outputString = Arrays.stream(outputNumbers)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));

        // VYDANI VYSTUPU (standard output nebo zapis do souboru)
        if (argValidationResults.getOutputFilePath().isEmpty()) { // varianta vypis do konzole (standard output)
            System.out.println("=====Vystup====");
            System.out.println(outputString);
        } else { // varianta ulozeni do souboru
            try {
                /*
                 * !!! Pokud uz soubor existuje a neni prazdny, tak se zeptat uzivatele
                 */
                File file = new File(argValidationResults.getOutputFilePath());
                if (file.createNewFile())
                    System.out.println("Soubor vytvoren: " + file.getName());

                FileWriter fileWriter = new FileWriter(argValidationResults.getOutputFilePath());
                fileWriter.write(outputString);
                fileWriter.close();
                System.out.println("Vystup zapsan do souboru: " + argValidationResults.getOutputFilePath());

            } catch (IOException e) {
                System.err.println("Nastala chyba pri vytvareni nebo nacitani vystupniho souboru.");
                System.exit(1);
            }
        }

    }

    private static ParsingResults parseInput(String rawInput) {
        ParsingResults parsingResults = new ParsingResults();

        // Zvoleny format pro ukladani v souboru je stajny jako pro zadavani v konzoli - mezery jsou oddelovac
        String[] inputTokens = rawInput.split("\\s+");
        // na vstup pouzivam List<> misto pole, protoze predem nevime, kolik prvku bude mit - nejdrive je musime projit
        // my vime inputTokens.length, ale uzivatel mohl zadat i jine hodnoty nez cisla a ty je potreba zahodit
        for (String s : inputTokens) {
            try {
                parsingResults.addParsedNumber(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                parsingResults.addWarning(String.format("%nWarning: Hodnota \"" + s + "\" není validní číslo. Ve výsledcích bude přeskočena."));
            }
        }

        return  parsingResults;
    }


    private static ArgValidationResults validateArguments(String[] args) {
        ArgValidationResults argValidationResults = new ArgValidationResults();

        // Posouzení počtu argumentů:
        if (args.length == 0) {
            argValidationResults.addError(ArgumentError.NO_ARGS);
            return argValidationResults;
        }
        if (args.length > 2)
            argValidationResults.addError(ArgumentError.TOO_MANY_ARGS);

        // Validace prvniho argumentu - muze byt kladne cele cislo, nebo cesta k souboru
        try {
            int parsedNumber = Integer.parseInt(args[0]);
            if (parsedNumber <= 0) { // podle specifikace je pouze kladne cislo spravne
                argValidationResults.addError(ArgumentError.WRONG_NUMBER);
            }
        } catch (NumberFormatException e) { // neni cele cislo, vstup má být ze souboru, načteme cestu
            argValidationResults.setInputFilePath(args[0]);
            if(!isValidPath(argValidationResults.getInputFilePath()))
                argValidationResults.addError(ArgumentError.INPUT_PATH_FORMAT);
        }

        // Validace druhého argumentu (pripadne ziskani cesty k vystupnimu souboru)
        if (args.length == 2) {
            argValidationResults.setOutputFilePath(args[1]);
            if(!isValidPath(argValidationResults.getOutputFilePath()))
                argValidationResults.addError(ArgumentError.OUTPUT_PATH_FORMAT);
        }

        return argValidationResults;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static boolean isValidPath(String path) {
        try {
            File file = new File(path);
            file.getCanonicalPath(); // https://www.javainuse.com/java/java-file-is-valid
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
