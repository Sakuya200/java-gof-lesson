package lesson1.version1.oop;

import lesson1.version1.oop.operation.*;

import java.util.Map;

/** 计算器类 */
public class Calculator {

    private final Map<Character, Operation> operations;

    /** 创建并注册基础四则运算 */
    public Calculator() {
        operations = Map.of(
                AddOperation.SYMBOL, new AddOperation(),
                SubOperation.SYMBOL, new SubOperation(),
                MulOperation.SYMBOL, new MulOperation(),
                DivOperation.SYMBOL, new DivOperation()
        );
    }

    /** 计算表达式的值 */
    public double calculate(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Invalid expression: " + input);
        }

        String expression = input.replace(" ", "");
        int operatorPosition = findOperatorPosition(expression);
        if (operatorPosition < 1 || operatorPosition == expression.length() - 1) {
            throw new IllegalArgumentException("Invalid expression: " + input);
        }

        double left = parseNumber(expression.substring(0, operatorPosition), input);
        char operator = expression.charAt(operatorPosition);
        Operation operation = operations.get(operator);
        if (operation == null) {
            throw new IllegalArgumentException("Unsupported operator: " + operator);
        }

        double right = parseNumber(expression.substring(operatorPosition + 1), input);
        return operation.apply(left, right);
    }

    /** 将字符串切分为数字后校验其合法性 */
    private double parseNumber(String value, String originalInput) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid expression: " + originalInput, exception);
        }
    }

    /** 找到第一个运算符的位置，并跳过可能出现在首位的负号 */
    private int findOperatorPosition(String expression) {
        for (int i = 1; i < expression.length(); i++) {
            if (operations.containsKey(expression.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

}
