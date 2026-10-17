package lesson1.version3.pop;

import java.util.*;

/// 支持系统函数和自定义函数的面向过程计算器示例
public class PopExample {

    /// 函数的参数数量，系统函数和自定义函数都在此注册
    private static final Map<String, Integer> FUNCTION_ARITIES = new HashMap<>();
    /// 自定义函数的参数名列表，按定义顺序保存
    private static final Map<String, List<String>> CUSTOM_PARAMETERS = new HashMap<>();
    /// 自定义函数的逆波兰表达式体，使用参数名引用参数
    private static final Map<String, List<String>> CUSTOM_BODIES = new HashMap<>();

    static {
        FUNCTION_ARITIES.put("sin", 1);
        FUNCTION_ARITIES.put("cos", 1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.trim().startsWith("def ")) {
                defineFunction(input);
                System.out.println("Function defined");
            } else {
                List<String> reversePolishExpression = parse(input);
                System.out.println("Result: " + compute(reversePolishExpression));
            }
        }
    }

    /// 将中缀表达式解析为逆波兰表达式
    public static List<String> parse(String input) {
        return parse(input, Set.of());
    }

    /// 将表达式解析为逆波兰表达式，并允许函数体引用参数变量
    private static List<String> parse(String input, Set<String> parameters) {
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

            if (isIdentifierStart(current)) {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Missing operator at position " + position);
                }
                int start = position++;
                while (position < input.length() && isIdentifierPart(input.charAt(position))) {
                    position++;
                }
                String name = input.substring(start, position);
                position = skipWhitespace(input, position);

                if (position < input.length() && input.charAt(position) == '(') {
                    position++;
                    int argumentCount = 0;
                    position = skipWhitespace(input, position);
                    if (position == input.length()) {
                        throw new IllegalArgumentException("Missing ')' at position " + position);
                    }
                    if (input.charAt(position) != ')') {
                        while (true) {
                            int before = position;
                            List<String> argument = parseExpression(input, position, parameters);
                            position = nextExpressionPosition(input, before);
                            output.addAll(argument);
                            argumentCount++;
                            position = skipWhitespace(input, position);
                            if (position < input.length() && input.charAt(position) == ')') {
                                break;
                            }
                            if (position >= input.length() || input.charAt(position) != ',') {
                                throw new IllegalArgumentException(
                                        "Expected ',' or ')' at position " + position);
                            }
                            position++;
                        }
                    }
                    if (position == input.length()) {
                        throw new IllegalArgumentException("Missing ')' at position " + position);
                    }
                    if (input.charAt(position) != ')') {
                        throw new IllegalArgumentException("Missing ')' at position " + position);
                    }
                    position++;
                    validateFunction(name, argumentCount);
                    output.add(functionToken(name, argumentCount));
                    expectOperand = false;
                    continue;
                }

                if (!parameters.contains(name)) {
                    throw new IllegalArgumentException("Unknown identifier: " + name);
                }
                output.add(variableToken(name));
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

            if (current == ',') {
                break;
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

    /// 通过扫描括号和逗号定位函数参数边界
    private static List<String> parseExpression(String input, int start, Set<String> parameters) {
        int depth = 0;
        int position = start;
        while (position < input.length()) {
            char current = input.charAt(position);
            if (current == '(') {
                depth++;
            } else if (current == ')') {
                if (depth == 0) {
                    break;
                }
                depth--;
            } else if (current == ',' && depth == 0) {
                break;
            }
            position++;
        }
        return parse(input.substring(start, position), parameters);
    }

    /// 返回下一个函数参数的起始位置
    private static int nextExpressionPosition(String input, int start) {
        int depth = 0;
        int position = start;
        while (position < input.length()) {
            char current = input.charAt(position);
            if (current == '(') {
                depth++;
            } else if (current == ')') {
                if (depth == 0) {
                    return position;
                }
                depth--;
            } else if (current == ',' && depth == 0) {
                return position;
            }
            position++;
        }
        return position;
    }

    /// 执行逆波兰表达式
    public static Double compute(List<String> reversePolishExpression) {
        return compute(reversePolishExpression, new HashMap<>());
    }

    /// 使用变量绑定执行逆波兰表达式
    private static Double compute(List<String> expression, Map<String, Double> variables) {
        if (expression == null || expression.isEmpty()) {
            throw new IllegalArgumentException("Reverse Polish expression must not be empty");
        }

        Deque<Double> values = new ArrayDeque<>();
        for (String token : expression) {
            if (token.startsWith("v:")) {
                Double value = variables.get(token.substring(2));
                if (value == null) {
                    throw new IllegalArgumentException("Unbound variable: " + token.substring(2));
                }
                values.push(value);
            } else if (token.startsWith("f:")) {
                applyFunction(token, values);
            } else if (!isOperator(token)) {
                values.push(parseNumber(token));
            } else {
                applyOperator(token, values);
            }
        }

        if (values.size() != 1) {
            throw new IllegalArgumentException("Invalid reverse Polish expression");
        }
        return values.pop();
    }

    /// 定义用户函数，函数体使用参数名引用参数
    public static void defineFunction(String name, String[] parameterNames, String body) {
        validateName(name);
        if (parameterNames == null || body == null) {
            throw new IllegalArgumentException("Function definition must be complete");
        }

        Set<String> uniqueParameters = new LinkedHashSet<>();
        for (String parameterName : parameterNames) {
            validateName(parameterName);
            if (!uniqueParameters.add(parameterName)) {
                throw new IllegalArgumentException("Duplicate parameter: " + parameterName);
            }
        }
        List<String> rpn = parse(body, uniqueParameters);
        CUSTOM_PARAMETERS.put(name, List.copyOf(uniqueParameters));
        CUSTOM_BODIES.put(name, rpn);
        FUNCTION_ARITIES.put(name, parameterNames.length);
    }

    /// 解析函数定义命令并注册用户函数
    private static void defineFunction(String input) {
        String definition = input.trim().substring(4);
        int equalsPosition = definition.indexOf('=');
        int leftParenthesis = definition.indexOf('(');
        int rightParenthesis = leftParenthesis < 0
                ? -1
                : definition.indexOf(')', leftParenthesis + 1);
        if (equalsPosition < 0
                || leftParenthesis < 0
                || rightParenthesis < leftParenthesis
                || leftParenthesis > equalsPosition
                || rightParenthesis > equalsPosition) {
            throw new IllegalArgumentException("Invalid function definition");
        }

        String name = definition.substring(0, leftParenthesis).trim();
        String parameterText = definition.substring(leftParenthesis + 1, rightParenthesis).trim();
        String body = definition.substring(equalsPosition + 1).trim();
        String[] parameters = parameterText.isEmpty() ? new String[0] : parameterText.split("\\s*,\\s*");
        defineFunction(name, parameters, body);
    }

    /// 执行一个函数调用
    private static void applyFunction(String token, Deque<Double> values) {
        String[] parts = token.substring(2).split(":", 2);
        String name = parts[0];
        int arity = Integer.parseInt(parts[1]);
        if (values.size() < arity) {
            throw new IllegalArgumentException("Missing operand for function: " + name);
        }

        List<Double> arguments = new ArrayList<>();
        for (int i = 0; i < arity; i++) {
            arguments.add(values.pop());
        }
        Collections.reverse(arguments);

        if (name.equals("sin") || name.equals("cos")) {
            double value = arguments.get(0);
            values.push(name.equals("sin") ? Math.sin(value) : Math.cos(value));
            return;
        }

        List<String> parameters = CUSTOM_PARAMETERS.get(name);
        List<String> body = CUSTOM_BODIES.get(name);
        if (parameters == null || body == null) {
            throw new IllegalArgumentException("Unknown function: " + name);
        }
        Map<String, Double> variables = new HashMap<>();
        for (int i = 0; i < parameters.size(); i++) {
            variables.put(parameters.get(i), arguments.get(i));
        }
        values.push(compute(body, variables));
    }

    /// 应用一个二元或一元运算
    private static void applyOperator(String operator, Deque<Double> values) {
        if (operator.equals("u+") || operator.equals("u-")) {
            if (values.isEmpty()) {
                throw new IllegalArgumentException("Missing operand for operator: " + operator);
            }
            double value = values.pop();
            values.push(operator.equals("u-") ? -value : value);
            return;
        }
        if (values.size() < 2) {
            throw new IllegalArgumentException("Missing operand for operator: " + operator);
        }
        double right = values.pop();
        double left = values.pop();
        switch (operator) {
            case "+":
                values.push(left + right);
                break;
            case "-":
                values.push(left - right);
                break;
            case "*":
                values.push(left * right);
                break;
            case "/":
                values.push(left / right);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    /// 校验函数是否存在且参数数量正确
    private static void validateFunction(String name, int argumentCount) {
        Integer arity = FUNCTION_ARITIES.get(name);
        if (arity == null) {
            throw new IllegalArgumentException("Unknown function: " + name);
        }
        if (arity != argumentCount) {
            throw new IllegalArgumentException(
                    name + " expects " + arity + " arguments but got " + argumentCount);
        }
    }

    /// 生成函数调用令牌
    private static String functionToken(String name, int arity) {
        return "f:" + name + ":" + arity;
    }

    /// 生成变量令牌
    private static String variableToken(String name) {
        return "v:" + name;
    }

    /// 解析数字令牌
    private static double parseNumber(String token) {
        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid number: " + token, exception);
        }
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
        parseNumber(input.substring(start, position));
        return position;
    }

    /// 判断字符是否为标识符首字符
    private static boolean isIdentifierStart(char value) {
        return Character.isLetter(value) || value == '_';
    }

    /// 判断字符是否为标识符组成字符
    private static boolean isIdentifierPart(char value) {
        return Character.isLetterOrDigit(value) || value == '_';
    }

    /// 校验标识符
    private static void validateName(String name) {
        if (name == null || name.isBlank() || !isIdentifierStart(name.charAt(0))) {
            throw new IllegalArgumentException("Invalid function or parameter name: " + name);
        }
        for (int i = 1; i < name.length(); i++) {
            if (!isIdentifierPart(name.charAt(i))) {
                throw new IllegalArgumentException("Invalid function or parameter name: " + name);
            }
        }
    }

    /// 判断字符是否为运算符
    private static boolean isOperator(char value) {
        return value == '+' || value == '-' || value == '*' || value == '/';
    }

    /// 判断令牌是否为运算符
    private static boolean isOperator(String value) {
        return value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")
                || value.equals("u+") || value.equals("u-");
    }

    /// 判断是否应该弹出栈顶运算符
    private static boolean shouldPop(String current, Deque<String> operators) {
        if (operators.isEmpty() || operators.peek().equals("(")) {
            return false;
        }
        String top = operators.peek();
        return precedence(top) > precedence(current)
                || (precedence(top) == precedence(current) && !isUnary(current));
    }

    /// 获取运算符优先级
    private static int precedence(String operator) {
        if (operator.equals("u+") || operator.equals("u-")) {
            return 3;
        }
        if (operator.equals("*") || operator.equals("/")) {
            return 2;
        }
        return 1;
    }

    /// 判断是否为一元运算符
    private static boolean isUnary(String operator) {
        return operator.equals("u+") || operator.equals("u-");
    }

    /// 返回跳过空白字符后的位置
    private static int skipWhitespace(String input, int position) {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
        return position;
    }
}
