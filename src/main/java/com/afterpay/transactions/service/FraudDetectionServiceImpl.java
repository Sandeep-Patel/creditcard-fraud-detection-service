package com.afterpay.transactions.service;

import com.afterpay.transactions.model.CreditCardTransactions;
import com.afterpay.transactions.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sandeep
 * <p>
 * This class implements logic for credit card fraud detection using sliding window
 */
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FraudDetectionServiceImpl.class);
    private final BigDecimal threshold;

    public FraudDetectionServiceImpl(BigDecimal threshold) {
        this.threshold = threshold;
    }

    /**
     * This method will process list of transactions
     */
    @Override
    public Set<String> processTransactions(final List<Transaction> transactions) {

        Map<String, CreditCardTransactions> transactionMap = new HashMap<>();
        Set<String> fraudCreditCards = new HashSet<>();

        // For each transaction, insert it into the map of <creditCard, list of transactions> in
        // such a way that at any time transaction list is not violating the threshold and 24 hours time window
        transactions.forEach(transaction -> {
            CreditCardTransactions creditCardTransactions =
                    transactionMap.getOrDefault(transaction.getCreditCardHash(), new CreditCardTransactions(threshold));

            boolean isFraud = checkTransactionsForCard(creditCardTransactions, transaction);
            if (isFraud) {
                fraudCreditCards.add(transaction.getCreditCardHash());
                emitFraudEvent(transaction.getCreditCardHash());
            }
            transactionMap.put(transaction.getCreditCardHash(), creditCardTransactions);
        });

        return fraudCreditCards;
    }

    private boolean checkTransactionsForCard(CreditCardTransactions creditCardTransactions, Transaction transaction) {
        // Already detected as fraud based on transactions so far.
        if (creditCardTransactions.isFraud()) {
            return true;
        }
        // Implement variable length Sliding window
        creditCardTransactions.slideWindow(transaction);

        return creditCardTransactions.isFraud();
    }

    /**
     * Emit event for fraud credit card with credit card info
     *
     * @param hashCardNumber - fraud credit card number
     */
    @Override
    public void emitFraudEvent(String hashCardNumber) {
        //TODO: Event emitter can be implemented here, sor simplicity just printing here.
    }

}
