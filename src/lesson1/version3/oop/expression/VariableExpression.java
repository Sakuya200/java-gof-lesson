package lesson1.version3.oop.expression;

/** 自定义函数的参数表达式 */
public class VariableExpression implements Expression {

    private final String name;
    private Expression value;

    /** 创建函数参数表达式 */
    public VariableExpression(String name) {
        this.name = name;
    }

    /** 绑定函数参数的实际表达式 */
    public void bind(Expression value) {
        this.value = value;
    }

    /** 计算参数绑定后的值 */
    @Override
    public Double compute() {
        if (value == null) {
            throw new IllegalStateException("Unbound function parameter: " + name);
        }
        return value.compute();
    }
}
