import constant.ArgumentError;
import controller.ConsoleController;
import wrapper.ArgValidationResults;

import java.io.File;
import java.io.IOException;

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

        ConsoleController.run(validateArguments(args)); // this terminates unless it waits for user input

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
            if(isInvalidPath(argValidationResults.getInputFilePath()))
                argValidationResults.addError(ArgumentError.INPUT_PATH_FORMAT);
        }

        // Validace druhého argumentu (pripadne ziskani cesty k vystupnimu souboru)
        if (args.length == 2) {
            argValidationResults.setOutputFilePath(args[1]);
            if(isInvalidPath(argValidationResults.getOutputFilePath()))
                argValidationResults.addError(ArgumentError.OUTPUT_PATH_FORMAT);
        }

        return argValidationResults;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static boolean isInvalidPath(String path) {
        try {
            File file = new File(path);
            file.getCanonicalPath(); // https://www.javainuse.com/java/java-file-is-valid
            return false;
        } catch (IOException e) {
            return true;
        }
    }


}
