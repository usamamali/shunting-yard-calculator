package org.usama.pocs.calc.testutil;

import static org.usama.pocs.calc.lex.TokenType.NUMBER;

import org.usama.pocs.calc.lex.Token;
import org.usama.pocs.calc.lex.TokenType;

public class TestTokens {

    private TestTokens() {
    }

    public static Token NUM(String value) {
        return new Token(NUMBER, value);
    }

    public static Token OP(String value) {
        return new Token(TokenType.OPERATOR, value);
    }

}
