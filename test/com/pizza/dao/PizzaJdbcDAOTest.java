// Created with eclipse new JUnit Test Case
// selected mthods: main with textUI, setup, teardown
package com.pizza.dao;

import org.springframework.test.AbstractTransactionalSpringContextTests;

import com.pizza.dao.PizzaJdbcDAOImpl;

public class PizzaJdbcDAOTest extends AbstractTransactionalSpringContextTests {
	
	public PizzaJdbcDAOTest() {}
	
	
	protected String[] getConfigLocations() {
		return new String[] {
				"file:WebContent/WEB-INF/pizza4S-data.xml"
		};
	}
	
//	// With this main, we can run the test as a Java app
//	public static void main(String[] args) {
//		junit.textui.TestRunner.run(PizzaOrderDAO1Test.class);
//	}
	
	public void testAddStuff() {
		   String[] TOPPINGS = { "Pepperoni", "Extra Cheese"};
		   String[] SIZELIST = {"small", "medium", "large"};

			PizzaJdbcDAOImpl jdbcDAO = (PizzaJdbcDAOImpl) applicationContext.getBean("pizzaJdbcDao");
			jdbcDAO.deleteTables();
			
			// add default toppings
			for (int i = 0; i < TOPPINGS.length; i++) {
				jdbcDAO.createTopping(TOPPINGS[i]);
			}
			// add default pizza sizes
			for (int i = 0; i < SIZELIST.length; i++) {
				jdbcDAO.createPizzaSize(SIZELIST[i]);
			}
			
			assertEquals(TOPPINGS.length, jdbcDAO.findToppings().size());
			assertEquals(SIZELIST.length, jdbcDAO.findPizzaSizes().size());
			
	}
}
