package controllers;

import wrappers.constants.ArgumentError;
import wrappers.immutable_collections.ROArgumentErrors;
import persistence.FileHandler;
import services.NumbersService;
import wrappers.records.ArgValidationResults;
import wrappers.records.ProcessingResults;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class performs:
 * - all STD IO (user interface) operations
 * - calls to the service layer
 */
public class ConsoleController {
    private ConsoleController() {
    }

    public static void run(ArgValidationResults argValidationResults) {

        performApplicationTasks(argValidationResults); // main control flow of the application

        System.out.printf("%nKonec programu.%n");
    }

    /**
     * The application state machine / control flow
     * (Simple state machine without backtracking/cycles)
     *
     * @param argValidationResults
     */
    private static void performApplicationTasks(ArgValidationResults argValidationResults) {
        final ROArgumentErrors argumentErrors = argValidationResults.errors();
        final String inputFilePath = argValidationResults.inputFilePath();
        final String outputFilePath = argValidationResults.outputFilePath();

        // display argument errors
        for (final ArgumentError argumentError : argumentErrors)
            System.err.printf(argumentError.getErrorMessage());

        if(!argumentErrors.isEmpty())
            return;

        // Načtení vstupu (standard input, nebo soubor)
        final String inputText;
        if (inputFilePath.isEmpty()) {
            inputText = requestUserInput();
        } else {
            try {
                inputText = NumbersService.getFileInput(inputFilePath);
            }
            catch (FileNotFoundException e) {
                System.err.printf("Vstupní soubor neexistuje.");
                return;
            }
        }

        if (inputText.trim().isEmpty()) {
            System.out.printf("%nWarning: Vstup neobsahuje žádná čísla ke zpracování.%n");
            return;
        }

        // Zpracování vstupu - performing main logic
        final ProcessingResults processingResults = NumbersService.performMainProcessing(inputText);

        processingResults.warnings().forEach(System.out::printf);


        // Zápis výstupu do konzole nebo do souboru
        if (outputFilePath.isEmpty()) { // varianta vypis do konzole (standard output)
            System.out.printf("%n=====Vystup====%n");
            System.out.printf(processingResults.outputText());
            System.out.printf("%n===============%n");
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
                NumbersService.saveFile(outputFilePath, processingResults.outputText());

                System.out.printf("Vystup zapsan do souboru: " + outputFilePath);
            } catch (IOException e) {
                System.err.printf("%nNastala chyba pri vytvareni nebo nacitani vystupniho souboru.%n");
            }
        }

    }


    private static String requestUserInput() {
        String inputText;

        System.out.printf("%nProsím zadejte celá čísla oddělená mezerami a potom  stiskněte ENTER.%n");
        Scanner standardIOscanner = new Scanner(System.in);
        inputText = standardIOscanner.nextLine().trim();
        standardIOscanner.close();

        return inputText;
    }

    private static String loadFileInput(String inputFilePath) throws FileNotFoundException {
        String inputText;

        try {
            inputText = FileHandler.loadFromFile(inputFilePath);
        } catch (FileNotFoundException e) {
            System.err.printf("Vstupní soubor nenalezen: " + inputFilePath);
            throw e;
        }

        return inputText;
    }

}
