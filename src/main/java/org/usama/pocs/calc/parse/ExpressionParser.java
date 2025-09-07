package org.usama.pocs.calc.parse;

import java.util.List;
import org.usama.pocs.calc.lex.Token;

/**
 * Interface for parsing a list of tokens into a structured format.
 */
public interface ExpressionParser {

    /**
     * Parses a list of tokens and returns a new list of tokens in a structured format.
     *
     * @param tokens the list of tokens to parse
     * @return a new list of tokens in a structured format
     */
    List<Token> parse(List<Token> tokens);

}
