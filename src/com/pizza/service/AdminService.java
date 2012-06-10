package com.pizza.service;



import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.dao.AdminDAO;
import com.pizza.dao.PizzaJdbcDAO;
import com.pizza.dao.PizzaOrderDAO;
import com.pizza.domain.Address;
import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaOrder.StatusName;
import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;
import com.pizza.domain.Topping.ToppingType;
import com.pizza.loader.PopulatorUtils;
import com.pizza.util.LobUtils;

/**
 *  
 * 
 * This class captures the business logic for
 * baker and administration related interactions.
 */
@Transactional(propagation=Propagation.REQUIRED, readOnly=false, isolation=Isolation.SERIALIZABLE)
public class AdminService {
	
	private AdminDAO adminDAO;
	private PizzaOrderDAO pizzaOrderDAO;
	private PizzaJdbcDAO jdbcDAO;
    private final String[][] TOPPINGS = { 
    		  {"Bacon","MeatsCheese", "ico_Bacon.jpg"}
    		, {"Banana Peppers","Veggies", "ico_BananaPep.jpg"}
    		, {"Beef","MeatsCheese", "ico_Beef.jpg"}
    		, {"Black Olives", "Veggies", "ico_BlkOlives.jpg"}
    		, {"Extra Cheese","MeatsCheese", "ico_Cheese.jpg"}
    		, {"Deli Pepperoni","MeatsCheese", "ico_deli_pepperoni.jpg"}
    		, {"Grilled Chicken","MeatsCheese", "ico_GrilledCkn.jpg"}
    		, {"Green Peppers", "Veggies","ico_GrnPeppers.jpg"}
    		, {"Ham","MeatsCheese", "ico_Ham.jpg"}
    		, {"Jalapeno Peppers","Veggies", "ico_Jalapenos.jpg"}
    		, {"Baby Portabello","Veggies", "ico_Mushrooms.jpg"}
    		, {"Onions","Veggies", "ico_Onions.jpg"}
    		, {"Pepperoni","MeatsCheese", "ico_Pepperoni.jpg"}
    		, {"Pineapple","Veggies", "ico_Pineapple.jpg"}
    		, {"Sausage","MeatsCheese", "ico_Sausage.jpg"}
    		, {"Spicy Italian Sausage","MeatsCheese", "ico_SpicyItaSausage.jpg"}
    		, {"Roma Tomatoes","Veggies", "ico_Tomatoes.jpg"}
	};
    private final String[] SIZELIST = {"small", "medium", "large"};
    
    private final String[][] DEMO_ADDRESSES = {
    		{"75 State St.","Boston","MA","02109"}
    		,{"114 Winslow Dr.", "Stoughton","MA","02072"}
    		,{"207 Everret St.", "Quincy", "MA", "02170"}
    };
    
//    (Address address, PizzaSize size, Set<Topping> toppings, StatusName status)
    
	private static final String STORE_IMAGE_DIR = "images/test_images/toppings";

    
    
    public AdminService() {
    }
    
	public AdminService(AdminDAO admDAO, PizzaOrderDAO poDAO, PizzaJdbcDAO jdbcDAO) {
		adminDAO = admDAO;
		pizzaOrderDAO = poDAO;
		this.jdbcDAO = jdbcDAO;
	}
	
	public void deleteTables() {
		jdbcDAO.deleteTables();
	}
	
	public void initializeDb(String realContextPath)  {
		// wipe out pizza related data first
		jdbcDAO.deleteTables();
		
		// add default toppings
		for (int i = 0; i < TOPPINGS.length; i++) {
			addTopping(TOPPINGS[i], realContextPath);
		}
		// add default pizza sizes
		for (int i = 0; i < SIZELIST.length; i++) {
			addPizzaSize(SIZELIST[i]);
		}
		
		
		insertDemoAddresses(realContextPath);
		
	}
	
	
	public void insertDemoOrders(String realContextPath) {
		
		Random gen = new Random(19580427);
		
		StatusName[] statusNameArray = StatusName.values();
		
		for (int i = 0; i < 10; i++) {
			Long addr = Math.abs(Long.valueOf(gen.nextInt())) % DEMO_ADDRESSES.length + jdbcDAO.findMinId("address");
			Address address = pizzaOrderDAO.findAddressById(addr);
			
			Long sizeId = Math.abs(Long.valueOf(gen.nextInt())) % SIZELIST.length + jdbcDAO.findMinId("pizza_size");
			PizzaSize size = pizzaOrderDAO.findSizeById(sizeId);
			
			Set<Topping> topps = new HashSet<Topping>();
//			int numTopps = Math.abs(gen.nextInt()) % TOPPINGS.length + 1;
			int numTopps = Math.abs(gen.nextInt()) % 5 + 1;
			for(int j=0; j < numTopps; j++) {
				Long toppingId = Math.abs(Long.valueOf(gen.nextInt())) % TOPPINGS.length + jdbcDAO.findMinId("toppings");
//				Topping top = customerService.getToppingById(toppingId);
				Topping top = pizzaOrderDAO.findToppingById(toppingId); 
				topps.add(top);
			}
			
			int statusIndx = Math.abs(gen.nextInt()) % StatusName.values().length;
			
			PizzaOrder order = new PizzaOrder(address
								, size
								, topps
								, statusNameArray[statusIndx]);
			pizzaOrderDAO.insertOrder(order);
		}

	}

	public void insertDemoAddresses(String realContextPath){
		for (int i = 0; i < DEMO_ADDRESSES.length; i++) {
			addAddress(DEMO_ADDRESSES[i], realContextPath);
		}
	}
	

	public void setOrderStatus(Long ordId, StatusName statusName)  {
		try {
			adminDAO.updateOrderStatus(ordId, statusName);
		} catch (Exception e) {
			throw new ServiceException("Error in changing the order status.", e);
		}
	}	
	
	public void addAddress(String[] addr, String realConextPath)  {
		try {
			final Address address = new Address(addr[0], addr[1], addr[2], addr[3]);
			pizzaOrderDAO.saveAddress(address);
		} catch (Exception e) {
			throw new ServiceException("Topping was not added successfully: ", e);
		}
	}	
	
	
	public void addTopping(String[] name, String realConextPath)  {
		try {
			final Topping topping = new Topping(name[0], ToppingType.valueOf(name[1]));
			final File logoFile = new File(realConextPath+"/"+STORE_IMAGE_DIR, name[2]);
			topping.setLogo(PopulatorUtils.createBlob(logoFile));
				
			adminDAO.createTopping(topping);
		} catch (Exception e) {
			throw new ServiceException("Topping was not added successfully: ", e);
		}
	}

	
	public void addTopping(String name, ToppingType type, InputStream imageStream)  {
		try {
			final Topping topping = new Topping(name, type);
			topping.setLogo(LobUtils.createBlob(imageStream));
				
			adminDAO.createTopping(topping);
		} catch (Exception e) {
			throw new ServiceException("Topping was not added successfully: ", e);
		}
	}	
	
	public void addTopping(String name, ToppingType type)  {
		try {
			final Topping topping = new Topping(name, type);
			adminDAO.createTopping(topping);
		} catch (Exception e) {
			throw new ServiceException("Topping was not added successfully: ", e);
		}
	}	
	
	
	public void addPizzaSize(String name) {
		try {
			adminDAO.createPizzaSize(name);
		} catch (Exception e) {
			throw new ServiceException("Pizza size was not added successfully", e);
		}
	}
	
	public void deleteTopping(String topping) {
		try {
 		    adminDAO.deleteTopping(topping);
 		} catch (Exception e) {
			throw new ServiceException("Error while removing topping ", e);
		}		
	}
	
	public void deleteSize(String size) {
		try {
 		    adminDAO.deletePizzaSize(size);
 		} catch (Exception e) {
			throw new ServiceException("Error while removing size ", e);
		}		
	}

	
	
//	@Transactional(readOnly=true)
//	public List<PizzaOrder> getDailyReport()  {
//		try {
//	        List<PizzaOrder> orders = getTodaysOrders();
//            return orders;
//		} catch (Exception e) {
//			throw new ServiceException("Error while getting daily report ", e);
//		}
//	}
	
//	// helper method to getDailyReport and advanceDay
//	// executes inside the current transaction
//	private List<PizzaOrder> getTodaysOrders() {
//		int today = jdbcDAO.findCurrentDate();
//		return adminDAO.findOrdersByDays(today, today);
//	}

//	public List<PizzaOrder> getAdminReport()  
//	{
//		List<PizzaOrder> report;
//		try {	
//			int prevLastReportDate = jdbcDAO.findLastReportDate();
//			int today = jdbcDAO.findCurrentDate();
//		    report = adminDAO.findOrdersByDays(prevLastReportDate+1, today); 
//			if (today > prevLastReportDate) {		
//				jdbcDAO.updateLastReportDate(today);	// advamce past reported days
//			} 
//		} catch(Exception e){
//		    throw new ServiceException("Error in admin report", e);	
//		}	
//		return report;
//	}

	public int getCurrentDate() 
	{
		int date;
		try {
			date = jdbcDAO.findCurrentDate();
		} catch(Exception e) {
			throw new ServiceException("Can't access date in db: ", e);
		}
	    return date;
	}

	@Transactional(readOnly=true)
	public List<PizzaSize> getPizzaSizes()
	{
		List<PizzaSize> sizes = null;
		try {
			System.out.println("starting getPizzaSizes");
			sizes = adminDAO.findAllSizes();
		} catch (Exception e) {
			throw new ServiceException("Can't access pizza sizes in db: ", e);
		}
	   return sizes;	
	}

	
	@Transactional(readOnly=true)
	public Set<Topping> getToppings()
	{
		Set<Topping> toppings = null;
		try {
			toppings = adminDAO.findAllToppings();
		} catch(Exception e) {
			throw new ServiceException("Can't access toppings in db: ", e);
		}
		return toppings;
	}

	
	@Transactional(readOnly=true)
	public Set<PizzaOrder> getOrders()
	{
		Set<PizzaOrder> orders = null;
		try {
			orders = adminDAO.findAllOrders();
		} catch(Exception e) {
			throw new ServiceException("Can't access orders in db: ", e);
		}
		return orders;
	}
	
	
	@Transactional
	public void updatePizzaSizeActiveFlag(Long id, Boolean active){
		PizzaSize pizzaSize = adminDAO.findPizzaSize(id);
		pizzaSize.setActive(active);
	}
	
	@Transactional
	public void updatePizzaToppingActiveFlag(Long id, Boolean active){
		Topping topping = adminDAO.findPizzaTopping(id);
		topping.setActive(active);
	}
	
}
