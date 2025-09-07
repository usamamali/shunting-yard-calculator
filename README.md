# Calculator

This is a simple calculator CLI that can evaluate arithmetic expressions with basic arithmetic operations such as addition, subtraction, multiplication, and division like:

```bash
java -jar target/calculator-1.0.0.jar "3 + 5 * 2 - 8 / 4"

# answer: 11
```

## Algorithms & Mechanism

- **Lexing** (`Lexer`): splits on whitespaces and identifies tokens. Tokens can be either `NUMBER` (signed integer like `-2`) or `OPERATOR` (`+ - * /`).
- **Parsing (Shunting-Yard)**: uses [Shunting Yard Algorithm](https://en.wikipedia.org/wiki/Shunting_yard_algorithm) to convert infix expressions to postfix expressions (Reverse Polish Notation).
- **Evaluation (RPN Stack)**: evaluates the postfix expression using a stack-based approach.

## Build & Test (Maven)

```bash
# run tests
mvn clean test

# package as a runnable uber-JAR (recommended)
mvn -DskipTests package
# produces: target/calculator-<version>.jar
```

## Run

> **Tip**: use quotes to avoid shell interpretation of special characters like `*` and `-`

```bashbash
# run with an expression argument
java -jar target/calculator-<version>.jar "3 + 5 * 2 - 8 / 4"
```

## Examples

```bash
java -jar target/calculator-1.0.0.jar "2 + 3 * 4"          # answer: 14
java -jar target/calculator-1.0.0.jar "10 - 2 - 3"         # answer: 5
java -jar target/calculator-1.0.0.jar "8 / 3"              # answer: 2
java -jar target/calculator-1.0.0.jar "4 * -2 + 8 / 3"     # answer: -6
java -jar target/calculator-1.0.0.jar "-2 * 4 / 0"         # calculate error: Arithmetic error: / by zero
```
