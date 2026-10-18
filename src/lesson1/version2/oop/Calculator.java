package lesson1.version2.oop;

import lesson1.version2.oop.expression.Expression;
import lesson1.version2.oop.operation.*;

import java.util.Map;

/** 计算器类 */
public class Calculator {

    private final Map<Character, Operation> operations;
    private final ExpressionParser parser;

    /** 创建并注册基础四则运算 */
    public Calculator() {
        operations = Map.of(
                AddOperation.SYMBOL, new AddOperation(),
                SubOperation.SYMBOL, new SubOperation(),
                MulOperation.SYMBOL, new MulOperation(),
                DivOperation.SYMBOL, new DivOperation()
        );
        parser = new ExpressionParser(operations);
    }

    /** 计算表达式的值 */
    public double calculate(String input) {
        Expression expression = parser.parse(input);
        return expression.compute();
    }

}
