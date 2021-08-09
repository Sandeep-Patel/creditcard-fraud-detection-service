
package com.afterpay.transactions.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Sandeep
 * <p>
 * This class represents a single transaction in file
 */
public class Transaction {

    private String creditCardHash;
    private LocalDateTime timestamp;
    private BigDecimal transactionAmount;

    public Transaction(String creditCardHash, LocalDateTime timestamp, BigDecimal transactionAmount) {
        this.creditCardHash = creditCardHash;
        this.timestamp = timestamp;
        this.transactionAmount = transactionAmount;
    }

    public Transaction(String line) {
        String[] transaction = line.replaceAll("\\s+", "").split(",");
        String cardNumber = transaction[0];
        LocalDateTime transactionDate = LocalDateTime.parse(transaction[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        BigDecimal amount = new BigDecimal(transaction[2]);

        this.creditCardHash = cardNumber;
        this.timestamp = transactionDate;
        this.transactionAmount = amount;
    }

    public String getCreditCardHash() {
        return creditCardHash;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

}
