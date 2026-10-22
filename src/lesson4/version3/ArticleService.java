package lesson4.version3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 文章服务类，用于演示文章排序的逻辑，同时作为其他相似查询逻辑的服务
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class ArticleService {

    private final List<Article> articleList;

    public ArticleService(List<Article> articleList) {
        this.articleList = articleList;
    }

    /** 单条件排序 */
    public List<Article> query(SortType sortType, boolean ascending) {
        Comparator<Article> comparator = switch (sortType) {
            case LIKE_COUNT -> Comparator.comparingInt(Article::getLikeCount);
            case COMMENT_COUNT -> Comparator.comparingInt(Article::getCommentCount);
            case PUBLISH_TIME -> Comparator.comparing(Article::getPublishTime);
            default -> throw new IllegalArgumentException("文章不支持该排序条件：" + sortType);
        };
        List<Article> result = new ArrayList<>(articleList);
        result.sort(ascending ? comparator : comparator.reversed());
        return result;
    }

    /** 组合排序：第三遍 switch，代码和商品服务除了排序的类不同基本一致 */
    public List<Article> query(List<SortType> sortTypes, boolean ascending) {
        Comparator<Article> combined = null;
        for (SortType sortType : sortTypes) {
            Comparator<Article> comparator = switch (sortType) {
                case LIKE_COUNT -> Comparator.comparingInt(Article::getLikeCount);
                case COMMENT_COUNT -> Comparator.comparingInt(Article::getCommentCount);
                case PUBLISH_TIME -> Comparator.comparing(Article::getPublishTime);
                default -> throw new IllegalArgumentException("文章不支持该排序条件：" + sortType);
            };
            combined = (combined == null) ? comparator : combined.thenComparing(comparator);
        }
        List<Article> result = new ArrayList<>(articleList);
        result.sort(ascending ? combined : combined.reversed());
        return result;
    }
}
