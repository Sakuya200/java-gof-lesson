package lesson1.version2.oop.operation;

/** 除法操作类 */
public class DivOperation implements Operation {

    public static final Character SYMBOL = '/';

    @Override
    public int getPriority() {
        return 2;
    }

    /** 执行除法 */
    @Override
    public Double apply(Double left, Double right) {
        return left / right;
    }
}
