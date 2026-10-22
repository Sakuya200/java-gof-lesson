package lesson3;

/**
 * 迪米特法则正例--狗与主人
 * @author Sakuya200
 * @since 2026/10/21
 */
public class DemeterExampleV2 {

    /**
     * 狗腿
     */
    static class Legs {
        private int stepCount = 0;

        public void walk() { stepCount++; }
        public int getStepCount() { return stepCount; }
    }

    /**
     * 狗，拥有狗腿
     */
    public class Dog {
        private final Legs legs = new Legs();
        private final String name;

        public Dog(String name) { this.name = name; }

        /** 对外只承诺"我会走"，不承诺"我用什么走" */
        public void walk() {
            legs.walk();                 // 这里的 legs 是 Dog 的直接组成对象 → 合法
        }

        public int getStepCount() {
            return legs.getStepCount();  // 同上，一个点号
        }

        public String getName() { return name; }

        // getLegs() 被删除：内部结构只有 Dog 自己知道
    }


    /**
     * 主人，拥有狗
     */
    public class Owner {
        public void walkTheDog(Dog dog) {
            dog.walk();                                            // 一个点号，dog 是 M 的参数，合法
            System.out.println(dog.getName() + " 走了 "
                    + dog.getStepCount() + " 步");                  // 仍是一个点号
        }
    }


}
