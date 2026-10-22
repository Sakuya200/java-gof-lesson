package lesson4.version3;


/**
 * 排序条件枚举：用来映射对应的排序逻辑
 * 注意，每新增一个排序条件，都要回来改这个枚举，同时还要改下面所有用到它的 switch
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public enum SortType {
    QUANTITY,        // 数量
    NAME,            // 名称
    COMMENT_COUNT,   // 评论数
    LIKE_COUNT,      // 点赞数（热度）
    PUBLISH_TIME     // 发布时间
}
