package com.pizza.domain;

import com.pizza.util.StringUtils;

public class PizzaSize implements Comparable<PizzaSize> {
	private Long id;
	private String name;
	private Boolean active;
	
	public PizzaSize() {
		
	}
	
	// Note: constructor and setters should not be called from the presentation layer
	public PizzaSize(String name)
	{
		this.setName(name);
		this.setActive(true);
	}
	
	public PizzaSize(String name, Boolean active) {
		this.setName(name);
		this.setActive(active);
	}
	
	public Long getId() {
		return id;
	}
	
	protected void setId(final Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = StringUtils.makeProper(name);
	}
	
	public int compareTo(PizzaSize x)
	{
		return (new Long(id)).compareTo(new Long(x.getId()));
	}
	
	public Boolean equals(PizzaSize x)
	{
		return id == x.getId();
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
