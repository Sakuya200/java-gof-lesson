package lesson1.version1.pop;
import java.util.Scanner;

/** 面向过程计算器示例 */
public class PopExample {

    /** 启动计算器交互程序 */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            System.out.println("Result: " + parseAndCompute(input));
        }
    }

    /** 解析并计算二元表达式 */
    private static Double parseAndCompute(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Invalid input format. Expected: <number> <operator> <number>");
        }

        String expression = input.replace(" ", "");
        int operatorPosition = findOperatorPosition(expression);
        if (operatorPosition < 1 || operatorPosition == expression.length() - 1) {
            throw new IllegalArgumentException("Invalid input format. Expected: <number> <operator> <number>");
        }

        Double left = parseNumber(expression.substring(0, operatorPosition));
        Double right = parseNumber(expression.substring(operatorPosition + 1));

        String operator = parseOperation(expression, operatorPosition);

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

    /** 将字符串切分为数字后校验其合法性 */
    private static double parseNumber(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Invalid input format. Expected: <number> <operator> <number>", exception);
        }
    }

    private static String parseOperation(String expression, int operatorPosition) {
        return String.valueOf(expression.charAt(operatorPosition));
    }


    /** 找到第一个运算符的位置，并跳过可能出现在首位的负号 */
    private static int findOperatorPosition(String expression) {
        for (int i = 1; i < expression.length(); i++) {
            if (expression.charAt(i) == '+'
                    || expression.charAt(i) == '-'
                    || expression.charAt(i) == '*'
                    || expression.charAt(i) == '/') {
                return i;
            }
        }
        return -1;
    }

    /** 执行加法 */
    private static Double add(double left, double right) {
        return left + right;
    }

    /** 执行减法 */
    private static Double sub(double left, double right) {
        return left - right;
    }

    /** 执行乘法 */
    private static Double mul(double left, double right) {
        return left * right;
    }

    /** 执行除法 */
    private static Double div(double left, double right) {
        return left / right;
    }
}
