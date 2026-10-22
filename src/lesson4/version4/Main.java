package lesson4.version4;

import lesson4.version4.sort.CommentCountSortStrategy;
import lesson4.version4.sort.LikeCountSortStrategy;
import lesson4.version4.sort.NameSortStrategy;
import lesson4.version4.sort.QuantitySortStrategy;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 测试类
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class Main {

     static void main(String[] args) {
        QueryService<Goods> goodsService = new QueryService<>(Arrays.asList(
                new Goods("Keyboard", 30, 12), new Goods("Monitor", 8, 40), new Goods("Mouse", 45, 12)));
        // 先按评论数降序，评论数相同的再按数量降序，两个策略直接组合即可
        System.out.println(goodsService.query(
                Arrays.asList(new CommentCountSortStrategy<>(), new QuantitySortStrategy()), false));

        QueryService<Article> articleService = new QueryService<>(Arrays.asList(
                new Article("设计模式入门", 120, 30, LocalDateTime.of(2026, 10, 1, 9, 0)),
                new Article("面向对象原则", 120, 8, LocalDateTime.of(2026, 9, 20, 9, 0)),
                new Article("UML 类图速览", 45, 66, LocalDateTime.of(2026, 10, 18, 9, 0))));
        // 先按点赞数降序，点赞数相同的再按评论数降序
        System.out.println(articleService.query(
                Arrays.asList(new LikeCountSortStrategy(), new CommentCountSortStrategy<>()), false));

        // 同一个 NameSortStrategy 分别作用在货物和文章上：V3 里做不到，V4 里一行就够
        System.out.println(goodsService.query(new NameSortStrategy<>(), true));
        System.out.println(articleService.query(new NameSortStrategy<>(), true));

        // 今后要加"按库存排序"，只需再写一个策略类实现 SortStrategy，
        // QueryService 和其他所有策略都不用动一行代码，这也很好的体现了开闭原则
    }
}
