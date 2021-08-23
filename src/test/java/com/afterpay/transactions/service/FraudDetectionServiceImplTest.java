package com.afterpay.transactions.service;

import com.afterpay.transactions.model.Transaction;
import com.afterpay.transactions.util.TransactionUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class FraudDetectionServiceImplTest {

    private FraudDetectionService fraudDetectionService;

    @Before
    public void setUp() {
        fraudDetectionService = new FraudDetectionServiceImpl(new BigDecimal(150));
    }

    @Test
    public void testTransactionsNoThresholdReach() {
        // All credit card numbers are unique, so not reaching threshold
        List<Transaction> transactions = new ArrayList<>(5);
        TransactionUtil.getTransaction("b0229db7f3794b85815da43a848c334a,2021-08-01T01:59:31,100").ifPresent(transactions::add);
        TransactionUtil.getTransaction("69eeb2d4afa04a1ca0e9ef7517aebfc6,2021-08-01T05:00:01,20").ifPresent(transactions::add);
        TransactionUtil.getTransaction("96659f7b1e0448b2ae232c8c65dd6ce7,2021-08-02T07:04:47,30").ifPresent(transactions::add);
        TransactionUtil.getTransaction("7fe394d1435342d6a08baddb795e9259,2021-08-02T10:03:12,40").ifPresent(transactions::add);
        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-03T10:31:49,50").ifPresent(transactions::add);

        Set<String> fraudCC = fraudDetectionService.processTransactions(transactions);
        assertEquals(0, fraudCC.size());
    }

    @Test
    public void testTransactionsThresholdReach() {
        // All credit card numbers are same, so reaching threshold in 24 hours
        List<Transaction> transactions = new ArrayList<>(5);

        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-03T10:31:49,50").ifPresent(transactions::add);
        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-03T11:31:49,20").ifPresent(transactions::add);
        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-03T12:31:49,30").ifPresent(transactions::add);
        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-03T13:31:49,35").ifPresent(transactions::add);
        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-03T13:35:49,50").ifPresent(transactions::add);

        Set<String> fraudCC = fraudDetectionService.processTransactions(transactions);
        assertEquals(1, fraudCC.size());
        assertEquals("c6c2ed18c1db490fa7d7e71125a4c168", fraudCC.stream().findFirst().get());
    }


    @Test
    public void testTransactionsNoThresholdReachDiffDate() {
        // All credit card numbers are same but date is different so that not reaching threshold in 24 hours
        List<Transaction> transactions = new ArrayList<>(5);

        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-03T10:31:49,50").ifPresent(transactions::add);
        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-04T11:31:49,20").ifPresent(transactions::add);
        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-05T12:31:49,30").ifPresent(transactions::add);
        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-06T13:31:49,35").ifPresent(transactions::add);
        TransactionUtil.getTransaction("c6c2ed18c1db490fa7d7e71125a4c168,2021-08-07T13:35:49,50").ifPresent(transactions::add);

        Set<String> fraudCC = fraudDetectionService.processTransactions(transactions);
        assertEquals(0, fraudCC.size());
    }
}