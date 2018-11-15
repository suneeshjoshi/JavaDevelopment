package myPackage;

public interface Interface1 {

	default void log() {
		System.out.println("Interface1:log()");
	}
	
}
