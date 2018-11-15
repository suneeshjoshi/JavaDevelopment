package com.mavenSpring.SpringProject1;

public class Address {
String street;
String city;
String country;
public String getStreet() {
	return street;
}
public void setStreet(String street) {
	this.street = street;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
	System.out.println("Address:Setter");
}
@Override
public String toString() {
	return "Address [street=" + street + ", city=" + city + ", country=" + country + "]";
}

public Address() {
}

public Address(String street, String city, String country) {
	super();
	this.street = street;
	this.city = city;
	this.country = country;
	System.out.println("Address:CTOR");
}

public void myAddressInitMethod() {
	System.out.println("Address::myInitMethod called");
}

public void myAddressDestroyMethod() {
	System.out.println("Address::myDestroyMethod called");
}


}
