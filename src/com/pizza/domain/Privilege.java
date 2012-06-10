package com.pizza.domain;


public class Privilege  {

	private Long id;
	private String accountId;	
	private String privilege;	
	
	public Privilege() {
	}


	
	public Privilege(String accountId, String privilege) {

		this.accountId = accountId;
		this.privilege = privilege;
	}



	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}



	public String getAccountId() {
		return accountId;
	}



	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}



	public String getPrivilege() {
		return privilege;
	}



	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	

}
