
package com.afterpay.transactions.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sandeep
 * 
 * This class represents a single transaction in file
 */
public class CreditCardTransactions {

    //TODO: This 24 hours should be configurable.
    public static final long TIME_WINDOW = 24 * 60;

    private BigDecimal dailyTotal = BigDecimal.ZERO;
    private boolean fraud;
    private final List<Transaction> transactions = new ArrayList<>();

    public boolean isFraud() {
        return fraud;
    }

    public void markCardAsFraud() {
        fraud = true;
    }

    public boolean isBreachesThreshold(BigDecimal threshold) {
        return dailyTotal.compareTo(threshold) > 0;
    }

    /**
     * This method implements variable length sliding window based on transaction date in last 24 hours
     * If the time difference between first transaction in list and current transaction is >24 hours then slide
     * the window right by removing transactions from the list until the time difference between first transaction
     * in list and current transaction is <= 24. Add the new transaction in list and check for fraud
     *
     * @param transaction - current transaction
     */
    public void slideWindow(Transaction transaction, BigDecimal threshold) {

        // slide window to right until new transaction fits in 24 hours window
        while (!transactions.isEmpty() && getTimeDiffInHours(transactions.get(0), transaction) > TIME_WINDOW) {
            removeFirstTransaction();
        }
        addTransaction(transaction);

        if (isBreachesThreshold(threshold)) {
            System.out.println("Fraud Creditcard alert!!! " + transaction.getCreditCardHash());
            markCardAsFraud();
        }
    }

    private long getTimeDiffInHours(Transaction firstTransaction, Transaction lastTransaction) {
        LocalDateTime fromDateTime = lastTransaction.getTimestamp();
        LocalDateTime toDateTime = firstTransaction.getTimestamp();
        return LocalDateTime.from(toDateTime).until(fromDateTime, ChronoUnit.MINUTES);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        dailyTotal = dailyTotal.add(transaction.getTransactionAmount());
    }

    public void removeFirstTransaction() {
        dailyTotal = dailyTotal.subtract(transactions.get(0).getTransactionAmount());
        transactions.remove(0);
    }
}
