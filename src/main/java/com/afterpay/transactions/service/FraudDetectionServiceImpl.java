package com.afterpay.transactions.service;

import com.afterpay.transactions.model.Transaction;
import com.afterpay.transactions.model.CreditCardTransactions;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sandeep
 * <p>
 * This class implements logic for credit card fraud detection using sliding window
 */
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final BigDecimal threshold;

    public FraudDetectionServiceImpl(BigDecimal threshold) {
        this.threshold = threshold;
    }

    /**
     * This methods will read & parse given file
     */
    @Override
    public void processTransactions(List<String> transactions) throws DateTimeParseException, NumberFormatException {

        Map<String, CreditCardTransactions> transactionMap = new HashMap<>();

        // For each line, parse the transaction and insert it into the map of <creditCard, list of transactions> in
        // such a way that at any time transaction list is not violating the threshold and 24 hours time window
        transactions.forEach(transaction -> {
            Transaction cardTransaction = new Transaction(transaction);

            CreditCardTransactions creditCardTransactions =
                    transactionMap.getOrDefault(cardTransaction.getCreditCardHash(), new CreditCardTransactions());

            boolean isFraud = checkTransactionsForCard(creditCardTransactions, cardTransaction);
            if (isFraud) {
                emitFraudEvent(cardTransaction.getCreditCardHash());
            }
            transactionMap.put(cardTransaction.getCreditCardHash(), creditCardTransactions);
        });
    }

    private boolean checkTransactionsForCard(CreditCardTransactions creditCardTransactions, Transaction transaction) {
        // Already detected as fraud based on transactions so far.
        if (creditCardTransactions.isFraud()) {
            return true;
        }
        // Implement variable length Sliding window
        creditCardTransactions.slideWindow(transaction, threshold);

        return creditCardTransactions.isFraud();
    }

    /**
     * Emit event for fraud credit card with credit card info
     *
     * @param hashCardNumber - fraud credit card number
     */
    @Override
    public void emitFraudEvent(String hashCardNumber) {
        //TODO: emit event
    }

}
