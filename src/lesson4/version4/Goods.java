package lesson4.version4;

import lesson4.version4.sort.Commentable;
import lesson4.version4.sort.Nameable;

/**
 * 货物对象
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class Goods implements Nameable, Commentable {
    private final String name;      // 货物名称
    private final int quantity;     // 货物数量
    private final int commentCount;  // 货物评论数（新增字段，用于演示多条件排序）

    public Goods(String name, int quantity, int commentCount) {
        this.name = name;
        this.quantity = quantity;
        this.commentCount = commentCount;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCommentCount() {
        return commentCount;
    }

    @Override
    public String toString() {
        return name + "-" + quantity + "-" + commentCount;
    }
}
