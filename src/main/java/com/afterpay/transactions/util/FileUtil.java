package com.afterpay.transactions.util;

import com.afterpay.transactions.model.Transaction;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sandeep
 * <p>
 * This class is helper utility for file reading
 */
public class FileUtil {
    private static final Logger LOGGER = Logger.getLogger(FileUtil.class);

    /**
     * This methods will read file and return transactions rows as list
     */
    public static List<Transaction> readTransactionsFile(String inputPath) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputPath), StandardCharsets.UTF_8);
            lines.forEach(l -> TransactionUtil.getTransaction(l).ifPresent(transactions::add));
        } catch (IOException e) {
            LOGGER.error("error while reading lines from file " + e);
        }
        return transactions;
    }

}
