package com.afterpay.transactions.service;

import java.time.format.DateTimeParseException;
import java.util.List;

/**
 *
 * @author Sandeep
 * 
 * This class defines interface for credit card fraud detection using sliding window
 */
public interface FraudDetectionService {

    List<String> processTransactions(List<String> transactions) throws DateTimeParseException, NumberFormatException;

    void emitFraudEvent(String hashCardNumber);

}
