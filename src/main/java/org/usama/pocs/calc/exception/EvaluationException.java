package org.usama.pocs.calc.exception;

public class EvaluationException extends CalcException{

    public EvaluationException (String message) {
        super(message);
    }

    public EvaluationException (String message, Throwable cause) {
        super(message, cause);
    }

}
