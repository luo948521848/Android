package com.library.luo.android;

import java.io.Serializable;

public class Book implements Serializable{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 5067618009013494544L;
	private String bookid=null;
	private String bookname=null;
	private String bookwriter=null;
	private double bookprice=0;
	
	
	public Book()
	{
		
	}
	
	//������set��get����
	public String getBookid() {
		return bookid;
	}
	public void setBookid(String bookid) {
		this.bookid = bookid;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public String getBookwriter() {
		return bookwriter;
	}
	public void setBookwriter(String bookwriter) {
		this.bookwriter = bookwriter;
	}
	public double getBookprice() {
		return bookprice;
	}
	public void setBookprice(double bookprice) {
		this.bookprice = bookprice;
	}
	
	
	
	

}
