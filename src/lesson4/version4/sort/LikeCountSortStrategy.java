package lesson4.version4.sort;

import lesson4.version4.Article;

import java.util.Comparator;

/**
 * 具体策略 4：按点赞数（热度）排序
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class LikeCountSortStrategy implements SortStrategy<Article> {
    @Override
    public Comparator<Article> getComparator() {
        return Comparator.comparingInt(Article::getLikeCount);
    }
}
