package com.pizza.domain;

/**
 * Domain object for Pizza Order.
 */

import java.util.Set;

import com.pizza.bean.bean.DataObject;
import com.pizza.bean.bean.EnumUserType;
import com.pizza.validation.ValidatedType;
	
public class PizzaOrder extends DataObject implements Comparable<PizzaOrder> {

	private Long id;
	private StatusName status;	
	private Address address;
	private PizzaSize size;
	private Set<Topping> toppings;
	
	// pizza order status values--
	public enum StatusName {
		ORDER_ACCEPTED, 
        PREPARING, 
        BAKED,
        DELIVERED;
		
		public static class StatusNameUserType
			extends EnumUserType<StatusName> {
			
			public StatusNameUserType() {
				super(StatusName.class);
			}
		}
    };  


	public PizzaOrder() {
		
	}
	
	// Note: constructor and setters should not be called from the presentation layer
	public PizzaOrder(Address address, PizzaSize size, Set<Topping> toppings, StatusName status) {
		
		this.address = address;
		this.size = size;
		this.status = status;
		this.toppings = toppings;
	}
	
	public void addTopping(Topping topping)
	{
		toppings.add(topping);
	}
	
	@ValidatedType(typeName = "min_num_toppings, max_num_toppings")
	public Set<Topping> getToppings()
	{
		return toppings;
	}
	
	public String getToppingsString() {
		StringBuffer topStr = new StringBuffer();
		for(Topping top: this.toppings){
			topStr.append(top.getName()+", ");
		}
		if(toppings.size() != 0){
			topStr.delete(topStr.length()-2, topStr.length()-1);
		}
		return topStr.toString();
	}
	
	public void setToppings(Set<Topping> toppings)
	{
		this.toppings = toppings;
	}

	public boolean containsTopping(Topping topping)
	{
		return toppings.contains(topping); 
	}
		
	@ValidatedType(typeName = "pizza_size_required")
	public PizzaSize getSize() 
	{
		return size;
	}

	public void setSize(PizzaSize pizzaSize) {
		this.size = pizzaSize;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Long getId() {
		return id;
	}
	
	protected void setId(final Long id) {
		this.id = id;
	}
	
	public StatusName getStatus() {
		return status;
	}
	
	public void setStatus(StatusName status) {
		this.status = status;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ORDER ID: " + getId() + "\n");
		buffer.append("SIZE: " + (getSize() != null?getSize().getName():"not available") + "\n");
		buffer.append("toppings: " + (getToppings() != null?getToppings().toString():"no toppings") + "\n");
		buffer.append("address: " + getAddress().toString() + "\n");
		buffer.append("STATUS: " + getStatus());
		return buffer.toString();
	}
	public int compareTo(PizzaOrder x)
	{
		return (new Long(id)).compareTo(new Long(x.getId()));
	}
	public Boolean equals(PizzaOrder x)
	{
		return id == x.getId();
	}


}
