package builderPattern;

public class Computer {
	// mandatory
	private int RAM;
	private int HDD;

	String CPU;
	
	// optional
	private boolean isBluetoothEnabled;
	
	public int getRAM() {
		return RAM;
	}

	public int getHDD() {
		return HDD;
	}

	public boolean isBluetoothEnabled() {
		return isBluetoothEnabled;
	}


	private Computer(ComputerBuilder computerBuilder) {
		this.RAM=computerBuilder.RAM;
		this.HDD = computerBuilder.HDD;
		this.isBluetoothEnabled = computerBuilder.isBluetoothEnabled;
	}

	public static class ComputerBuilder{
		// mandatory
		private int RAM;
		private int HDD;
		
		// optional
		private boolean isBluetoothEnabled;
		
		public ComputerBuilder(int RAM , int HDD) {
			this.HDD = HDD;
			this.RAM = RAM;
		}
		
		public ComputerBuilder setIsBluetoothEnabled(boolean state) {
			this.isBluetoothEnabled=state;
			return this;
		}
 		
		public Computer build() {
			if( ( RAM < 0) && ( HDD < 0 ) )
				throw new IllegalStateException("RAM or HDD having in valid values.");
			return new Computer(this);
		}
	}

	@Override
	public String toString() {
		return "Computer [RAM=" + getRAM() + ", HDD=" + getHDD() + ", isBluetoothEnabled=" + isBluetoothEnabled() + "]";
	}
	
	
}
