package org.usama.pocs.calc.exception;

import org.usama.pocs.calc.lex.TokenType;

public class InvalidTokenException extends LexicalException{

    public InvalidTokenException(String token, TokenType tokenType) {
        super(
            String.format(
                "Invalid token: %s of type: %s",
                token,
                tokenType
            )
        );
    }
}
