package org.usama.pocs.calc.exception;

import org.usama.pocs.calc.lex.TokenType;

public class UnexpectedTokenException extends SyntaxException {

    public UnexpectedTokenException(String message) {
        super(message);
    }

    public UnexpectedTokenException(String token, TokenType expectedType) {
        super(
            String.format(
                "Unexpected: %s for type: %s",
                token,
                expectedType
            )
        );
    }
}
