package lesson1.version3.oop.function;

import lesson1.version3.oop.expression.Expression;

/** 正弦函数实现 */
public class SinFunction implements Function {

    /** 执行正弦函数 */
    @Override
    public Double apply(Expression... args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("sin expects exactly one argument");
        }
        return Math.sin(args[0].compute());
    }
}
