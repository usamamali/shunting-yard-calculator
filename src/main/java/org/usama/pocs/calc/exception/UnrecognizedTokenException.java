package org.usama.pocs.calc.exception;

import org.usama.pocs.calc.lex.TokenType;

public class UnrecognizedTokenException extends LexicalException {

    public UnrecognizedTokenException(String token, TokenType expectedType) {
        super(
            String.format(
                "Unrecognized token: %s. %s token was expected",
                token,
                expectedType)
        );
    }
}
