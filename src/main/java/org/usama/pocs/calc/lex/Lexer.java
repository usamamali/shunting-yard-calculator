package org.usama.pocs.calc.lex;

import static org.usama.pocs.calc.lex.TokenType.NUMBER;
import static org.usama.pocs.calc.lex.TokenType.OPERATOR;

import java.util.ArrayList;
import java.util.List;
import org.usama.pocs.calc.exception.EmptyExpressionException;
import org.usama.pocs.calc.exception.UnexpectedTokenException;
import org.usama.pocs.calc.exception.UnrecognizedTokenException;

/**
 * Splits a whitespace-separated infix expression into {@link Token}s.
 * <p>Supported: signed integer literals (e.g., {@code -2}, {@code +7}) and
 * binary operators {@code + - * /}. The lexer enforces the alternating pattern NUMBER → OPERATOR → NUMBER … and treats
 * leading {@code +}/{@code -} as part of a number token.</p>
 */
public final class Lexer {

    /**
     * Tokenizes the given input string into NUMBER and OPERATOR tokens.
     *
     * @param input expression like {@code "1 + 2 - 3 * 4 / 5"}
     * @return tokens in left-to-right order
     * @throws EmptyExpressionException   if the input is null or empty
     * @throws UnrecognizedTokenException if an unrecognized token is encountered
     * @throws UnexpectedTokenException   if the token sequence is invalid (e.g., two consecutive operators)
     */
    public List<Token> tokenize(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new EmptyExpressionException();
        }

        var parts = input.trim().split("\\s+");
        List<Token> tokens = new ArrayList<>();

        var expectedTokenType = TokenType.NUMBER;

        for (String part : parts) {
            if (isSignedInteger(part)) {
                if (expectedTokenType != TokenType.NUMBER) {
                    throw new UnexpectedTokenException(part, expectedTokenType);
                }
                tokens.add(new Token(TokenType.NUMBER, part));
                expectedTokenType = OPERATOR;

            } else if (isOperator(part)) {
                if (expectedTokenType != OPERATOR) {
                    throw new UnexpectedTokenException(part, expectedTokenType);
                }
                tokens.add(new Token(OPERATOR, part));
                expectedTokenType = TokenType.NUMBER;

            } else {
                throw new UnrecognizedTokenException(part, expectedTokenType);
            }
        }

        // Expression cannot end with an operator
        if (expectedTokenType == NUMBER && !tokens.isEmpty()) {
            throw new UnexpectedTokenException("Expression cannot end with an operator");
        }

        return tokens;
    }

    /**
     * Checks if the part is one of the supported operators.
     *
     * @param part the string to check
     * @return true if the part is an operator, false otherwise
     */
    private boolean isOperator(String part) {
        return part.length() == 1
            && "+-*/".contains(part);
    }

    /**
     * Checks if the part is a signed integer (e.g., {@code -2}, {@code +7}, {@code 42}).
     *
     * @param part the string to check
     * @return true if the part is a signed integer, false otherwise
     */
    private boolean isSignedInteger(String part) {
        return part.matches("[+-]?\\d+");
    }
}
