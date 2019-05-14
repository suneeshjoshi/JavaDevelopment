package suneesh.myPackage;

public class Main {

    public static void main(String[] args) {
        System.out.println("Singleton using ENUM");

        Singleton obj1 = Singleton.INSTANCE;
        obj1.i = 10;
        obj1.show();

        Singleton obj2 = Singleton.INSTANCE;
        obj2.i = 11;
        obj1.show();

    }
}
