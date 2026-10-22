package lesson4.version4;

import lesson4.version4.sort.SortStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 通用查询服务：只依赖 SortStrategy 这个抽象，和具体的被排序的类无关
 * 货物、文章，以及今后新增的任何对象，都由这一份代码统一处理
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class QueryService<T> {

    private final List<T> source;

    public QueryService(List<T> source) {
        this.source = source;
    }

    /** 单条件排序 */
    public List<T> query(SortStrategy<T> strategy, boolean ascending) {
        return query(Collections.singletonList(strategy), ascending);
    }

    /** 组合排序：把传入的策略按顺序串成一个比较器，代码逻辑相当简练 */
    public List<T> query(List<SortStrategy<T>> strategies, boolean ascending) {
        if (strategies == null || strategies.isEmpty()) {
            throw new IllegalArgumentException("至少需要指定一个排序策略");
        }
        Comparator<T> combined = strategies.get(0).getComparator();
        for (int i = 1; i < strategies.size(); i++) {
            combined = combined.thenComparing(strategies.get(i).getComparator());
        }
        List<T> result = new ArrayList<>(source);
        result.sort(ascending ? combined : combined.reversed());
        return result;
    }
}
