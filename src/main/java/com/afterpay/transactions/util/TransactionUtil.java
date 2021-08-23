package com.afterpay.transactions.util;

import com.afterpay.transactions.model.Transaction;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class TransactionUtil {

    private static final Logger LOGGER = Logger.getLogger(TransactionUtil.class);

    public static Optional<Transaction> getTransaction(String line) throws DateTimeParseException, NumberFormatException {
        try {
            String[] transaction = line.replaceAll("\\s+", "").split(",");
            String cardNumber = transaction[0];
            LocalDateTime transactionDate = LocalDateTime.parse(transaction[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            BigDecimal amount = new BigDecimal(transaction[2]);
            return Optional.of(new Transaction(cardNumber, transactionDate, amount));
        } catch (DateTimeParseException | NumberFormatException e) {
            //Log invalid and continue with other lines instead of halting the whole process
            LOGGER.error("Invalid transaction format: " + line);
        }
        return Optional.empty();
    }
}
