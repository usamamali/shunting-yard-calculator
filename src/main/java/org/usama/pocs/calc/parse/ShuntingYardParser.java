package org.usama.pocs.calc.parse;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.usama.pocs.calc.exception.InvalidTokenException;
import org.usama.pocs.calc.exception.UnexpectedTokenException;
import org.usama.pocs.calc.lex.Token;
import org.usama.pocs.calc.lex.TokenType;

/**
 * Implementation of the Shunting Yard algorithm to parse infix expressions into Reverse Polish Notation (RPN).
 */
public final class ShuntingYardParser implements ExpressionParser {

    @Override
    public List<Token> parse(List<Token> tokens) {
        List<Token> rpnTokens = new ArrayList<>(tokens.size());

        Deque<Token> operators = new ArrayDeque<>();
        for (Token token : tokens) {
            switch (token.type()) {
                case NUMBER -> rpnTokens.add(token);
                case OPERATOR -> {
                    while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)
                            && !isRightAssociativeOperator(token)
                    ) {
                        rpnTokens.add(operators.pop());
                    }
                    operators.push(token);
                }
                default -> throw new InvalidTokenException(token.value(), token.type());
            }
        }

        while (!operators.isEmpty()) {
            rpnTokens.add(operators.pop());
        }

        return rpnTokens;
    }

    /**
     * Get the precedence of an operator token.
     *
     * @param operator the operator token
     * @return precedence level (higher means higher precedence)
     * @throws UnexpectedTokenException if the token is not an operator
     */
    private static int precedence(Token operator) {
        if (operator.type() != TokenType.OPERATOR) {
            throw new UnexpectedTokenException("Token is not an operator: " + operator);
        }

        return switch (operator.value()) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> throw new UnexpectedTokenException("Unknown operator: " + operator);
        };
    }

    private static boolean isRightAssociativeOperator(Token operator) {
        if (operator.type() != TokenType.OPERATOR) {
            throw new UnexpectedTokenException("Token is not an operator: " + operator);
        }

        return "^".equals(operator.value());
    }
}
