package com.afterpay.transactions.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

/**
 * @author Sandeep
 * <p>
 * This class is helper utility for file reading
 */
public class FileUtil {

    /**
     * This methods will read file and return transactions rows as list
     */
    public static List<String> readTransactionsFile(String inputPath) throws DateTimeParseException, NumberFormatException {
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(inputPath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("error while reading lines from file " + e);
        }

        return lines;
    }

}
