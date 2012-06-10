package com.pizza.dao;

import java.util.List;
import java.util.Set;

import com.pizza.domain.Address;
import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;

public interface PizzaOrderDAO {

	public abstract void saveAddress(Address address);

	public abstract void insertOrder(PizzaOrder order);

	public abstract Address findAddressById(Long addressId);
	
	Address findAddress(Address address);

	public abstract List<PizzaSize> findActivePizzaSizes();

	public abstract Topping findToppingById(Long toppingId);

	public abstract PizzaSize findSizeById(Long sizeId);

	public abstract Set<Topping> findToppings();
	
	Set<Topping> findActiveToppings();

	// Get orders, including toppings for a certain day and address
	public abstract Set<PizzaOrder> findOrdersByAddress(Address address);

}