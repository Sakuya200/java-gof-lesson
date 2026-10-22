package lesson4.version4.sort;

import java.util.Comparator;

/**
 * 策略接口：把"按什么条件排序"抽象成一个可以随时替换的对象
 *
 * @param <T> 该策略作用的对象类型
 * @author Sakuya200
 * @since 2026/10/22
 */
public interface SortStrategy<T> {
    /**
     * 返回本策略的比较器
     * 策略只需要描述"怎么比"，升序降序由调用方统一处理
     *
     * @return 返回比较器
     */
    Comparator<T> getComparator();
}
