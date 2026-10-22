package lesson3;

/**
 * 迪米特法则反例--狗与主人
 * @author Sakuya200
 * @since 2026/10/21
 */
public class DemeterExample {

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
    static class Dog {
        private final Legs legs = new Legs();
        private final String name;

        public Dog(String name) { this.name = name; }

        public Legs getLegs() { return legs; }   // 病根在这里：把内部结构交给了调用方
        public String getName() { return name; }
    }


    /**
     * 主人，拥有狗
     */
    static class Owner {
        /** 明显违反了迪米特法则，出现了两个点号，即跨对象调用 Owner.Dog.Leg */
        public void walkTheDog(Dog dog) {
            dog.getLegs().walk();                                  // 两个点号
            System.out.println(dog.getName() + " 走了 "
                    + dog.getLegs().getStepCount() + " 步");        // 又是两个点号
        }
    }


}
