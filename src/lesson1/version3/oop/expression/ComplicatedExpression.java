package lesson1.version3.oop.expression;

import lesson1.version3.oop.operation.Operation;

/// 由左右子表达式和运算符组成的复合表达式
public class ComplicatedExpression implements Expression {

    private Expression left;

    private Operation operation;

    private Expression right;

    /// 创建复合表达式
    public ComplicatedExpression(Expression left, Operation operation, Expression right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    /// 计算复合表达式的值
    @Override
    public Double compute() {
        return operation.apply(left.compute(), right.compute());
    }
}
