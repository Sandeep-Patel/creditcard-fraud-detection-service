package com.afterpay.transactions.model;

import com.afterpay.transactions.util.TransactionUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

public class CreditCardTransactionsTest {

    private CreditCardTransactions creditCardTransactions;

    @Before
    public void setUp() {
        creditCardTransactions = new CreditCardTransactions(new BigDecimal(100));
    }

    @Test
    public void slideWindowFraud() {
        Optional<Transaction> firstTransaction = TransactionUtil.getTransaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T07:00:00,100.00");
        firstTransaction.ifPresent(creditCardTransactions::slideWindow);
        assertEquals(1, creditCardTransactions.getTransactions().size());
        assertFalse(creditCardTransactions.isFraud());

        Optional<Transaction> secondTransaction = TransactionUtil.getTransaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T11:00:00,100.00");
        secondTransaction.ifPresent(creditCardTransactions::slideWindow);
        assertEquals(2, creditCardTransactions.getTransactions().size());
        assertTrue(creditCardTransactions.isFraud());
    }

    @Test
    public void slideWindowNoFraud() {
        Optional<Transaction> firstTransaction = TransactionUtil.getTransaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T07:00:00,100.00");
        firstTransaction.ifPresent(creditCardTransactions::slideWindow);
        assertEquals(1, creditCardTransactions.getTransactions().size());
        assertFalse(creditCardTransactions.isFraud());

        Optional<Transaction> secondTransaction = TransactionUtil.getTransaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-03T11:00:00,20.00");
        secondTransaction.ifPresent(creditCardTransactions::slideWindow);
        //Transaction date is changed, so first will be removed.
        assertEquals(1, creditCardTransactions.getTransactions().size());
        assertFalse(creditCardTransactions.isFraud());
    }

    @Test
    public void getTimeDiffInMins() {
        Optional<Transaction> firstTransaction = TransactionUtil.getTransaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T07:00:00,100.00");
        Optional<Transaction> secondTransaction = TransactionUtil.getTransaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T07:14:00,100.00");
        long mins = creditCardTransactions.getTimeDiffInMins(firstTransaction.get(), secondTransaction.get());
        assertEquals(14L, mins);
    }
}