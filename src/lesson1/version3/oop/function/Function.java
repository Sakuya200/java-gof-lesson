package lesson1.version3.oop.function;

import lesson1.version3.oop.expression.Expression;


/// 函数接口
public interface Function {

    /// 使用表达式参数执行函数
    Double apply(Expression... args);
}
