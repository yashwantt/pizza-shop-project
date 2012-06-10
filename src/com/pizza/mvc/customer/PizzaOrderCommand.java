package com.pizza.mvc.customer;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Command object for Pizza Order Form.
 */

	
public class PizzaOrderCommand  {
	private String chosenSizeId;
	
	private List<String> chosenToppingIds;	
	
	// for addressNumber select list--
	private int addressNumber;

	public PizzaOrderCommand() {
		chosenToppingIds = new LinkedList<String>();
	}
		

	public int getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(int addressNumber)
	{
	   this.addressNumber = addressNumber;	
	}	

	public String getChosenSizeId() 
	{
		return chosenSizeId;
	}
	
	public void setChosenSizeId(String sizeId) {
		this.chosenSizeId = sizeId;
	}

	public List<String> getChosenToppingIds() {
		return chosenToppingIds;
	}
	
	public void setChosenToppingIds(List<String> toppingIds) {
		this.chosenToppingIds = toppingIds;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SIZE: " + getChosenSizeId());
		buffer.append("ADDRESS NUMBER: " + getAddressNumber() + "\n");
		return buffer.toString();
	}

}
