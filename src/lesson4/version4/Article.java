package lesson4.version4;

import lesson4.version4.sort.Commentable;
import lesson4.version4.sort.Nameable;

import java.time.LocalDateTime;

/**
 * 文章类
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class Article implements Nameable, Commentable {
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

    /** 文章没有"货物名称"，但标题就是它对外展示的名称，因此同样实现了 Nameable */
    @Override
    public String getName() { return title; }
    public String getTitle() { return title; }
    public int getLikeCount() { return likeCount; }
    @Override
    public int getCommentCount() { return commentCount; }
    public LocalDateTime getPublishTime() { return publishTime; }

    @Override
    public String toString() {
        return title + "[点赞=" + likeCount + ", 评论=" + commentCount
                + ", 时间=" + publishTime + "]";
    }
}
