package lesson1.version3.oop;

import lesson1.version3.oop.expression.Expression;
import lesson1.version3.oop.expression.VariableExpression;
import lesson1.version3.oop.function.CosFunction;
import lesson1.version3.oop.function.CustomFunction;
import lesson1.version3.oop.function.Function;
import lesson1.version3.oop.function.SinFunction;
import lesson1.version3.oop.operation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** 计算器类 */
public class Calculator {

    private final Map<Character, Operation> operations;
    private final Map<String, Function> functions;
    private final ExpressionParser parser;

    /** 创建计算器并注册基础运算和系统函数 */
    public Calculator() {
        operations = Map.of(
                AddOperation.SYMBOL, new AddOperation(),
                SubOperation.SYMBOL, new SubOperation(),
                MulOperation.SYMBOL, new MulOperation(),
                DivOperation.SYMBOL, new DivOperation()
        );
        functions = new HashMap<>();
        functions.put("sin", new SinFunction());
        functions.put("cos", new CosFunction());
        parser = new ExpressionParser(operations, functions, new HashMap<>());
    }

    /** 计算表达式的值 */
    public double calculate(String input) {
        Expression expression = parser.parse(input);
        return expression.compute();
    }

    /** 解析函数定义命令并注册用户函数 */
    public void defineFunction(String input) {
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


    /** 注册一个可由表达式调用的函数 */
    private void registerFunction(String name, Function function) {
        if (name == null || name.isBlank() || function == null) {
            throw new IllegalArgumentException("Function name and function must be provided");
        }
        functions.put(name, function);
    }

    /** 根据参数名和函数体定义一个用户函数，例如 square(x) = x * x */
    private void defineFunction(String name, String[] parameterNames, String body) {
        if (name == null || name.isBlank() || parameterNames == null || body == null) {
            throw new IllegalArgumentException("Function definition must be complete");
        }

        Map<String, VariableExpression> variables = new LinkedHashMap<>();
        for (String parameterName : parameterNames) {
            if (parameterName == null || parameterName.isBlank()
                    || variables.put(parameterName, new VariableExpression(parameterName)) != null) {
                throw new IllegalArgumentException("Invalid or duplicate parameter: " + parameterName);
            }
        }
        Expression functionBody = new ExpressionParser(operations, functions, variables).parse(body);
        registerFunction(name, new CustomFunction(functionBody, variables.values().stream().toList()));
    }
}
