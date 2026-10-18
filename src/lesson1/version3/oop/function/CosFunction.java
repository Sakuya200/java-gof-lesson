package lesson1.version3.oop.function;

import lesson1.version3.oop.expression.Expression;

/** 余弦函数实现 */
public class CosFunction implements Function {

    /** 执行余弦函数 */
    @Override
    public Double apply(Expression... args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("cos expects exactly one argument");
        }
        return Math.cos(args[0].compute());
    }
}
