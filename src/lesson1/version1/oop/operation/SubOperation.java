package lesson1.version1.oop.operation;

/** 减法操作类 */
public class SubOperation implements Operation {

    public static final Character SYMBOL = '-';

    /** 执行减法 */
    @Override
    public Double apply(Double left, Double right) {
        return left - right;
    }
}
