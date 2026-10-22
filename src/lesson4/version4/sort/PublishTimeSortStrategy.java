package lesson4.version4.sort;

import lesson4.version4.Article;

import java.util.Comparator;

/**
 * 具体策略 5：按发布时间排序
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class PublishTimeSortStrategy implements SortStrategy<Article> {
    @Override
    public Comparator<Article> getComparator() {
        return Comparator.comparing(Article::getPublishTime);
    }
}
