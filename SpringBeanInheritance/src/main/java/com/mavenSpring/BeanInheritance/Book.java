package com.mavenSpring.BeanInheritance;

public class Book {
String name;
String author;
int noOfPages;
float cost;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public int getNoOfPages() {
	return noOfPages;
}
public void setNoOfPages(int noOfPages) {
	this.noOfPages = noOfPages;
}
public float getCost() {
	return cost;
}
public void setCost(float cost) {
	this.cost = cost;
}
@Override
public String toString() {
	return "Book [name=" + name + ", author=" + author + ", noOfPages=" + noOfPages + ", cost=" + cost + "]";
}

public Book() {
}

public Book(String name, String author, int noOfPages, int cost) {
	super();
	this.name = name;
	this.author = author;
	this.noOfPages = noOfPages;
	this.cost = cost;
}

}
