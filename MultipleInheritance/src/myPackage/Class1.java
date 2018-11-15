package myPackage;

public class Class1 implements Interface3{
	
	@Override
	public void log() {
		System.out.println("Class1:log() : Start");
	}
	
public static void main(String[] args) {
	System.out.println("main()");
	Class1 obj = new Class1();
	obj.log();
	
}
}
