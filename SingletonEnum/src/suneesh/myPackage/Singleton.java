package suneesh.myPackage;

public enum Singleton {
    INSTANCE;
    int i;

    public void show() {
        System.out.println("i = " + i);
    }
}
