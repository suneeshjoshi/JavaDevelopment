package package1;

class Class2 {

	Class2(){
		System.out.println("Class2::CTOR");
	}

}

public class Class1 extends Class2{
	static
	{
		System.out.println("Class1 Static Block");
	}
	
	{
		System.out.println("Instance Initiazlise Block");
	}
	
	Class1(){
		System.out.println("Class1::CTOR");
		this.prObj = new Class3();
	}

	class Class3 {

		Class3(){
			System.out.println("Class3::CTOR");
		}

	}

	private Class3 prObj;
	
	public static void main(String[] args) {
		System.out.println("Class1 : main()");
		Class1 obj = new Class1();
		
	}

}
