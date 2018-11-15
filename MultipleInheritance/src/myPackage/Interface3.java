package myPackage;

public interface Interface3 extends Interface1, Interface2{
		default void log() {
			System.out.println("Interface3:log()");
		}
}
