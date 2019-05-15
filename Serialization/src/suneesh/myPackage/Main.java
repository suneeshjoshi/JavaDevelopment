package suneesh.myPackage;

import java.io.*;

public class Main {

    private static void writeDoubleLockingSingleton() {
        DataClass obj = DataClass.getInstance();
        obj.age = 10;
        System.out.println(obj.getAge());

        File f = new File("datastore.txt");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(obj);

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));

            DataClass obj1 = (DataClass) ois.readObject();

            System.out.println("Data read back = " + obj1.age);

            if (obj.equals(obj1)) {
                System.out.println("Object equal" + obj.hashCode() + "/" + obj1.hashCode());
            } else {
                System.out.println("Object Not equal" + obj.hashCode() + "/" + obj1.hashCode());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void writeEnumSingleton() {
        DataEnum obj = DataEnum.INSTANCE;

        obj.setAge(10);
        obj.setName("Suneesh");

        System.out.println("Data back = " + obj.toString());

        File f = new File("datastore2.txt");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(obj);

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));

            DataEnum obj1 = (DataEnum) ois.readObject();

            System.out.println("Data read back = " + obj1.toString());

            if (obj.equals(obj1)) {
                System.out.println("Object equal" + obj.hashCode() + "/" + obj1.hashCode());
            } else {
                System.out.println("Object Not equal" + obj.hashCode() + "/" + obj1.hashCode());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void writeEnumSingletonExternalizable() {
        DataEnumExternalizable obj = DataEnumExternalizable.INSTANCE;

        obj.setAge(10);
        obj.setName("Suneesh");

        System.out.println("Data back = " + obj.toString());

        File f = new File("datastore2.txt");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(obj);

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));

            DataEnumExternalizable obj1 = (DataEnumExternalizable) ois.readObject();

            System.out.println("Data read back = " + obj1.toString());

            if (obj.equals(obj1)) {
                System.out.println("Object equal" + obj.hashCode() + "/" + obj1.hashCode());
            } else {
                System.out.println("Object Not equal" + obj.hashCode() + "/" + obj1.hashCode());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("Serializing Singleton objects ");
//        Main.writeDoubleLockingSingleton();
//        Main.writeEnumSingleton();
        Main.writeEnumSingletonExternalizable();

    }
}
