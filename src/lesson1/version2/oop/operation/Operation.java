package lesson1.version2.oop.operation;

/// 操作接口
public interface Operation {

    /// 返回运算符优先级，数值越大优先级越高
    int getPriority();

    /// 对两个操作数执行运算
    Double apply(Double left, Double right);

}
