package builderPattern;

public class MainClass {

	public static void main(String[] args) {
		
		Computer com = new Computer.ComputerBuilder(10, 20).build();
		System.out.println(com.toString());
		
		Computer com2 = new Computer.ComputerBuilder(10, 20).setIsBluetoothEnabled(true).build();
		System.out.println(com2.toString());
		
	}

}
