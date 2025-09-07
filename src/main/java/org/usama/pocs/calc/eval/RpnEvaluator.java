package org.usama.pocs.calc.eval;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usama.pocs.calc.exception.EvaluationException;
import org.usama.pocs.calc.exception.InvalidTokenException;
import org.usama.pocs.calc.lex.Token;
import org.usama.pocs.calc.lex.TokenType;

/**
 * Evaluates expressions in Reverse Polish Notation (postfix).
 * <p>Supports {@code + - * /} on {@code long}.</p>
 */
public class RpnEvaluator {

    private static final Logger LOG = LogManager.getLogger(RpnEvaluator.class);

    public RpnEvaluator() {
        // no-op
    }

    /**
     * Evaluate an RPN token list.
     *
     * @param rpnTokens tokens in postfix order (numbers and operators)
     * @return result as {@code long}
     * @throws EvaluationException   if the sequence is empty, malformed (too few/many operands), a number is invalid,
     *                               or an arithmetic fault occurs
     * @throws InvalidTokenException if an unknown operator is encountered
     */
    public long evaluate(List<Token> rpnTokens) {
        if (rpnTokens == null || rpnTokens.isEmpty()) {
            throw new EvaluationException("Empty RPN sequence.");
        }

        Deque<Long> numberStack = new ArrayDeque<>();

        for (Token token : rpnTokens) {
            switch (token.type()) {
                case NUMBER -> numberStack.push(parseLongOrFail(token.value()));
                case OPERATOR -> {
                    var rightNumber = popOrFail(numberStack, token.value());
                    var leftNumber = popOrFail(numberStack, token.value());
                    try {
                        long res = applyOperator(leftNumber, rightNumber, token.value());
                        numberStack.push(res);

                    } catch (ArithmeticException e) {
                        LOG.error("Arithmetic error at op '{}' with left={} right={}",
                            token.value(), leftNumber, rightNumber, e);
                        throw new EvaluationException("Arithmetic error: " + e.getMessage(), e);
                    }
                }
                default -> throw new InvalidTokenException(token.value(), token.type());
            }
        }
        return numberStack.pop();
    }

    /**
     * Apply the operator on the two operands.
     *
     * @param left     left operand
     * @param right    right operand
     * @param operator the operator as string
     * @return the result of the operation as {@code long}
     * @throws InvalidTokenException if the operator is unknown
     */
    private static long applyOperator(long left, long right, String operator) {
        return switch (operator) {
            case "+" -> Math.addExact(left, right);
            case "-" -> Math.subtractExact(left, right);
            case "*" -> Math.multiplyExact(left, right);
            case "/" -> left / right;
            default -> throw new InvalidTokenException(operator, TokenType.OPERATOR);
        };
    }

    /**
     * Pop a value from the stack or throw an exception if the stack is empty.
     *
     * @param stack   the stack to pop from
     * @param context context information for error messages
     * @return the popped value
     * @throws EvaluationException if the stack is empty
     */
    private static long popOrFail(Deque<Long> stack, String context) {
        Long v = stack.pollFirst();
        if (v == null) {
            throw new EvaluationException("Not enough operands for operator: " + context);
        }
        return v;
    }

    /**
     * Parse a token as long or throw an exception if parsing fails.
     *
     * @param value the value to parse
     * @return the parsed long value
     * @throws EvaluationException if parsing fails
     */
    private static long parseLongOrFail(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new EvaluationException("Invalid number literal: " + value, ex);
        }
    }
}
