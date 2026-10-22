package lesson4.version3;

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
        GoodsService goodsService = new GoodsService(
                Arrays.asList(
                        new Goods("Keyboard", 30, 12),
                        new Goods("Monitor", 8, 40),
                        new Goods("Mouse", 45, 12)
                )
        );
        // 先按评论数降序，评论数相同的再按数量降序
        System.out.println(
                goodsService.query(
                        Arrays.asList(SortType.COMMENT_COUNT, SortType.QUANTITY), false
                )
        );

        ArticleService articleService = new ArticleService(
                Arrays.asList(
                        new Article("设计模式入门", 120, 30, LocalDateTime.of(2026, 10, 1, 9, 0)),
                        new Article("面向对象原则", 120, 8, LocalDateTime.of(2026, 9, 20, 9, 0)),
                        new Article("UML 类图速览", 45, 66, LocalDateTime.of(2026, 10, 18, 9, 0))
                )
        );
        // 先按点赞数降序，点赞数相同的再按评论数降序
        System.out.println(
                articleService.query(
                        Arrays.asList(SortType.LIKE_COUNT, SortType.COMMENT_COUNT), false
                )
        );
    }
}
