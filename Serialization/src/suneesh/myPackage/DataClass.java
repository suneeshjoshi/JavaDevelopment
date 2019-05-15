package suneesh.myPackage;

import java.io.Serializable;

public class DataClass implements Serializable {
    private static DataClass obj;
    int age;

    private DataClass() {
        System.out.println("Private CTOR");
    }

    public static DataClass getInstance() {
        if (obj == null) {
            synchronized (DataClass.class) {
                if (obj == null) {
                    obj = new DataClass();
                }
            }
        }
        return obj;
    }

    int getAge() {
        return age;
    }
}
