package com.afterpay.transactions.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidUsageException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidUsageException.class);

    public InvalidUsageException(String message) {
        super(message);
        LOGGER.error(message);
        System.exit(1);
    }
}
