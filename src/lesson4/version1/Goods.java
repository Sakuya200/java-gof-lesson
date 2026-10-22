package lesson4.version1;

/**
 * 货物对象
 *
 * @author Sakuya200
 * @since 2026/10/22
 */
public class Goods {
    private final String name;      // 货物名称
    private final int quantity;     // 货物数量

    public Goods(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name + "-" + quantity;
    }
}

