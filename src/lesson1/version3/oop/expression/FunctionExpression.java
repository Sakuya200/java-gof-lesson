package lesson1.version3.oop.expression;

import lesson1.version3.oop.function.Function;

/** 函数调用表达式 */
public class FunctionExpression implements Expression {

    private final Function function;
    private final Expression[] arguments;

    /** 创建函数调用表达式 */
    public FunctionExpression(Function function, Expression... arguments) {
        this.function = function;
        this.arguments = arguments.clone();
    }

    /** 计算函数调用结果 */
    @Override
    public Double compute() {
        return function.apply(arguments);
    }
}
