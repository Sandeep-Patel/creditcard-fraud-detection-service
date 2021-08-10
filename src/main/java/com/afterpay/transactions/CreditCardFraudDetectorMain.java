package com.afterpay.transactions;

import com.afterpay.transactions.exception.InvalidUsageException;
import com.afterpay.transactions.service.FraudDetectionService;
import com.afterpay.transactions.service.FraudDetectionServiceImpl;
import com.afterpay.transactions.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Sandeep
 */
public class CreditCardFraudDetectorMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditCardFraudDetectorMain.class);

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new InvalidUsageException("Invalid usage: \nUsage:> java -jar <jar_name>.jar <filePath> <threshold>");
        } else {
            BigDecimal threshold = new BigDecimal(args[1]);
            FraudDetectionService fraudDetectionService = new FraudDetectionServiceImpl(threshold);

            List<String> transactions = FileUtil.readTransactionsFile(args[0]);
            List<String> fraudCreditCards = fraudDetectionService.processTransactions(transactions);

            LOGGER.error("Potential fraud credit card list:");
            fraudCreditCards.forEach(LOGGER::error);
        }
    }

}
