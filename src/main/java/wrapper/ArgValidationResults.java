package wrapper;

import constant.ArgumentError;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Just a wrapper class
 */
public class ArgValidationResults {

    private String inputFilePath;
    private String outputFilePath;
    /**
     * Zde si přidělávám práci tím, že neukončím program když narazím na první chybu v uživatelských argumentech,
     * ale snažím se vyhodnotit i případně víc chyb najednou. Například:
     * Uživatel zadal nesprávný formát argumentu a ještě nesprávný počet argumentů
     *
     * Kdybychom si chtěli ušetřit práci, tak tu bude jen enum atribut, který bude obsahovat kosntantu OK, nebo
     * prnví chybu na kterou se narazí
     */
    private Set<ArgumentError> errors = new HashSet<>();


    public Set<ArgumentError> getErrors() {
//        Set<ArgumentError> errorsCopy = new HashSet<>();
//        for (ArgumentError error : errors)
//            errorsCopy.add(error);
        return Collections.unmodifiableSet(errors);
    }

    public void addError(ArgumentError error) {
        this.errors.add(error);
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

}
