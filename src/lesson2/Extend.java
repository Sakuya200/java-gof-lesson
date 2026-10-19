package lesson2;

/**
 * Java 的继承语法
 * @author Sakuya200
 * @since 2026/10/19
 */
public class Extend {


     static class Child extends Father {
        public Child(String name) {
            super(name);
        }

        @Override
        public void speak() {
            System.out.println("My name is " + name + ", I'm a child.");
        }
    }



    static class Father {
        protected String name;

        public Father(String name) {
            this.name = name;
        }

        public void speak() {
            System.out.println("My name is " + name + ", I'm a father.");
        }
    }

    public static void main(String[] args) {
        Father father = new Extend.Father("John");
        father.speak();
        Child child = new Extend.Child("Mike");
        child.speak();
    }
}
