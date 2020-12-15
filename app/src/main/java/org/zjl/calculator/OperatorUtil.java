package org.zjl.calculator;

public final class OperatorUtil {
    private OperatorUtil() {
    }

    public static boolean isOperator(char input) {
        return input == '+' || input == '-' || input == '*' || input == '/';
    }

    public static boolean isOperator(String input) {
        return input.equals("/") || input.equals("-") || input.equals("+") || input.equals("*");
    }
}
