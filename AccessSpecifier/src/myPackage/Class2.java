package myPackage;

public class Class2 extends Class1 {


	public void log() {
		System.out.println("log()");
		Class2 obj  = new Class2();
		obj.a=10; //Default
//		obj.b=20; // Private 
		obj.c=30; // Protected
		obj.d=40; // Public
		
	}

	@Override
	public String toString() {
		return "Class2 [a=" + a + ", c=" + c + ", d=" + d + "]";
	}

}
