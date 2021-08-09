package com.afterpay.transactions;

import com.afterpay.transactions.util.FileUtil;
import com.afterpay.transactions.service.FraudDetectionService;
import com.afterpay.transactions.service.FraudDetectionServiceImpl;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 *
 * @author Sandeep
 */
public class CreditCardFraudDetectorMain {

    public static void main(String[] args) {
        if(args.length != 2) {
            System.err.println("Invalid input");
            System.err.println("Usage:> java -jar <jar_name>.jar <filePath> <threshold>");
            System.exit(1);
        } else {
            try {
                BigDecimal threshold =  new BigDecimal(args[1]);
                FraudDetectionService fraudDetectionService = new FraudDetectionServiceImpl(threshold);

                List<String> transactions = FileUtil.readTransactionsFile(args[0]);
                fraudDetectionService.processTransactions(transactions);
            } catch (DateTimeParseException e) {
                System.err.println("Incorrect datetime format in the sample file");
                System.exit(1);
            } catch (NumberFormatException e) {
                System.err.println("Threshold amount is in incorrect format in the sample file");
                System.exit(1);
            }
        }
    }
    
}
