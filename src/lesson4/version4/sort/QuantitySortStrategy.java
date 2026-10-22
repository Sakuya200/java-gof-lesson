package lesson4.version4.sort;

import lesson4.version4.Goods;

import java.util.Comparator;

/**
 * 具体策略 3：按数量排序，仅适用于货物
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class QuantitySortStrategy implements SortStrategy<Goods> {
    @Override
    public Comparator<Goods> getComparator() {
        return Comparator.comparingInt(Goods::getQuantity);
    }
}
