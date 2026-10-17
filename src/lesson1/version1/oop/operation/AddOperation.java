package lesson1.version1.oop.operation;

/// 加法操作类
public class AddOperation implements Operation {

    public static final Character SYMBOL = '+';

    /// 执行加法
    @Override
    public Double apply(Double left, Double right) {
        return left + right;
    }
}
