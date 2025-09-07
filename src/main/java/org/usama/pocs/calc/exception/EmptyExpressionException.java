package org.usama.pocs.calc.exception;

public class EmptyExpressionException extends SyntaxException{

    public EmptyExpressionException() {
        super("Expression cannot be null or empty");
    }
}
