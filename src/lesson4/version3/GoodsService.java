package lesson4.version3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 商品服务类，用于演示商品排序的逻辑
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class GoodsService {

    private final List<Goods> goodsList;

    public GoodsService(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    /** 单条件排序 */
    public List<Goods> query(SortType sortType, boolean ascending) {
        Comparator<Goods> comparator = switch (sortType) {
            case QUANTITY -> Comparator.comparingInt(Goods::getQuantity);
            case NAME -> Comparator.comparing(Goods::getName);
            case COMMENT_COUNT -> Comparator.comparingInt(Goods::getCommentCount);
            default ->
                // 货物没有点赞数和发布时间，只能在这里拦一下
                    throw new IllegalArgumentException("货物不支持该排序条件：" + sortType);
        };
        List<Goods> result = new ArrayList<>(goodsList);
        result.sort(ascending ? comparator : comparator.reversed());
        return result;
    }

    /** 组合排序：又抄了一遍上面的 switch，只是外面套了个循环 */
    public List<Goods> query(List<SortType> sortTypes, boolean ascending) {
        Comparator<Goods> combined = null;
        for (SortType sortType : sortTypes) {
            Comparator<Goods> comparator = switch (sortType) {
                case QUANTITY -> Comparator.comparingInt(Goods::getQuantity);
                case NAME -> Comparator.comparing(Goods::getName);
                case COMMENT_COUNT -> Comparator.comparingInt(Goods::getCommentCount);
                default -> throw new IllegalArgumentException("货物不支持该排序条件：" + sortType);
            };
            combined = (combined == null) ? comparator : combined.thenComparing(comparator);
        }
        List<Goods> result = new ArrayList<>(goodsList);
        result.sort(ascending ? combined : combined.reversed());
        return result;
    }


}
