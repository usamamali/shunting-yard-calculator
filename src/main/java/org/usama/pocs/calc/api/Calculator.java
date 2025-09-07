package org.usama.pocs.calc.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usama.pocs.calc.eval.RpnEvaluator;
import org.usama.pocs.calc.exception.CalcException;
import org.usama.pocs.calc.exception.EvaluationException;
import org.usama.pocs.calc.parse.ExpressionParser;
import org.usama.pocs.calc.parse.ShuntingYardParser;
import org.usama.pocs.calc.lex.Lexer;

/**
 * High-level facade for evaluating arithmetic expressions
 * <p>This calculator performs a three-step pipeline:
 * <ol>
 *     <li><b>Lexing</b> - {@link Lexer} tokenizes the input string into numbers and operators.</li>
 *     <li><b>Parsing</b> - {@link ExpressionParser} converts infix tokens to Reverse Polish Notation (RPN).</li>
 *     <li><b>Evaluation</b>- {@link RpnEvaluator} executes the RPN stack to produce a {@code long} result.</li>
 * </ol>
 *
 * @see Lexer
 * @see ExpressionParser
 * @see ShuntingYardParser
 * @see RpnEvaluator
 */
public class Calculator {

    private final Lexer lexer;
    private final ExpressionParser parser;
    private final RpnEvaluator rpnEvaluator;

    private static final Logger LOG = LogManager.getLogger(Calculator.class);

    /**
     * Creates a calculator with the default pipeline
     */
    public Calculator() {
        this.lexer = new Lexer();
        this.parser = new ShuntingYardParser();
        this.rpnEvaluator = new RpnEvaluator();
    }

    /**
     *
     * @param expression arithmetic expression to evaluate in infix notation (e.g., {@code "3 * -2 + 1"})
     * @return the computed result as a {@code long}
     * @throws CalcException if tokenization, parsing, or evaluation fails
     */
    public long calculate(String expression) throws CalcException {
        try {
            var tokens = lexer.tokenize(expression);
            var rpnTokens = parser.parse(tokens);
            var result = rpnEvaluator.evaluate(rpnTokens);

            LOG.info("calculate('{}') -> {})", expression, result);
            return result;

        } catch (CalcException e) {
            LOG.error("calculate('{}') failed", expression, e);
            throw e;

        } catch (RuntimeException e) {
            LOG.error("calculate('{}') unexpected error", expression, e);
            throw new EvaluationException("Unexpected error during evaluation", e);
        }
    }
}
