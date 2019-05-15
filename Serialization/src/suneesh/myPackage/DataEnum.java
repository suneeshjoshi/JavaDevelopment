package suneesh.myPackage;

import java.io.Serializable;

public enum DataEnum implements Serializable {
    INSTANCE;

    private int age;
    transient private String name = "";

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DataEnum{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

}
