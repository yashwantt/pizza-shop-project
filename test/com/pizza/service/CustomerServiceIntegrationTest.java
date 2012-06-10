package com.pizza.service;



import java.util.LinkedList;
import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.pizza.domain.Address;
import com.pizza.domain.PizzaOrder;
import com.pizza.mvc.customer.PizzaOrderCommand;

public class CustomerServiceIntegrationTest extends AbstractDependencyInjectionSpringContextTests {


    protected String[] getConfigLocations() {
    	return new String[] {
    			"file:WebContent/WEB-INF/pizza4S-data.xml"
    			, "file:WebContent/WEB-INF/pizza4S-service.xml"
    	};
    }

    
//    public void testDupPizzaSize() {
//      AdminService adminService = (AdminService) applicationContext.getBean("adminService");
//      CustomerService customerService = (CustomerService) applicationContext.getBean("customerService");
//    	
//      adminService.initializeDb();
//      
//      adminService.addPizzaSize("small");
//      
//      System.out.println(adminService.getPizzaSizes());
//    }
    
    
//    public void testInsertAndFind() {
//        AdminService adminService = (AdminService) applicationContext.getBean("adminService");
//        CustomerService customerService = (CustomerService) applicationContext.getBean("customerService");
//        
//        adminService.initializeDb();
//        
////        [sql] ID,SIZE_NAME
////        [sql] 1,small
////        [sql] 2,medium
////        [sql] 3,large
////        [sql] 0 rows affected
////        [sql] ID,TOPPING_NAME
////        [sql] 1,Pepperoni
////        [sql] 2,Extra Cheese
////        [sql] 0 rows affected
////        [sql] ID,ADDRESS_ID,SIZE_ID,DAY,STATUS
////        [sql] 1,3,2,1,1
////        [sql] 0 rows affected
////        [sql] ORDER_ID,TOPPING_ID
////        [sql] 1,1
////        [sql] 1,2
////        [sql] 0 rows affected
////        [sql] ID,CURRENT_DAY,LAST_REPORT
////        [sql] 1,1,1
//        
//        PizzaOrderCommand command = new PizzaOrderCommand();
//        command.setAddressNumber(3);
//        command.setChosenSizeId("2");
//        List<String> topIds = new LinkedList<String>();
//        topIds.add("1");
//        topIds.add("2");
//        command.setChosenToppingIds(topIds);
//        
//
//        customerService.makeOrder(command);
//        
//        List<PizzaOrder> orders = customerService.getOrderDetails(3);
//        for(PizzaOrder order: orders) {
//        	System.out.println(order);
//        }
//        
//        
//    }
    
    
    
    public void testInsertAddressAndOrderThenFind() {
        AdminService adminService = (AdminService) applicationContext.getBean("adminService");
        CustomerService customerService = (CustomerService) applicationContext.getBean("customerService");
        
        adminService.initializeDb("WebContent");
        

		Address address = new Address("75 state st", "boston", "MA", "02109");
		customerService.saveAddress(address);

		Long addressNum = address.getId();
		int addressNumInt = addressNum.intValue();
		
//        [sql] ID,SIZE_NAME
//        [sql] 1,small
//        [sql] 2,medium
//        [sql] 3,large
//        [sql] 0 rows affected
//        [sql] ID,TOPPING_NAME
//        [sql] 1,Pepperoni
//        [sql] 2,Extra Cheese
//        [sql] 0 rows affected
//        [sql] ID,ADDRESS_ID,SIZE_ID,DAY,STATUS
//        [sql] 1,3,2,1,1
//        [sql] 0 rows affected
//        [sql] ORDER_ID,TOPPING_ID
//        [sql] 1,1
//        [sql] 1,2
//        [sql] 0 rows affected
//        [sql] ID,CURRENT_DAY,LAST_REPORT
//        [sql] 1,1,1
        
        PizzaOrderCommand command = new PizzaOrderCommand();
        command.setAddressNumber(addressNumInt);
        command.setChosenSizeId("2");
        List<String> topIds = new LinkedList<String>();
        topIds.add("1");
        topIds.add("2");
        command.setChosenToppingIds(topIds);
        

        customerService.makeOrder(command);
        
        List<PizzaOrder> orders = customerService.findOrdersByAddress(addressNumInt);
        for(PizzaOrder order: orders) {
        	System.out.println(order);
        }
        
        
    }
    
    
    
    
//    public void testToppingLogo() {
//    	AdminService adminService = (AdminService) applicationContext.getBean("adminService");
//        
//        adminService.initializeDb();
//      	
//      }
//			

}
