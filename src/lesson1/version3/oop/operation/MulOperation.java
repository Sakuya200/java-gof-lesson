package lesson1.version3.oop.operation;

/// 乘法操作类
public class MulOperation implements Operation {

    public static final Character SYMBOL = '*';

    @Override
    public int getPriority() {
        return 2;
    }

    /// 执行乘法
    @Override
    public Double apply(Double left, Double right) {
        return left * right;
    }


}
