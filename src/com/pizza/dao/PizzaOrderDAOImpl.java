package com.pizza.dao;

/**
 *  
 *
 * Data access class for pizza order objects.
 */

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.pizza.domain.Address;
import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;



public class PizzaOrderDAOImpl extends HibernateDaoSupport implements PizzaOrderDAO {
	
    
	public PizzaOrderDAOImpl()  {
	}
	
	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#saveAddress(com.pizza.domain.Address)
	 */
	@Override
	public void saveAddress(Address address) {
		this.getHibernateTemplate().saveOrUpdate(address);
	}
	
	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#insertOrder(com.pizza.domain.PizzaOrder)
	 */
	@Override
	public void insertOrder(PizzaOrder order) {
		
		this.getHibernateTemplate().save(order);
	}
	
	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#findAddressById(java.lang.Long)
	 */
	@Override
	public Address findAddressById(Long addressId) {
		return (Address) this.getHibernateTemplate().get(Address.class, addressId);
	}
	
	@SuppressWarnings("unchecked")
	public Address findAddress(Address address) {
		List<Address> addresses = this.getHibernateTemplate()
							.findByNamedParam("from Address a"
							+ " where a.streetAddress = :streetAddress "
							+ " and a.city = :city "
							+ " and a.state = :state "
							+ " and a.zip = :zip "
							,new String[] {"streetAddress", "city", "state", "zip"}
							,new Object[] {address.getStreetAddress()
									, address.getCity()
									, address.getState()
									, address.getZip()});
		if(null != addresses && !addresses.isEmpty()) {
			return addresses.get(0);
		} 
		else {
			return address;
		}
//		Hibernate.initialize(address);
//		return address;
	}	
	
	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#findPizzaSizes()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PizzaSize> findActivePizzaSizes()  
	{
		List<PizzaSize> sizes = this.getHibernateTemplate()
							.find("from PizzaSize where active = 1");
		return sizes; 
	}

	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#findToppingById(java.lang.Long)
	 */
	@Override
	public Topping findToppingById(Long toppingId) {
		return (Topping) this.getHibernateTemplate().get(Topping.class, toppingId);
	}
	
	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#findSizeById(java.lang.Long)
	 */
	@Override
	public PizzaSize findSizeById(Long sizeId) {
		return (PizzaSize) this.getHibernateTemplate().get(PizzaSize.class, sizeId);
	}

	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#findToppings()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<Topping> findToppings()  
	{
		List<Topping> toppings = this.getHibernateTemplate()
								.find("from " + Topping.class.getSimpleName());
		return new TreeSet<Topping>(toppings);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Set<Topping> findActiveToppings()  
	{
		List<Topping> toppings = this.getHibernateTemplate()
								.find("from Topping where active=1");
		return new TreeSet<Topping>(toppings);
	}
	
	
	// Get orders, including toppings for a certain day and address
	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#findOrdersByAddress(com.pizza.domain.Address)
	 */
	@Override
	@SuppressWarnings("unchecked")		
	public Set<PizzaOrder> findOrdersByAddress(Address address)
	{
		List<PizzaOrder> ordersList = this.getHibernateTemplate().findByNamedParam(
				" from PizzaOrder po "
				+ " left join fetch po.size "
				+ " left join fetch po.toppings "
				+ " where po.address = :adrs "
				, new String[] {"adrs"}
				, new Object[] {address} );
		// to avoid dups
		Set<PizzaOrder> ordersSet = new LinkedHashSet<PizzaOrder>(ordersList);
		
		return ordersSet; 
	}
	
	
	
//	@SuppressWarnings("unchecked")		
//	public List<PizzaOrder> findOrdersByAddress(int addressNumber, int day)
//	{
//		Long addressNumberLong = new Long(addressNumber);
//		List<PizzaOrder> ordersList = this.getHibernateTemplate().findByNamedParam(
//				" from PizzaOrder po "
//				+ " where po.address.id = :addressNumber "
//				+ " and po.day = :day "
//				, new String[] {"addressNumber", "day"}
//				, new Object[] {addressNumberLong, day} );
//		
//		
//		for (PizzaOrder order : ordersList) {
//			//initialize the mappings that are lazily loaded via hibernate proxies
//			Hibernate.initialize(order.getSize());
//			Hibernate.initialize(order.getToppings());
//			Hibernate.initialize(order.getAddress());
//		}
//		
//		return ordersList; 
//	}
	
}
