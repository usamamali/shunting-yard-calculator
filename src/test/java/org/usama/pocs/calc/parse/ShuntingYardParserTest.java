package org.usama.pocs.calc.parse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.usama.pocs.calc.testutil.TestTokens.NUM;
import static org.usama.pocs.calc.testutil.TestTokens.OP;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ShuntingYardParser")
class ShuntingYardParserTest {

    private ExpressionParser parser;

    @BeforeEach
    void setUp() {
        parser = new ShuntingYardParser();
    }

    @Test
    @DisplayName("*/ has higher precedence than +")
    void parsePrecedenceInOrder() {
        var tokens = parser.parse(
            List.of(NUM("1"), OP("*"), NUM("2"), OP("+"), NUM("3"))
        );

        var expectedTokens = List.of(NUM("1"), NUM("2"), OP("*"), NUM("3"), OP("+"));

        assertEquals(expectedTokens, tokens, "RPN Tokens do not match expected tokens");
    }

    @Test
    @DisplayName("*/^ has higher precedence than +")
    void parsePrecedenceInOrderPower() {
        var tokens = parser.parse(
                List.of(NUM("2"), OP("^"), NUM("3"), OP("^"), NUM("2"))
        );

        var expectedTokens = List.of(NUM("2"), NUM("3"), NUM("2"), OP("^"), OP("^"));

        assertEquals(expectedTokens, tokens, "RPN Tokens do not match expected tokens");
    }

    @Test
    @DisplayName("+ before * defers to higher precedence")
    void parsePrecedenceReversed() {
        var tokens = parser.parse(
            List.of(NUM("1"), OP("+"), NUM("2"), OP("*"), NUM("-3"))
        );

        var expectedTokens = List.of(NUM("1"), NUM("2"), NUM("-3"), OP("*"), OP("+"));

        assertEquals(expectedTokens, tokens, "RPN Tokens do not match expected tokens");
    }

    @Test
    @DisplayName("same-precedence ops (+/-) are left-associative")
    void parseSamePrecedence() {
        var tokens = parser.parse(
            List.of(NUM("1"), OP("+"), NUM("2"), OP("-"), NUM("3"))
        );

        var expectedTokens = List.of(NUM("1"), NUM("2"), OP("+"), NUM("3"), OP("-"));

        assertEquals(expectedTokens, tokens, "RPN Tokens do not match expected tokens");
    }
}