package com.pizza.domain;

import java.io.IOException;

import com.pizza.bean.bean.DataObject;
import com.pizza.util.StringUtils;
import com.pizza.validation.ValidatedType;


public class Address extends DataObject {

	private Long id;
	private String streetAddress;	
	private String city;	
	private String state;
	private String zip;

	public Address() {
		
	}
	
	// Note: constructor and setters should not be called from the presentation layer
	
	public Address(String streetAddress, String city, String state, String zip) {
		this.setStreetAddress(streetAddress);
		this.setCity(city);
		this.setState(state);
		this.setZip(zip);
	}

	public Long getId() {
		return id;
	}
	
	protected void setId(final Long id) {
		this.id = id;
	}
	
	private String cleanUpString(String input) {
		// trim whitespace from the ends and collapse the whitespace in the middle to only one
		String trimmedEnds = input.toLowerCase().trim();
		String result = trimmedEnds.replaceAll("\\s{2,}", " ");
		return result;

	}

	@ValidatedType(typeName = "required, street_address")
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = cleanUpString(streetAddress);
	}

	@ValidatedType(typeName = "required, city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = cleanUpString(city);
	}

	@ValidatedType(typeName = "required, stateCode")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = cleanUpString(state);
	}

	@ValidatedType(typeName = "required, zipCode")
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = cleanUpString(zip);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result
				+ ((streetAddress == null) ? 0 : streetAddress.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (streetAddress == null) {
			if (other.streetAddress != null)
				return false;
		} else if (!streetAddress.equals(other.streetAddress))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Address [id=").append(id).append(", streetAddress=")
				.append(streetAddress).append(", city=").append(city)
				.append(", state=").append(state).append(", zip=").append(zip)
				.append("]");
		return builder.toString();
	}
	
	public String toUIString() throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(StringUtils.makeProper(streetAddress)).append(", ")
			   .append(StringUtils.makeProper(city)).append(", ").append(state.toUpperCase()).append(", ").append(zip);
		
		return builder.toString();
	}
	
	
	
//	public static void main(String[] args) {
//		String input = "    75    State					     st.  ";
//		String output = cleanUpString(input);
//		System.out.println("input: " + input );
//		System.out.println("output: " + output);
//	}
	
}
