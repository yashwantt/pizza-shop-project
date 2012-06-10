package com.pizza.dao;

import java.util.List;
import java.util.Set;

import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaSize;
import com.pizza.domain.PizzaOrder.StatusName;
import com.pizza.domain.Topping;
import com.pizza.domain.Topping.ToppingType;


public interface AdminDAO {

	public abstract void createTopping(Topping topping);

	public abstract void createPizzaSize(String sizeName);

	public abstract void deleteTopping(String toppingName);

	public abstract void deletePizzaSize(String sizeName);

//	// find first order with specified status
//	public abstract Long findFirstOrderId(final int status);

	public abstract void updateOrderStatus(final Long ordNo, final StatusName newStatus);

//	// get all orders, without toppings, between day1 and day2 (inclusive)
//	public abstract List<PizzaOrder> findOrdersByDays(int day1, int day2);
	
	public abstract Set<PizzaOrder> findAllOrders();

	public PizzaSize findPizzaSize(Long id);

	Set<Topping> findAllToppings();

	List<PizzaSize> findAllSizes();

	Topping findPizzaTopping(Long id);

	void createTopping(String toppingName, ToppingType type);
}