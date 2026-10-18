package lesson1.version3.oop;

import lesson1.version3.oop.expression.ComplicatedExpression;
import lesson1.version3.oop.expression.Expression;
import lesson1.version3.oop.expression.FunctionExpression;
import lesson1.version3.oop.expression.NumberExpression;
import lesson1.version3.oop.expression.VariableExpression;
import lesson1.version3.oop.function.Function;
import lesson1.version3.oop.operation.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 表达式解析器，用于将用户输入构建为 Expression 实现 */
public class ExpressionParser {

    private final Map<Character, Operation> operations;
    private final Map<String, Function> functions;
    private final Map<String, VariableExpression> variables;
    private String input;
    private int position;

    /** 创建只支持运算符的解析器，保留第二版构造方式 */
    public ExpressionParser(Map<Character, Operation> operations) {
        this(operations, new HashMap<>(), new HashMap<>());
    }

    /** 创建支持函数和变量的解析器 */
    public ExpressionParser(
            Map<Character, Operation> operations,
            Map<String, Function> functions,
            Map<String, VariableExpression> variables) {
        this.operations = operations;
        this.functions = functions;
        this.variables = variables;
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

    /** 解析数字、函数调用、变量、括号表达式和一元正负号 */
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
        if (position < input.length() && isIdentifierStart(input.charAt(position))) {
            return parseIdentifier();
        }
        return parseNumber();
    }

    /** 解析函数调用或自定义函数参数 */
    private Expression parseIdentifier() {
        int start = position;
        position++;
        while (position < input.length() && isIdentifierPart(input.charAt(position))) {
            position++;
        }
        String name = input.substring(start, position);
        skipWhitespace();

        if (!match('(')) {
            VariableExpression variable = variables.get(name);
            if (variable == null) {
                throw new IllegalArgumentException("Unknown identifier: " + name);
            }
            return variable;
        }

        List<Expression> arguments = new ArrayList<>();
        skipWhitespace();
        if (!match(')')) {
            while (true) {
                arguments.add(parseExpression());
                skipWhitespace();
                if (match(')')) {
                    break;
                }
                if (!match(',')) {
                    throw new IllegalArgumentException("Expected ',' or ')' at position " + position);
                }
            }
        }

        Function function = functions.get(name);
        if (function == null) {
            throw new IllegalArgumentException("Unknown function: " + name);
        }
        return new FunctionExpression(function, arguments.toArray(new Expression[0]));
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

    /** 根据运算符创建二元表达式 */
    private Expression createBinaryExpression(Expression left, char symbol, Expression right) {
        Operation operation = operations.get(symbol);
        if (operation == null) {
            throw new IllegalArgumentException("Unsupported operator: " + symbol);
        }
        return new ComplicatedExpression(left, operation, right);
    }

    /** 判断当前位置是否匹配指定字符，并在匹配时消费字符 */
    private boolean match(char expected) {
        if (position < input.length() && input.charAt(position) == expected) {
            position++;
            return true;
        }
        return false;
    }

    /** 判断字符是否可以作为标识符首字符 */
    private boolean isIdentifierStart(char value) {
        return Character.isLetter(value) || value == '_';
    }

    /** 判断字符是否可以出现在标识符中 */
    private boolean isIdentifierPart(char value) {
        return Character.isLetterOrDigit(value) || value == '_';
    }

    /** 跳过当前位置开始的所有空白字符 */
    private void skipWhitespace() {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }
}
