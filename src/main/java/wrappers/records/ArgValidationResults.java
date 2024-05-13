package wrappers.records;

import wrappers.immutable_collections.ROArgumentErrors;

/**
 * Just a wrapper class
 *
 * @param errors Zde si přidělávám práci tím, že neukončím program když narazím na první chybu v uživatelských argumentech,
 *               ale snažím se vyhodnotit i případně víc chyb najednou. Například:
 *               Uživatel zadal nesprávný formát argumentu a ještě nesprávný počet argumentů
 *               ...
 *               Kdybychom si chtěli ušetřit práci, tak tu bude jen enum atribut, který bude obsahovat kosntantu OK, nebo
 *               prnví chybu na kterou se narazí
 */
public record ArgValidationResults(String inputFilePath,
                                   String outputFilePath,
                                   ROArgumentErrors errors) {
}
