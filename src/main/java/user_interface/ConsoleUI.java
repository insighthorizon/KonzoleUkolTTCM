package user_interface;

import constant.ArgumentError;
import persistence.LocalFileHandler;
import processing.InputParser;
import processing.NumbersProcessor;
import wrapper.ArgValidationResults;
import wrapper.ParsingResults;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This class performs:
 * - all STD IO (user interface) operations
 * - calls to the controller layer (actions to perform)
 */
public class ConsoleUI {
    private ConsoleUI() {}

    public static void run(ArgValidationResults argValidationResults) {

        performUITasks(argValidationResults); // main control flow of the application

        System.out.printf("%nKonec programu.%n");
    }

    private static void performUITasks(ArgValidationResults argValidationResults) {
        // výpis problémů odchycených při validaci
        for (ArgumentError argumentError : argValidationResults.getErrors())
            System.err.printf(argumentError.getErrorMessage());

        if (!argValidationResults.getErrors().isEmpty()) // ukončení programu z důvodu špatných argumentů
            return;

        // Načtení vstupu (standard input, nebo soubor)
        String rawInput;
        if (argValidationResults.getInputFilePath() == null
                || argValidationResults.getInputFilePath().isEmpty()) { // varianta nacteni z konzole (standard input)
            System.out.printf("%nProsím zadejte celá čísla oddělená mezerami, " +
                    "potom stisknete ENTER.%n");
            Scanner standardIOscanner = new Scanner(System.in);
            rawInput = standardIOscanner.nextLine().trim();
            standardIOscanner.close();
        } else { // varianta nacteni ze souboru
            try {
                rawInput = LocalFileHandler.loadFromFile(argValidationResults.getInputFilePath());
            } catch (FileNotFoundException e) {
                System.err.printf("Vstupní soubor nenalezen: " + argValidationResults.getInputFilePath());
                return;
            }
        }

        if (rawInput.trim().isEmpty())
            System.out.printf("%nWarning: Vstup neobsahuje žádná čísla ke zpracování. Konec programu.%n");


        if (rawInput.isEmpty())
            return;

        // Naparsování vstupu
        ParsingResults parsingResults = InputParser.parse(rawInput);
        for (String warning : parsingResults.getWarnings())
            System.out.printf(warning);

        // ZPRACOVANI CISEL
        int[] outputNumbers = NumbersProcessor.process(parsingResults.getParsedNumbers());

        // Naformátování výstupu
        String outputText = Arrays.stream(outputNumbers)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));

        // VYDANI VYSTUPU (standard output nebo zapis do souboru)
        if (argValidationResults.getOutputFilePath() == null
                || argValidationResults.getOutputFilePath().isEmpty()) { // varianta vypis do konzole (standard output)
            System.out.printf("%n=====Vystup====%n");
            System.out.printf(outputText);
        } else { // varianta ulozeni do souboru
            try {
                /*
                 * !!! Pokud uz soubor existuje a neni prazdny, tak bude prepsan
                 * Ptat se uzivatele jestli nahodou chce jednu z techto variant se mi vazne nechce:
                 * 1. prepsat soubor
                 * 2. doplnit data do souboru (append)
                 * 3. radeji vypsat do STD IO
                 * 4. radeji nedelat nic
                 */
                LocalFileHandler.overwriteFile(argValidationResults.getOutputFilePath(), outputText);

                System.out.printf("Vystup zapsan do souboru: " + argValidationResults.getOutputFilePath());
            } catch (IOException e) {
                System.err.printf("%nNastala chyba pri vytvareni nebo nacitani vystupniho souboru.%n");
            }
        }

    }

}
