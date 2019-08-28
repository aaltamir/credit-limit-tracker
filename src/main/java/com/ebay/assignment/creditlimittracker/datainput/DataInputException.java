package com.ebay.assignment.creditlimittracker.datainput;

/**
 * Exception used when there are problems Reading and processing the input data
 */
public class DataInputException extends RuntimeException {

    public DataInputException(String message) {
        super(message);
    }

    public DataInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
