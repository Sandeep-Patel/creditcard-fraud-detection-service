package com.afterpay.transactions.service;

import com.afterpay.transactions.model.CreditCardTransactions;
import com.afterpay.transactions.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<String> processTransactions(final List<String> transactions) throws DateTimeParseException, NumberFormatException {

        Map<String, CreditCardTransactions> transactionMap = new HashMap<>();
        List<String> fraudCreditCards = new ArrayList<>();

        // For each line, parse the transaction and insert it into the map of <creditCard, list of transactions> in
        // such a way that at any time transaction list is not violating the threshold and 24 hours time window
        AtomicLong lineNumber = new AtomicLong(1);
        transactions.forEach(transaction -> {
            try {
                Transaction cardTransaction = new Transaction(transaction);

                CreditCardTransactions creditCardTransactions =
                        transactionMap.getOrDefault(cardTransaction.getCreditCardHash(), new CreditCardTransactions(threshold));

                boolean isFraud = checkTransactionsForCard(creditCardTransactions, cardTransaction);
                if (isFraud) {
                    fraudCreditCards.add(cardTransaction.getCreditCardHash());
                    emitFraudEvent(cardTransaction.getCreditCardHash());
                }
                transactionMap.put(cardTransaction.getCreditCardHash(), creditCardTransactions);
                lineNumber.getAndIncrement();
            } catch (DateTimeParseException | NumberFormatException e) {
                //Log invalid and continue with other lines instead of halting the whole process
                LOGGER.error("Invalid transaction format at line number: " + lineNumber);
            }
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
