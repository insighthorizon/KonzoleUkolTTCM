import constant.ArgumentError;
import utility.ArgumentValidator;
import utility.InputParser;
import utility.NumbersProcessor;
import wrapper.ArgValidationResults;
import wrapper.ParsingResults;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This class contains the main control flow and all IO operations of the application
 * The private methods perform a lot of IO
 * - It's not possible to concentrate input/output operations in different places respectively,
 * because the specification demands them in certain sequence (ask user for input (output), get input, output again ...)
 *
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

        ArgValidationResults argValidationResults = ArgumentValidator.validate(args);
        if(handleArgumentErrors(argValidationResults))
            return;

        // Načtení vstupu (standard input, nebo soubor)
        String rawInput = loadInput(argValidationResults);
        if (rawInput.isEmpty())
            return;

        // Naparsování vstupu
        ParsingResults parsingResults = InputParser.parse(rawInput);
        for (String warning : parsingResults.getWarnings())
            System.out.println(warning);

        // ZPRACOVANI CISEL
        int[] outputNumbers = NumbersProcessor.process(parsingResults.getParsedNumbers());

        // Naformátování výstupu
        String outputString = Arrays.stream(outputNumbers)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));

        // VYDANI VYSTUPU (standard output nebo zapis do souboru)
        if (argValidationResults.getOutputFilePath() == null
        || argValidationResults.getOutputFilePath().isEmpty()) { // varianta vypis do konzole (standard output)
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
                //System.exit(1);
            }
        }

    }

    /**
     * Performs STD output
     * @param argValidationResults
     * @return
     */
    private static boolean handleArgumentErrors(ArgValidationResults argValidationResults) {
        for (ArgumentError argumentError : argValidationResults.getErrors()) // výpis problémů odchycených při validaci
            System.err.println(argumentError.getErrorMessage());

        if (!argValidationResults.getErrors().isEmpty()) { // ukončení programu z důvodu špatných argumentů
            System.out.println(String.format(
                    "%nKonec programu z důvodu chybných vstupů." +
                            " Zkuste to znovu s opravenými vstupy."));
            return true;
        }

        return false;
    }

    private static String loadInput(ArgValidationResults argValidationResults) {
        String rawInput = "";
        if (argValidationResults.getInputFilePath() == null
        || argValidationResults.getInputFilePath().isEmpty()) { // varianta nacteni z konzole (standard input)
            System.out.println("Prosím zadejte celá čísla oddělená mezerami, " +
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
                System.out.println("asfdasdf");
                System.out.println("...\"" + rawInput + "\"...");
            } catch (FileNotFoundException e) {
                System.err.println("Vstupní soubor nenalezen: " + argValidationResults.getInputFilePath());
                return rawInput;
            }
        }

        if (rawInput.trim().isEmpty())
            System.out.printf("%nWarning: Vstup neobsahuje žádná čísla.%n");

        return rawInput;
    }


}
