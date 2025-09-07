package org.usama.pocs.calc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usama.pocs.calc.api.Calculator;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    /**
     * Print CLI usage examples to stderr.
     */
    private static void usage() {
        System.err.println("usage: java -jar calculator.jar <expression>");
        System.err.println("example: java -jar calculator.jar \"3 * -2 + 4\"");
        System.err.println("note: quote expressions to avoid shell globbing of '*'.");
    }

    public static void main(String[] args) {
        if (args.length == 0 || isHelp(args[0])) {
            usage();
            System.exit(args.length == 0 ? 1 : 0);
        }

        final String expr = args[0].trim();
        if (expr.isEmpty()) {
            System.err.println("error: empty expression");
            System.exit(2);
        }

        var calculator = new Calculator();

        try {
            var result = calculator.calculate(expr);
            LOG.debug("calculate('{}') -> {}", expr, result);
            System.out.println("answer: " + result);
            System.exit(0);
        } catch (Exception e) {
            LOG.error("calculate('{}') failed: {}", expr, e.getMessage());
            System.err.println("calculate error: " + e.getMessage());
            System.exit(2);
        }
    }

    /**
     * Returns whether the given CLI argument requests help output.
     *
     * @param arg argument to test
     * @return true if the arg requests help output.
     */
    private static boolean isHelp(String arg) {
        return "-h".equals(arg) || "--help".equals(arg);
    }
}