package persistence;


import wrapper.ProcessingResults;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Errors should be handled via exceptions - checked exceptions
 */
public class FileHandler {

    private FileHandler() {} // never instantiate

    /**
     * File input
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static String loadFromFile(String filePath) throws FileNotFoundException {
        String fileContents;

        Scanner fileScanner = new Scanner(new File(filePath));
        StringBuilder rawInputBuilder = new StringBuilder();
        while (fileScanner.hasNext()) {
            rawInputBuilder.append(fileScanner.nextLine());
        }
        fileScanner.close();
        fileContents = rawInputBuilder.toString();

        return fileContents;
    }

    /**
     * File output (overwriting)
     * Overwrites the files contents if it already exists!
     * @param filePath
     * @param dataToSave
     * @throws IOException
     */
    @SuppressWarnings("ResultOfMethodCallIgnored") // ignore file.createNewFile() output
    public static void overwriteFile(String filePath, String dataToSave) throws IOException {
        File file = new File(filePath);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(dataToSave);
        fileWriter.close();
    }

    // TODO implement methods
    public static boolean fileExists(String filePath) {

        return false;
    }

    public static boolean fileIsEmpty(String filePath) {

        return false;
    }

    public static void createFile(String filePath) {

    }

    public static void appendFile(String filePath, String dataToSave) {

    }

}
