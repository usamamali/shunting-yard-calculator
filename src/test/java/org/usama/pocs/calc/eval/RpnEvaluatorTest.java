package org.usama.pocs.calc.eval;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.usama.pocs.calc.lex.TokenType.NUMBER;
import static org.usama.pocs.calc.testutil.TestTokens.NUM;
import static org.usama.pocs.calc.testutil.TestTokens.OP;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.usama.pocs.calc.exception.EvaluationException;
import org.usama.pocs.calc.lex.Token;

@DisplayName("RpnEvaluator")
class RpnEvaluatorTest {

    private RpnEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new RpnEvaluator();
    }

    @Test
    @DisplayName("evaluate: 1 2 3 * +  -> 7")
    void evaluateSimpleInput() {
        var rpnTokens = List.of(NUM("1"), NUM("2"), NUM("3"), OP("*"), OP("+"));
        var result = evaluator.evaluate(rpnTokens);
        assertEquals(7, result, "RPN evaluation result is incorrect");
    }

    @ParameterizedTest(name = "evaluate: {0} -> {1}")
    @MethodSource("validRpnCases")
    void evaluateVariousInputs(List<Token> rpn, long expected) {
        assertEquals(expected, evaluator.evaluate(rpn));
    }

    static Stream<Object[]> validRpnCases() {
        return Stream.of(
            new Object[]{List.of(NUM("2"), NUM("3"), NUM("4"), OP("*"), OP("+")), 14},    // 2 + 3*4
            new Object[]{List.of(NUM("3"), NUM("-2"), OP("*"), NUM("4"), OP("+")), -2},  // 3 * -2 + 4
            new Object[]{List.of(NUM("10"), NUM("2"), OP("-"), NUM("3"), OP("-")), 5},  // (10-2)-3
            new Object[]{List.of(NUM("8"), NUM("3"), OP("/")), 2},  // truncates toward zero
            new Object[]{List.of(NUM("-8"), NUM("3"), OP("/")), -2}   // negative truncation
        );
    }

    @Test
    @DisplayName("evaluate: divide by zero -> EvaluationException")
    void evaluateDivideByZero() {
        var rpn = List.of(NUM("3"), NUM("0"), OP("/"));
        assertThrows(EvaluationException.class, () -> evaluator.evaluate(rpn));
    }

    @Test
    @DisplayName("evaluate: overflow in addition -> EvaluationException")
    void evaluateOverflowAdd() {
        var rpn = List.of(NUM(String.valueOf(Long.MAX_VALUE)), NUM("1"), OP("+"));
        assertThrows(EvaluationException.class, () -> evaluator.evaluate(rpn));
    }

    @Test
    @DisplayName("evaluate: overflow in multiplication -> EvaluationException")
    void evaluateOverflowMultiplication() {
        var rpn = List.of(NUM(String.valueOf(Long.MAX_VALUE)), NUM("2"), OP("*"));
        assertThrows(EvaluationException.class, () -> evaluator.evaluate(rpn));
    }

    @Test
    @DisplayName("evaluate: not enough operands -> EvaluationException")
    void evaluateOperandUnderflow() {
        var rpn = List.of(NUM("1"), OP("+")); // needs two operands
        assertThrows(EvaluationException.class, () -> evaluator.evaluate(rpn));
    }

    @Test
    @DisplayName("evaluate: invalid number literal -> EvaluationException")
    void evaluateInvalidNumber() {
        var rpn = List.of(new Token(NUMBER, "3.5"), NUM("2"), OP("+"));
        assertThrows(EvaluationException.class, () -> evaluator.evaluate(rpn));
    }

    @Test
    @DisplayName("evaluate: null/empty input -> EvaluationException")
    void evaluateNullOrEmpty() {
        assertThrows(EvaluationException.class, () -> evaluator.evaluate(null));
        assertThrows(EvaluationException.class, () -> evaluator.evaluate(List.of()));
    }

}