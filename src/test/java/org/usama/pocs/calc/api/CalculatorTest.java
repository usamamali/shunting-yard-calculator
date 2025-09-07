package org.usama.pocs.calc.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.usama.pocs.calc.exception.EvaluationException;

@DisplayName("Calculator")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @ParameterizedTest(name = "[{index}] calculate(\"{0}\") -> {1}")
    @CsvSource(
        value = {
            "1 + 2 = 3",
            "1 - 2 = -1",
            "2 * 3 = 6",
            "4 + -3 = 1",
            "8 / 4 = 2",
            "3 * 2 + 1 = 7",
            "2 * -2 + 1 = -3",
            "2 * 4 * 6 - 1 = 47",
            "10 + 2 * 6 = 22",
            "100 * 2 - 12 / 3 = 196"
        },
        delimiter = '='
    )
    void calculateVariousExpressions(String expression, long expectedResult) {
        var result = calculator.calculate(expression);
        assertEquals(expectedResult, result, "Calculation result does not match expected result");
    }

    @Test
    @DisplayName("calculate: divide by zero -> EvaluationException")
    void calculateDivideByZero() {
        assertThrows(
            EvaluationException.class,
            () -> calculator.calculate("1 + 9 / 0")
            , "Expected ArithmeticException for division by zero");
    }
}