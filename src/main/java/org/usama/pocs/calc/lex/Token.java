package org.usama.pocs.calc.lex;

/**
 * A Token represents a lexical token with a type and a value.
 *
 * @param type  the type of the token (e.g., NUMBER, OPERATOR)
 * @param value the string value of the token
 */
public record Token(TokenType type, String value) {

}
