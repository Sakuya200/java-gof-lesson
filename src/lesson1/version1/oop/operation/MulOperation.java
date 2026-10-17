package lesson1.version1.oop.operation;

/// 乘法操作类
public class MulOperation implements Operation {

    public static final Character SYMBOL = '*';

    /// 执行乘法
    @Override
    public Double apply(Double left, Double right) {
        return left * right;
    }


}
