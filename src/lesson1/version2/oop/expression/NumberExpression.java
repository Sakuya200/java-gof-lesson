package lesson1.version2.oop.expression;

/// 数字表达式
public class NumberExpression implements Expression {
    private Double value;

    /// 创建数字表达式
    public NumberExpression(Double value) {
        this.value = value;
    }

    /// 返回数字的值
    @Override
    public Double compute() {
        return value;
    }
}
