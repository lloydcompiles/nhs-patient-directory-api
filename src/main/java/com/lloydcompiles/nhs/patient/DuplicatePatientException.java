package com.lloydcompiles.nhs.patient;

/**
 * Custom exception thrown when NHS Number value is duplicated.
 */
public class DuplicatePatientException extends Exception {
    public DuplicatePatientException(String message) {
        super(message);
    }
}
