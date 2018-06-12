package com.library.luo.android;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = -4626445234327820773L;
    private String name=null;
    private String password=null;
    private String location=null;
 

	//Ĭ�Ϲ��캯��
	public User() {
		super();
	}
	
	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	//Set��get����
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
    
	 
}
