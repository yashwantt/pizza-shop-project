package com.pizza.service;

import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import com.pizza.dao.AdminDAO;
import com.pizza.dao.PizzaJdbcDAO;
import com.pizza.dao.PizzaOrderDAO;
import com.pizza.domain.Topping;


public class AdminServiceUnitMockTest extends TestCase {

    private AdminDAO adminDAO;
    private PizzaOrderDAO pizzaOrderDAO;
    private PizzaJdbcDAO jdbcDAO;
    
    private AdminService adminService;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AdminServiceUnitMockTest.class);
    }

    protected void setUp() throws Exception {
        adminDAO = EasyMock.createMock(AdminDAO.class);
        pizzaOrderDAO = EasyMock.createMock(PizzaOrderDAO.class);
        jdbcDAO = EasyMock.createMock(PizzaJdbcDAO.class);
        adminService = new AdminService(adminDAO, pizzaOrderDAO, jdbcDAO);
        
    }

    /*
     * Test method for 'com.pizza.service.AdminService.addTopping(String)'
     */
    public void testAddTopping() {
    		
    	Set<Topping> expectedToppingList = new TreeSet<Topping>();
    	expectedToppingList.add(new Topping("anchovies", "meat"));
    	
    	adminDAO.createTopping("anchovies","meat");
    	EasyMock.expectLastCall().once();
    	EasyMock.expect(pizzaOrderDAO.findToppings()).andReturn(expectedToppingList).anyTimes();
    	EasyMock.replay(adminDAO);
    	EasyMock.replay(pizzaOrderDAO);
    	
        adminService.addTopping("anchovies","meat");
        Set<Topping> toppingList = adminService.getToppings();
        assertEquals(expectedToppingList.size(), toppingList.size());
        assertEquals(expectedToppingList.iterator().next().getName(), toppingList.iterator().next().getName());
        EasyMock.verify(adminDAO);
        EasyMock.verify(pizzaOrderDAO);
    }

    /*
     * Test method for 'com.pizza.logic.AdminService.getCurrentDate()'
     */
    public void testGetCurrentDate() {
    	
    	EasyMock.expect(jdbcDAO.findCurrentDate()).andReturn(1);
    	EasyMock.replay(jdbcDAO);
    	
    	
        int today = adminService.getCurrentDate();
        assertTrue(today > 0);
        EasyMock.verify(jdbcDAO);
    }

 
    public void testAdvanceDay() {
    	
    	int expLastDay = 1;
    	int expDay = 2;
    	
		EasyMock.expect(jdbcDAO.findCurrentDate()).andReturn(expLastDay);
    	jdbcDAO.advanceDay();
    	EasyMock.expectLastCall().once();
    	EasyMock.expect(jdbcDAO.findCurrentDate()).andReturn(expDay);
    	EasyMock.replay(adminDAO);
    	EasyMock.replay(jdbcDAO);
    	
        int lastDay = adminService.getCurrentDate();
        adminService.advanceDay();
        int day = adminService.getCurrentDate();
        assertTrue(day > lastDay);
        EasyMock.verify(adminDAO);
        EasyMock.verify(jdbcDAO);
    }

}
