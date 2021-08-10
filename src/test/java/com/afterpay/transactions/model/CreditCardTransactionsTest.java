package com.afterpay.transactions.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CreditCardTransactionsTest {

    private CreditCardTransactions creditCardTransactions;

    @Before
    public void setUp() {
        creditCardTransactions = new CreditCardTransactions(new BigDecimal(100));
    }

    @Test
    public void slideWindowFraud() {
        Transaction firstTransaction = new Transaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T07:00:00,100.00");
        creditCardTransactions.slideWindow(firstTransaction);
        assertEquals(1, creditCardTransactions.getTransactions().size());
        assertFalse(creditCardTransactions.isFraud());

        Transaction secondTransaction = new Transaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T11:00:00,100.00");
        creditCardTransactions.slideWindow(secondTransaction);
        assertEquals(2, creditCardTransactions.getTransactions().size());
        assertTrue(creditCardTransactions.isFraud());
    }

    @Test
    public void slideWindowNoFraud() {
        Transaction firstTransaction = new Transaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T07:00:00,100.00");
        creditCardTransactions.slideWindow(firstTransaction);
        assertEquals(1, creditCardTransactions.getTransactions().size());
        assertFalse(creditCardTransactions.isFraud());

        Transaction secondTransaction = new Transaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-03T11:00:00,20.00");
        creditCardTransactions.slideWindow(secondTransaction);
        //Transaction date is changed, so first will be removed.
        assertEquals(1, creditCardTransactions.getTransactions().size());
        assertFalse(creditCardTransactions.isFraud());
    }

    @Test
    public void getTimeDiffInMins() {
        Transaction firstTransaction = new Transaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T07:00:00,100.00");
        Transaction secondTransaction = new Transaction("e7f49f8558b94a1283a7750e4d65859b,2021-08-01T07:14:00,100.00");
        long mins = creditCardTransactions.getTimeDiffInMins(firstTransaction, secondTransaction);
        assertEquals(14L, mins);
    }
}