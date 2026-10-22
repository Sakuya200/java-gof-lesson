package lesson4.version4.sort;

import java.util.Comparator;

/**
 * 具体策略 2：按评论数排序，货物和文章共用
 *
 * @param <T> 可评论对象的类型
 * @author Sakuya200
 * @since 2026/10/22
 */
public class CommentCountSortStrategy<T extends Commentable> implements SortStrategy<T> {
    @Override
    public Comparator<T> getComparator() {
        return Comparator.comparingInt(Commentable::getCommentCount);
    }
}
