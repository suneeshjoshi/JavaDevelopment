package com.mavenSpring.SpringProject1;

public class Employee {

	String name;
	Address address;
	int age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		System.out.println("Employee()::setName()");
		this.name = name;
	}
	public Address getAddress() {
		return address;
	}
	
	// Setter Dependency Injection
	public void setAddress(Address address) {
		this.address = address;
		System.out.println("Employee::Using Setter method for Address ");
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		System.out.println("Employee()::setAge()");
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "Employee [name=" + name + ", address=" + address + ", age=" + age + "]";
	}
	
	public Employee() {
	}

	// Constructor Dependency Injection 
	public Employee(Address address) {
		System.out.println("Employee::CTOR Called");
		this.address = address;
	}
	
	
	public Employee(String name, Address address, int age) {
		super();
		this.name = name;
		this.address = address;
		this.age = age;
		System.out.println("Employee::CTOR Called");
	}

	public void myInitMethod() {
		System.out.println("Employee::myInitMethod called");
	}

	public void myDestroyMethod() {
		System.out.println("Employee::myDestroyMethod called");
	}
	
}
