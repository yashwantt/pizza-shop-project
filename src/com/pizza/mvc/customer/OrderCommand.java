package com.pizza.mvc.customer;

import java.util.HashSet;
import java.util.Set;

import com.pizza.bean.bean.DataObject;
import com.pizza.domain.Address;
import com.pizza.domain.PizzaOrder;
import com.pizza.validation.BeanMap;

public class OrderCommand extends DataObject {
	
	String pizzaSizeId;
	Set<String> pizzaToppingIds;
	
	String streetAddress;
	String city;
	String state;
	String zip;
	
	public OrderCommand() {
		pizzaToppingIds = new HashSet<String>();
	}
	
	@BeanMap(targetClass = PizzaOrder.class, propertyName = "size")
	public String getPizzaSizeId() {
		return pizzaSizeId;
	}
	public void setPizzaSizeId(String pizzaSizeId) {
		this.pizzaSizeId = pizzaSizeId;
	}
	
	@BeanMap(targetClass = PizzaOrder.class, propertyName = "toppings")
	public Set<String> getPizzaToppingIds() {
		return pizzaToppingIds;
	}
	
	public void setPizzaToppingIds(Set<String> pizzaToppingIds) {
		this.pizzaToppingIds = pizzaToppingIds;
	}
	
	@BeanMap(targetClass = Address.class, propertyName = "streetAddress")
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	@BeanMap(targetClass = Address.class, propertyName = "city")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	@BeanMap(targetClass = Address.class, propertyName = "state")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	@BeanMap(targetClass = Address.class, propertyName = "zip")
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	

}
