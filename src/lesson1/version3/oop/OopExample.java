package lesson1.version3.oop;

import java.util.Scanner;

/** 支持函数调用的面向对象计算器示例 */
public class OopExample {

    /** 启动计算器交互程序 */
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.trim().startsWith("def ")) {
                calculator.defineFunction(input);
                System.out.println("Function defined");
            } else {
                System.out.println("Result: " + calculator.calculate(input));
            }
        }
    }
}
