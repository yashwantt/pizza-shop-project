package com.pizza.domain;

import java.sql.Blob;

import com.pizza.bean.bean.EnumUserType;

// Domain object for a pizza topping
public class Topping implements Comparable<Topping> {
	private Long id;
//	private int version;
	private String name;
	private ToppingType type;
	
	public enum ToppingType {
		MeatsCheese, 
        Veggies;
		
		public static class ToppingTypeUserType
			extends EnumUserType<ToppingType> {
			
			public ToppingTypeUserType() {
				super(ToppingType.class);
			}
		}
    };  

	
	private Blob logo;
	
	private Boolean active;

	public Topping() {
		
	}
	
	public Topping(String name, ToppingType type)
	{
		setName(name);
		setType(type);
		setActive(true);
	}
	
	public Topping(String name, ToppingType type, Boolean active)
	{
		setName(name);
		setType(type);
		setActive(active);
	}

//	/**
//	 * Get the version used for optimistic locking.
//	 *
//	 * @return the current version
//	 */
//	public int getVersion() {
//		return version;
//	}
	
	
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
		this.name = name;
	}

	public int compareTo(Topping x)
	{
		return (new Long(id)).compareTo(new Long(x.getId()));
	}
	public Boolean equals(Topping x)
	{
		return id == x.getId();
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Topping [id=").append(id).append(", name=")
				.append(name).append("]");
		return builder.toString();
	}


	public Blob getLogo() {
		return logo;
	}


	public void setLogo(Blob logo) {
		this.logo = logo;
	}


	public ToppingType getType() {
		return type;
	}


	public void setType(ToppingType type) {
		this.type = type;
	}



	public Boolean getActive() {
		return active;
	}



	public void setActive(Boolean active) {
		this.active = active;
	}

}
