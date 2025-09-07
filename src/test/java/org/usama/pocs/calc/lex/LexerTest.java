package org.usama.pocs.calc.lex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.usama.pocs.calc.testutil.TestTokens.NUM;
import static org.usama.pocs.calc.testutil.TestTokens.OP;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.usama.pocs.calc.exception.EmptyExpressionException;
import org.usama.pocs.calc.exception.UnexpectedTokenException;
import org.usama.pocs.calc.exception.UnrecognizedTokenException;

@DisplayName("Lexer")
class LexerTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        lexer = new Lexer();
    }

    @Test
    @DisplayName("tokenize: basic expression")
    void tokenizeBasicExpression() {
        var tokens = lexer.tokenize("1 + 2 - 3 * 4 / 5");
        var expectedTokens = List.of(
            NUM("1"),
            OP("+"),
            NUM("2"),
            OP("-"),
            NUM("3"),
            OP("*"),
            NUM("4"),
            OP("/"),
            NUM("5")
        );

        assertEquals(expectedTokens, tokens, "Tokens do not match expected tokens");
    }

    @Test
    @DisplayName("tokenize: signed numbers allowed where a number is expected")
    void tokenizeSignedInteger() {
        var tokens = lexer.tokenize("1 + 2 - 3 * -4 / 5");
        var expectedTokens = List.of(
            NUM("1"),
            OP("+"),
            NUM("2"),
            OP("-"),
            NUM("3"),
            OP("*"),
            NUM("-4"),
            OP("/"),
            NUM("5")
        );

        assertEquals(expectedTokens, tokens, "Tokens do not match expected tokens");
    }

    @Test
    @DisplayName("tokenize: handles extra whitespace")
    void tokenizeExtraSpaces() {
        var tokens = lexer.tokenize("  1   + 2 -  3 * -4 / 5  ");
        var expectedTokens = List.of(
            NUM("1"),
            OP("+"),
            NUM("2"),
            OP("-"),
            NUM("3"),
            OP("*"),
            NUM("-4"),
            OP("/"),
            NUM("5")
        );

        assertEquals(expectedTokens, tokens, "Tokens do not match expected tokens");
    }

    @Test
    @DisplayName("tokenize: empty or null expression is an error")
    void tokenizeEmptyExpression() {
        assertThrows(
            EmptyExpressionException.class,
            () -> lexer.tokenize(""),
            "Empty expression should fail");

        assertThrows(
            EmptyExpressionException.class,
            () -> lexer.tokenize(null),
            "Null expression should fail");
    }

    @ParameterizedTest(name = "tokenize: invalid -> {1}")
    @MethodSource("invalidExpressions")
    void tokenizeInvalidInputs(Class<? extends RuntimeException> expected, String expr, String why) {
        assertThrows(expected, () -> lexer.tokenize(expr), why);
    }

    static Stream<Object[]> invalidExpressions() {
        return Stream.of(
            // wrong numeric format (doubles not allowed)
            new Object[]{UnrecognizedTokenException.class, "1 + 2 - 3.5", "Doubles are not accepted"},
            // starts with operator
            new Object[]{UnexpectedTokenException.class, "+ 2 - 3", "Expression cannot start with an operator"},
            // non-integer token
            new Object[]{UnrecognizedTokenException.class, "x + 2 - 3", "Non-integer token should fail"},
            // ends with operator
            new Object[]{UnexpectedTokenException.class, "1 + 2 -", "Expression cannot end with operator"},
            // consecutive numbers (missing operator)
            new Object[]{UnexpectedTokenException.class, "1 2 + 3", "Two numbers in a row should fail"},
            // consecutive operators
            new Object[]{UnexpectedTokenException.class, "1 + * 2", "Two operators in a row should fail"},
            // unknown operator
            new Object[]{UnrecognizedTokenException.class, "1 % 2", "Unsupported operator should fail"},
            // no whitespace between tokens
            new Object[]{UnrecognizedTokenException.class, "1+2", "No whitespace between tokens should fail"},
            // malformed signed number
            new Object[]{UnrecognizedTokenException.class, "1 + 2 -+3", "Bad sign sequence should fail"}
        );
    }
}