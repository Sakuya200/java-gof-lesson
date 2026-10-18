package lesson1.version2.oop;

import lesson1.version2.oop.expression.ComplicatedExpression;
import lesson1.version2.oop.expression.Expression;
import lesson1.version2.oop.expression.NumberExpression;
import lesson1.version2.oop.operation.Operation;

import java.util.Map;

/** 表达式解析器，用于将用户的输入构建为 Expression 实现 */
public class ExpressionParser {

    private final Map<Character, Operation> operations;
    private String input;
    private int position;

    /** 创建表达式解析器，并保存可用的运算符实现 */
    public ExpressionParser(Map<Character, Operation> operations) {
        this.operations = operations;
    }

    /** 解析完整输入，并检查输入是否已经全部消费 */
    public Expression parse(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Expression must not be blank");
        }

        this.input = input;
        position = 0;
        Expression expression = parseExpression();
        skipWhitespace();
        if (position != input.length()) {
            throw new IllegalArgumentException(
                    "Unexpected character at position " + position + ": " + input.charAt(position));
        }
        return expression;
    }

    /** 根据运算符优先级构建表达式树；相同优先级按从左到右结合 */
    private Expression parseExpression() {
        return parseExpression(Integer.MIN_VALUE);
    }

    private Expression parseExpression(long minimumPriority) {
        Expression expression = parseFactor();
        while (true) {
            skipWhitespace();
            if (position >= input.length()) {
                return expression;
            }

            char symbol = input.charAt(position);
            Operation operation = operations.get(symbol);
            if (operation == null || operation.getPriority() < minimumPriority) {
                return expression;
            }
            position++;
            Expression right = parseExpression((long) operation.getPriority() + 1);
            expression = new ComplicatedExpression(expression, operation, right);
        }
    }

    /** 解析数字、括号表达式以及一元正负号 */
    private Expression parseFactor() {
        skipWhitespace();
        if (match('+')) {
            return parseFactor();
        }
        if (match('-')) {
            return createBinaryExpression(new NumberExpression(0D), '-', parseFactor());
        }
        if (match('(')) {
            Expression expression = parseExpression();
            skipWhitespace();
            if (!match(')')) {
                throw new IllegalArgumentException("Missing ')' at position " + position);
            }
            return expression;
        }
        return parseNumber();
    }

    /** 解析普通数字和小数 */
    private Expression parseNumber() {
        skipWhitespace();
        int start = position;
        boolean hasDigits = false;
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            position++;
            hasDigits = true;
        }
        if (position < input.length() && input.charAt(position) == '.') {
            position++;
            while (position < input.length() && Character.isDigit(input.charAt(position))) {
                position++;
                hasDigits = true;
            }
        }
        if (!hasDigits) {
            throw new IllegalArgumentException("Expected number at position " + start);
        }
        try {
            return new NumberExpression(Double.parseDouble(input.substring(start, position)));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid number at position " + start, exception);
        }
    }

    /** 根据运算符创建二元表达式，并校验运算符是否受支持 */
    private Expression createBinaryExpression(Expression left, char symbol, Expression right) {
        Operation operation = operations.get(symbol);
        if (operation == null) {
            throw new IllegalArgumentException("Unsupported operator: " + symbol);
        }
        return new ComplicatedExpression(left, operation, right);
    }

    /** 如果当前位置匹配指定字符，则消费该字符并返回 true */
    private boolean match(char expected) {
        if (position < input.length() && input.charAt(position) == expected) {
            position++;
            return true;
        }
        return false;
    }

    /** 跳过当前位置开始的所有空白字符 */
    private void skipWhitespace() {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }

}
