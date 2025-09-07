package org.usama.pocs.calc.exception;

public class CalcException extends RuntimeException {

    public CalcException(String message) {
        super(message);
    }

    public CalcException (String message, Throwable cause) {
        super(message, cause);
    }
}
