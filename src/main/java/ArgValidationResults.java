import java.util.HashSet;
import java.util.Set;

public class ArgValidationResults {

    public boolean inputFilePathProvided;

    public int theNumber;
    public String inputFilePath;
    public String outputFilePath;

    /**
     * Zde si přidělávám práci tím, že neukončím program když narazím na první chybu v uživatelských argumentech,
     * ale snažím se vyhodnotit i případně víc chyb najednou. Například:
     * Uživatel zadal nesprávný formát argumentu a ještě nesprávný počet argumentů
     *
     * Kdybychom si chtěli ušetřit práci, tak tu bude jen enum atribut, který bude obsahovat kosntantu OK, nebo
     * prnví chybu na kterou se narazí
     */
    public Set<ArgumentError> errors = new HashSet<>();
}
