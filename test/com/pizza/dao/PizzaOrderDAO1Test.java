// Created with eclipse new JUnit Test Case
// selected mthods: main with textUI, setup, teardown
package com.pizza.dao;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import org.springframework.test.AbstractTransactionalSpringContextTests;

import com.pizza.dao.AdminDAOImpl;
import com.pizza.dao.PizzaJdbcDAOImpl;
import com.pizza.domain.*;

public class PizzaOrderDAO1Test extends AbstractTransactionalSpringContextTests {
	
	public PizzaOrderDAO1Test() {}
	private boolean firstTest = true;
	private PizzaJdbcDAOImpl jdbcDAO;
	
	protected String[] getConfigLocations() {
		return new String[] {
				"file:WebContent/WEB-INF/pizza4S-data.xml"
		};
	}
	
	protected void onSetUp() {
		if (firstTest) {
			jdbcDAO = (PizzaJdbcDAOImpl) applicationContext.getBean("pizzaJdbcDao");
			jdbcDAO.deleteTables();
			firstTest = false;
		}
	}
//	// With this main, we can run the test as a Java app
//	public static void main(String[] args) {
//		junit.textui.TestRunner.run(PizzaOrderDAO1Test.class);
//	}

//	public void testFindOrders()
//	{
//		PizzaOrderDAO pizzaOrderDAO = (PizzaOrderDAO) applicationContext.getBean("pizzaOrderDao");
//
//		List<PizzaOrder> orders = pizzaOrderDAO.findOrdersByAddress(5, 1);
//		assertTrue(orders.isEmpty());
//	}
//	
	
	public void testInsertOrder()
	{
		AdminDAOImpl adminDAO = (AdminDAOImpl) applicationContext.getBean("adminDao");
		adminDAO.createPizzaSize("small");
		adminDAO.createPizzaSize("small");
		
//		PizzaOrderDAO pizzaOrderDAO = (PizzaOrderDAO) applicationContext.getBean("pizzaOrderDao");
//
//		Address address = new Address("75 state st", "boston", "MA", "02109");
//		pizzaOrderDAO.saveAddress(address);
//
//		jdbcDAO.createPizzaSize("small");
//		jdbcDAO.createTopping("mushrooms");
//		
//		
//		PizzaSize size = pizzaOrderDAO.findPizzaSizes().iterator().next();
//		
//		Set<Topping> toppings = new TreeSet<Topping>(pizzaOrderDAO.findToppings());		
//
//
//		PizzaOrder order = new PizzaOrder(address, size, toppings, 1, PizzaOrder.PREPARING);
//		pizzaOrderDAO.insertOrder(order);			

	}
}
