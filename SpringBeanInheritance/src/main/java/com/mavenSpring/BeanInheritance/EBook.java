package com.mavenSpring.BeanInheritance;

public class EBook extends Book{

	int sizeInMB;

	public int getSizeInMB() {
		return sizeInMB;
	}

	public void setSizeInMB(int sizeInMB) {
		this.sizeInMB = sizeInMB;
	}

	public EBook() {
		super();
	}

	public EBook(int sizeInMB) {
		super();
		this.sizeInMB = sizeInMB;
	}

	@Override
	public String toString() {
		return "EBook [sizeInMB=" + sizeInMB + "]"+ super.toString();
	}
}
