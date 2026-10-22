package lesson4.version2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 商品服务类，用于演示商品排序的逻辑
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class GoodsService {

    /**
     * 按货物数量排序
     *
     * @param goodsList 待排序的货物列表
     * @param ascending true 为升序，false 为降序
     */
    public static void sortByQuantity(List<Goods> goodsList, boolean ascending) {
        // 此处使用 lambda 函数简化了排序逻辑的编写
        // 函数式编程也是一个很有意思的概念，如果有机会的话未来也会介绍
        goodsList.sort(
                (g1, g2) -> ascending
                ? Integer.compare(g1.getQuantity(), g2.getQuantity())
                : Integer.compare(g2.getQuantity(), g1.getQuantity())
        );
    }

    /**
     * 新增需求：按货物名称排序，把上面的方法复制一份，只把比较的字段从数量换成名称
     *
     * @param goodsList 待排序的货物列表
     * @param ascending true 为升序，false 为降序
     */
    public static void sortByName(List<Goods> goodsList, boolean ascending) {
        goodsList.sort(
                (g1, g2) -> ascending
                ? g1.getName().compareTo(g2.getName())
                : g2.getName().compareTo(g1.getName())
        );
    }


    public static void main(String[] args) {
        List<Goods> goodsList = new ArrayList<>(
                Arrays.asList(
                        new Goods("Keyboard", 30),
                        new Goods("Monitor", 8),
                        new Goods("Mouse", 45)
                )
        );

        sortByQuantity(goodsList, false);
        System.out.println("按数量降序：" + goodsList);   // [Mouse-45, Keyboard-30, Monitor-8]

        sortByName(goodsList, true);
        System.out.println("按名称升序：" + goodsList);   // [Keyboard-30, Monitor-8, Mouse-45]
    }
}
