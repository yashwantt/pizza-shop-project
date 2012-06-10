package com.pizza.service;



import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.dao.PizzaJdbcDAO;
import com.pizza.dao.PizzaOrderDAO;
import com.pizza.domain.Address;
import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaOrder.StatusName;
import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;
import com.pizza.mvc.customer.OrderCommand;
import com.pizza.util.PresentationUtils;

/**
 *  
 * 
 * This class captures the business logic for customer interactions.
 */

@Transactional(propagation=Propagation.REQUIRED, readOnly=false, isolation=Isolation.SERIALIZABLE)
public class CustomerService {
	private PizzaOrderDAO pizzaOrderDAO;
	private PizzaJdbcDAO jdbcDAO;
//	private AdminService adminService;
	
	public CustomerService() {
	}

	public CustomerService(PizzaOrderDAO pizzaDAO, PizzaJdbcDAO jdbcDAO) {
		pizzaOrderDAO = pizzaDAO;
		this.jdbcDAO = jdbcDAO;
	}
	
	
	public void saveAddress(String streetAddress, String city, String state, String zip) {
		try {
			Address address = new Address(streetAddress, city, state, zip);
			pizzaOrderDAO.saveAddress(address);
		} catch (Exception e) {
			throw new ServiceException("Address can not be inserted ", e);
		}
	}
	
	public void saveAddress(Address address) {
		try {
			pizzaOrderDAO.saveAddress(address);
		}
//		catch (org.hibernate.exception.GenericJDBCException e) {
//			System.out.println("ignoring dup address exception");
//		}
//		catch (BatchUpdateException e) {
//			throw new ServiceException("Address can not be inserted ", e);
//		}
		catch (Exception e) {
			throw new ServiceException("Address can not be inserted ", e);
		}
	}

	public Address findAddressById(int addID) {
		try {
			Long addIdLong = new Long(addID);
			Address address = pizzaOrderDAO.findAddressById(addIdLong);
			return address;
		} catch (Exception e) {
			throw new ServiceException("Address not found ", e);
		}
	}
	
	public Address findAddress(Address possibleAddress) {
		try {
			Address foundAddress = pizzaOrderDAO.findAddress(possibleAddress);
			return foundAddress;
		} catch (Exception e) {
			throw new ServiceException("Address not found ", e);
		}
	}
	
	
	
	public void placeOrder(OrderCommand orderCommand)  {

		Long sizeId = Long.valueOf(orderCommand.getPizzaSizeId());
		PizzaSize chosenSize = pizzaOrderDAO.findSizeById(sizeId);
		
		Set<Topping> chosenToppings = new HashSet<Topping>();
		for(String topStr : orderCommand.getPizzaToppingIds()) {
			Long topId = Long.valueOf(topStr);
			chosenToppings.add(pizzaOrderDAO.findToppingById(topId));
		}
		
		String streetAddress = orderCommand.getStreetAddress();
		String city = orderCommand.getCity();
		String state = orderCommand.getState();
		String zip = orderCommand.getZip();
		
		Address addressCommand = new Address(streetAddress, city, state, zip);
		Address address = findAddress(addressCommand);
		if (null == address.getId()) {
			pizzaOrderDAO.saveAddress(address);
		}
		
		PizzaOrder order;
		try {
			order = new PizzaOrder(address
					, chosenSize
					, chosenToppings
					, StatusName.ORDER_ACCEPTED);
			pizzaOrderDAO.insertOrder(order);
		} catch (Exception e) {
			throw new ServiceException("Order could not be inserted ", e);
		}

//		return order;
	}
	
	public void validateOrder(OrderCommand orderCommand) {
		if (orderCommand.getPizzaSizeId() == null) {
			throw new ServiceException("Order has no size");
		}
			List<PizzaSize> allSizes;
			allSizes = pizzaOrderDAO.findActivePizzaSizes();
			PizzaSize chosenSize = PresentationUtils.getSizeFromSizes(
					allSizes, Long.parseLong(orderCommand.getPizzaSizeId()) );
			
			if (chosenSize == null) { // size was there when we showed the form, but
										// disappeared since
				// show the form again--with updated lists of sizes and toppings
				// provided by referenceData()
				throw new ServiceException("Order can not be inserted: Your chosen pizza size is no longer available, please choose again ");

			}	
				
			System.out.println("#toppings from form: "
					+ orderCommand.getPizzaToppingIds().size());
			Set<Topping> chosenToppings = new TreeSet<Topping>();
			Set<Topping> allToppings;
			allToppings = pizzaOrderDAO.findToppings();
			System.out.println("getChosenToppingIds: "
					+ orderCommand.getPizzaToppingIds());
			for (String i : orderCommand.getPizzaToppingIds()) {
				Topping topping = PresentationUtils.getToppingFromToppings(
						allToppings, Long.parseLong(i));
				if (topping == null) {
					throw new ServiceException("Order can not be inserted: One of your chosen toppings is now gone, please choose again ");
				} else
					chosenToppings.add(topping);
			}
	}
	
	
	
//	public PizzaOrder makeOrder(PizzaOrderCommand orderCommand)  {
//		
//		if (orderCommand.getChosenSizeId() == null) {
//			throw new ServiceException("Order has no size");
//		}
//			List<PizzaSize> allSizes;
//			allSizes = pizzaOrderDAO.findPizzaSizes();
//			PizzaSize chosenSize = PresentationUtils.getSizeFromSizes(
//					allSizes, Long.parseLong(orderCommand.getChosenSizeId()) );
//			
//			if (chosenSize == null) { // size was there when we showed the form, but
//										// disappeared since
//				// show the form again--with updated lists of sizes and toppings
//				// provided by referenceData()
//				throw new ServiceException("Order can not be inserted: Your chosen pizza size is no longer available, please choose again ");
//
//			}	
//				
//			System.out.println("#toppings from form: "
//					+ orderCommand.getChosenToppingIds().size());
//			Set<Topping> chosenToppings = new TreeSet<Topping>();
//			Set<Topping> allToppings;
//			allToppings = pizzaOrderDAO.findToppings();
//			System.out.println("getChosenToppingIds: "
//					+ orderCommand.getChosenToppingIds());
//			for (String i : orderCommand.getChosenToppingIds()) {
//				Topping topping = PresentationUtils.getToppingFromToppings(
//						allToppings, Long.parseLong(i));
//				if (topping == null) {
//					throw new ServiceException("Order can not be inserted: One of your chosen toppings is now gone, please choose again ");
//				} else
//					chosenToppings.add(topping);
//			}
//			Integer addressNo = orderCommand.getAddressNumber();
//			Long addIdLong = new Long(addressNo);
//			Address address = pizzaOrderDAO.findAddress(addIdLong);
//			PizzaOrder order;
//			try {
//				order = new PizzaOrder(address
//						, chosenSize
//						, chosenToppings
//						, jdbcDAO.findCurrentDate()
//						, StatusName.PREPARING);
//				pizzaOrderDAO.insertOrder(order);
//			} catch (Exception e) {
//				throw new ServiceException("Order can not be inserted ", e);
//			}
//
//		return order;
//	}	
//	
//	@Transactional()
//	public List<PizzaOrder> getOrderDetails(int addressNumber)
//			 {
//		List<PizzaOrder> pizzaOrders = null;
//		try {
//			pizzaOrders = pizzaOrderDAO.findOrdersByAddress(addressNumber, jdbcDAO.findCurrentDate());
//		} catch (Exception e) {
//			throw new ServiceException("Error in getting status ", e);
//		}
//		return pizzaOrders;
//	}

	@Transactional()
	public Set<PizzaOrder> findOrdersByAddress(Address address)
			 {
		Set<PizzaOrder> pizzaOrders = null;
		try {
			pizzaOrders = pizzaOrderDAO.findOrdersByAddress(address);
		} catch (Exception e) {
			throw new ServiceException("Error in finding orders by address ", e);
		}
		return pizzaOrders;
	}
	
	@Transactional(readOnly=true)
	public List<PizzaSize> getPizzaSizes()
	{
		List<PizzaSize> sizes = null;
		try {
			System.out.println("starting getPizzaSizes");
			sizes = pizzaOrderDAO.findActivePizzaSizes();
		} catch (Exception e) {
			throw new ServiceException("Can't access pizza sizes in db: ", e);
		}
	   return sizes;	
	}

	@Transactional(readOnly=true) 
	public Topping getToppingById(Long toppingId) {
		Topping top = null;
		try {
			top = pizzaOrderDAO.findToppingById(toppingId);
		}
		catch (Exception e) {
			throw new ServiceException("Can't find topping with id");
		}
		return top;
	}
	
	@Transactional(readOnly=true)
	public Set<Topping> getToppings()
	{
		Set<Topping> toppings = null;
		try {
			toppings = pizzaOrderDAO.findActiveToppings();
		} catch(Exception e) {
			throw new ServiceException("Can't access toppings in db: ", e);
		}
		return toppings;
	}
	
}
