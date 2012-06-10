package com.pizza.domain;


public class Account  {

	private Long id;
	private String userName;	
	private String password;	
	
	public Account() {
	}

	public Account(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
