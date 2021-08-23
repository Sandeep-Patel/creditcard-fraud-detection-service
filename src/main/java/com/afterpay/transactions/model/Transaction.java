
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
