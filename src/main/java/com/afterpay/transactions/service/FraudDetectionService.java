package com.afterpay.transactions.service;

import com.afterpay.transactions.model.Transaction;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

/**
 * @author Sandeep
 * <p>
 * This class defines interface for credit card fraud detection using sliding window
 */
public interface FraudDetectionService {

    Set<String> processTransactions(List<Transaction> transactions);

    void emitFraudEvent(String hashCardNumber);

}
