import wrappers.constants.ArgumentError;
import controllers.ConsoleController;
import wrappers.immutable_collections.ROArgumentErrors;
import wrappers.records.ArgValidationResults;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        final Set<ArgumentError> errors = new HashSet<>();
        String inputFilePath = "";
        String outputFilePath = "";

        // Posouzení počtu argumentů:
        if (args.length == 0) {
            errors.add(ArgumentError.NO_ARGS);
        } else { // we try to check the rest only if some arguments actually have been provided
            if (args.length > 2)
                errors.add(ArgumentError.TOO_MANY_ARGS);

            // Validace prvniho argumentu - muze byt kladne cele cislo, nebo cesta k souboru
            try {
                int parsedNumber = Integer.parseInt(args[0]);
                if (parsedNumber <= 0) { // podle specifikace je pouze kladne cislo spravne
                    errors.add(ArgumentError.WRONG_NUMBER);
                }
            } catch (NumberFormatException e) { // neni cele cislo, vstup má být ze souboru, načteme cestu
                inputFilePath = args[0];
                if (isInvalidPath(inputFilePath))
                    errors.add(ArgumentError.INPUT_PATH_FORMAT);
            }

            // Validace druhého argumentu (pripadne ziskani cesty k vystupnimu souboru)
            if (args.length == 2) {
                outputFilePath = args[1];
                if (isInvalidPath(outputFilePath))
                    errors.add(ArgumentError.OUTPUT_PATH_FORMAT);
            }
        }

        return new ArgValidationResults(inputFilePath, outputFilePath,
                new ROArgumentErrors(errors));
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
