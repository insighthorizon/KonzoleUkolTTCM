public enum ArgumentError {
    NO_ARGS("%nError: Nebyl zadán žádný argument."),
    TOO_MANY_ARGS("%nError: Přebytečné argumenty."),
    WRONG_NUMBER("%nError: Pokud je první argument celé číslo, tak musí být kladné."),
    INPUT_PATH_FORMAT("%nError: Cesta vstupního souboru má chybný formát."),
    OUTPUT_PATH_FORMAT("%nError: Cesta výstupního souboru má chybný formát.");

    private final String errorMessage;

    ArgumentError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}


