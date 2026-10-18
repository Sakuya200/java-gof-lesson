package lesson1.version3.oop.function;

import lesson1.version3.oop.expression.Expression;
import lesson1.version3.oop.expression.VariableExpression;

import java.util.List;

/** 基于表达式和参数变量的自定义函数 */
public class CustomFunction implements Function {

    private final Expression expression;

    private final List<Expression> arguments;

    /** 创建一个由表达式和参数变量组成的自定义函数 */
    public CustomFunction(Expression expression, List<? extends VariableExpression> arguments) {
        this.expression = expression;
        this.arguments = List.copyOf(arguments);
    }

    /** 绑定实参并计算函数结果 */
    @Override
    public Double apply(Expression... args) {
        if (args.length != arguments.size()) {
            throw new IllegalArgumentException(
                    "Expected " + arguments.size() + " arguments but got " + args.length);
        }
        for (int i = 0; i < args.length; i++) {
            ((VariableExpression) arguments.get(i)).bind(args[i]);
        }
        return expression.compute();
    }
}
