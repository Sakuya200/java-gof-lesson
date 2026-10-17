package lesson1.version2.oop;

import java.util.Scanner;

/// 面向对象计算器示例
public class OopExample {

    /// 启动计算器交互程序
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            System.out.println("Result: " + calculator.calculate(input));
        }
    }
}
