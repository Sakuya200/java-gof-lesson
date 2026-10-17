package lesson1.version2.pop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

/// 支持逆波兰表达式的面向过程计算器示例
public class PopExample {

    /// 启动计算器交互程序
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            List<String> reversePolishExpression = parse(input);
            System.out.println("Result: " + compute(reversePolishExpression));
        }
    }

    /// 将中缀表达式解析为逆波兰表达式
    private static List<String> parse(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Expression must not be blank");
        }

        List<String> output = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();
        boolean expectOperand = true;
        int position = 0;

        while (position < input.length()) {
            char current = input.charAt(position);
            if (Character.isWhitespace(current)) {
                position++;
                continue;
            }

            if (Character.isDigit(current) || current == '.') {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Missing operator at position " + position);
                }
                int start = position;
                position = parseNumberEnd(input, position);
                output.add(input.substring(start, position));
                expectOperand = false;
                continue;
            }

            if (current == '(') {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Missing operator at position " + position);
                }
                operators.push("(");
                position++;
                continue;
            }

            if (current == ')') {
                if (expectOperand) {
                    throw new IllegalArgumentException("Unexpected ')' at position " + position);
                }
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("Unmatched ')' at position " + position);
                }
                operators.pop();
                position++;
                continue;
            }

            if (isOperator(current)) {
                boolean unary = expectOperand && (current == '+' || current == '-');
                if (expectOperand && !unary) {
                    throw new IllegalArgumentException("Expected number at position " + position);
                }
                String operator = unary ? "u" + current : String.valueOf(current);
                while (shouldPop(operator, operators)) {
                    output.add(operators.pop());
                }
                operators.push(operator);
                position++;
                if (!unary) {
                    expectOperand = true;
                }
                continue;
            }

            throw new IllegalArgumentException("Unexpected character at position " + position + ": " + current);
        }

        if (expectOperand) {
            throw new IllegalArgumentException("Expression must end with a number or ')'");
        }
        while (!operators.isEmpty()) {
            if (operators.peek().equals("(")) {
                throw new IllegalArgumentException("Missing ')'");
            }
            output.add(operators.pop());
        }
        return output;
    }

    /// 执行逆波兰表达式并返回计算结果
    private static Double compute(List<String> reversePolishExpression) {
        if (reversePolishExpression == null || reversePolishExpression.isEmpty()) {
            throw new IllegalArgumentException("Reverse Polish expression must not be empty");
        }

        Deque<Double> values = new ArrayDeque<>();
        for (String token : reversePolishExpression) {
            if (!isOperator(token)) {
                try {
                    values.push(Double.parseDouble(token));
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException("Invalid number: " + token, exception);
                }
                continue;
            }

            if (token.equals("u+") || token.equals("u-")) {
                if (values.isEmpty()) {
                    throw new IllegalArgumentException("Missing operand for operator: " + token);
                }
                double value = values.pop();
                values.push(token.equals("u-") ? sub(0D, value) : value);
                continue;
            }

            if (values.size() < 2) {
                throw new IllegalArgumentException("Missing operand for operator: " + token);
            }
            double right = values.pop();
            double left = values.pop();
            values.push(apply(left, token, right));
        }

        if (values.size() != 1) {
            throw new IllegalArgumentException("Invalid reverse Polish expression");
        }
        return values.pop();
    }

    /// 解析数字的结束位置
    private static int parseNumberEnd(String input, int start) {
        int position = start;
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
            throw new IllegalArgumentException("Invalid number at position " + start);
        }
        try {
            Double.parseDouble(input.substring(start, position));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid number at position " + start, exception);
        }
        return position;
    }

    /// 判断字符是否为支持的二元运算符
    private static boolean isOperator(char value) {
        return value == '+' || value == '-' || value == '*' || value == '/';
    }

    /// 判断字符串是否为支持的运算符，包括一元正负号
    private static boolean isOperator(String value) {
        return value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")
                || value.equals("u+") || value.equals("u-");
    }

    /// 根据运算符优先级判断是否应先弹出栈顶运算符
    private static boolean shouldPop(String current, Deque<String> operators) {
        if (operators.isEmpty() || operators.peek().equals("(")) {
            return false;
        }
        String top = operators.peek();
        return precedence(top) > precedence(current)
                || (precedence(top) == precedence(current) && !isUnary(current));
    }

    /// 返回运算符的优先级
    private static int precedence(String operator) {
        if (operator.equals("u+") || operator.equals("u-")) {
            return 3;
        }
        if (operator.equals("*") || operator.equals("/")) {
            return 2;
        }
        return 1;
    }

    /// 判断运算符是否为一元运算符
    private static boolean isUnary(String operator) {
        return operator.equals("u+") || operator.equals("u-");
    }

    /// 执行一个二元运算
    private static double apply(double left, String operator, double right) {
        switch (operator) {
            case "+":
                return add(left, right);
            case "-":
                return sub(left, right);
            case "*":
                return mul(left, right);
            case "/":
                return div(left, right);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    /// 执行加法
    private static Double add(double left, double right) {
        return left + right;
    }

    /// 执行减法
    private static Double sub(double left, double right) {
        return left - right;
    }

    /// 执行乘法
    private static Double mul(double left, double right) {
        return left * right;
    }

    /// 执行除法
    private static Double div(double left, double right) {
        return left / right;
    }
}
