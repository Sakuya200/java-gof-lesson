package lesson4.version4.sort;


import java.util.Comparator;

/**
 * 具体策略 1：按名称排序
 * 货物和文章共用同一个策略类，这就是把逻辑抽象出来之后拿到的复用收益
 * @author Sakuya200
 * @since 2026/10/22
 */
public class NameSortStrategy<T extends Nameable> implements SortStrategy<T> {
    @Override
    public Comparator<T> getComparator() {
        return Comparator.comparing(Nameable::getName);
    }

}
