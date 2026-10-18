package lesson2;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 货物类
 * @author Sakuya200
 * @since 2026/10/18
 */
@Data
@AllArgsConstructor
public class Cargo {

    /**
     * 货物名称
     */
    private String name;

    /**
     * 货物数量
     */
    private Integer count;


    /**
     * 线程安全方法：添加货物量
     * @param count Integer
     */
    public synchronized void addCount(Integer count) {
        this.count += count;
    }

    /**
     * 线程安全方法：减少货物量
     * @param count Integer
     */
    public synchronized void remCount(Integer count) {
        this.count -= count;
    }

    public static void main(String[] args) {
        Cargo cargo = new Cargo("apple", 20);
        // 正确使用方法
        cargo.addCount(20);
        System.out.println(cargo.count);
        cargo.remCount(10);
        System.out.println(cargo.count);

        // 错误使用方法：不看类的源码的结果
        cargo.setCount(cargo.getCount() + 20);
        System.out.println(cargo.count);
        cargo.setCount(cargo.getCount() - 10);
        System.out.println(cargo.count);
    }
}
