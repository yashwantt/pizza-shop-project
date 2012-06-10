package com.pizza.service;



import java.util.Set;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import com.pizza.domain.Topping;

public class AdminServiceIntegrationTest 
	extends AbstractTransactionalDataSourceSpringContextTests 
	{

    private AdminService adminService;

    protected String[] getConfigLocations() {
    	return new String[] {
    			"file:WebContent/WEB-INF/pizza4S-data.xml"
    			, "file:WebContent/WEB-INF/pizza4S-service.xml"
    	};
    }

//    protected void onSetUp() {
//      PizzaJdbcDAO jdbcDAO = (PizzaJdbcDAO) applicationContext.getBean("pizzaJdbcDao");
//      adminService = (AdminService) applicationContext.getBean("adminService");
//      
//      jdbcDAO.deleteTables();
//      int numTopps = jdbcTemplate.queryForInt("select count(*) from " + TOPPING_TABLE);
//      int numSizes = jdbcTemplate.queryForInt("select count(*) from " + PIZZA_SIZE_TABLE);
//      assertEquals(0, numTopps);
//      assertEquals(0, numSizes);
//
//      // insert a topping and size
//      jdbcDAO.createTopping("extra cheese");
//      jdbcDAO.createPizzaSize("small");
//      numTopps = jdbcTemplate.queryForInt("select count(*) from " + TOPPING_TABLE);
//      numSizes = jdbcTemplate.queryForInt("select count(*) from " + PIZZA_SIZE_TABLE);
//      assertEquals(1, numTopps);
//      assertEquals(1, numSizes);
//    }
//    /*
//     * Test method for 'com.pizza.logic.AdminService.removeTopping(String)'
//     */
//    public void testRemoveTopping() {
//    	
//        adminService.deleteTopping("extra cheese");
//        Set<Topping> toppingList = adminService.getToppings();
//        assertTrue(toppingList.size() == 0);
//        
//        int numTopps = jdbcTemplate.queryForInt("select count(*) from " + TOPPING_TABLE);
//        int numSizes = jdbcTemplate.queryForInt("select count(*) from " + PIZZA_SIZE_TABLE);
//        assertEquals(0, numTopps);
//        assertEquals(1, numSizes);
//        
//    }
//    /*
//     * Test method for 'com.pizza.logic.AdminService.removeTopping(String)'
//     */
//    public void testRemoveSize() {
//            adminService.deleteSize("small");
//            List<PizzaSize> sizeList = adminService.getPizzaSizes();
//            assertTrue(sizeList.size() == 0);
//            
//            int numTopps = jdbcTemplate.queryForInt("select count(*) from " + TOPPING_TABLE);
//            int numSizes = jdbcTemplate.queryForInt("select count(*) from " + PIZZA_SIZE_TABLE);
//            assertEquals(1, numTopps);
//            assertEquals(0, numSizes);
//
//    }
    
//    public void testOrderReport() {
//        adminService = (AdminService) applicationContext.getBean("adminService");
//
//		Set<PizzaOrder> allOrders = adminService.getOrders();
//		
//		for(PizzaOrder order: allOrders) {
//			System.out.println(order);
//		}
//		
//		
//		JSONObject ordersJson = new JSONObject();
//		ordersJson.element("identifier", "id");
//		ordersJson.element("label", "day");
//
//		JSONArray ordersJsonArray = new JSONArray();
//		for(PizzaOrder order: allOrders) {
//			JSONObject orderJson = new JSONObject()
//									.element("id", order.getId())
//									.element("day", order.getDay())
//									.element("addressNumber", order.getAddress().getId())
//									.element("statusString", order.getStatusString())
//									.element("toppings", order.getToppingsString());
//			ordersJsonArray = ordersJsonArray.element(orderJson);
//		}		
//		
//		ordersJson.element("items", ordersJsonArray);
//		
//		System.out.println(ordersJson);
//    	
//    }
    
  public void testToppingsJson() {
  CustomerService customerService = (CustomerService) applicationContext.getBean("customerService");

	Set<Topping> allToppings = customerService.getToppings();
	
	JSON topsJson = buildToppingsJson(allToppings);
	
	System.out.println(topsJson);
	
  }
   
  
	private JSON buildToppingsJson(Set<Topping> tops) {
		JSONArray topsArray = new JSONArray();
		for(Topping top: tops) {
			JSONObject toppingJson = new JSONObject()
									.element("id", top.getId())
									.element("toppingName", top.getName());
			topsArray.element(toppingJson);
		}
		return topsArray;
		
	}
  
  
}
