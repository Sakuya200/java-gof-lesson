package lesson4.version3;

import java.time.LocalDateTime;

/**
 * 文章类
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class Article {
    private final String title;
    private final int likeCount;
    private final int commentCount;
    private final LocalDateTime publishTime;

    public Article(String title, int likeCount, int commentCount, LocalDateTime publishTime) {
        this.title = title;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    @Override
    public String toString() {
        return title + "[点赞=" + likeCount + ", 评论=" + commentCount
                + ", 时间=" + publishTime + "]";
    }
}
