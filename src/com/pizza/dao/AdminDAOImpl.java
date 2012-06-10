package com.pizza.dao;

/**
 * Database access class for baker and admin related tasks
 * (maybe shouldn't be called a DAO)
 */
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaOrder.StatusName;
import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;
import com.pizza.domain.Topping.ToppingType;

public class AdminDAOImpl extends HibernateDaoSupport implements AdminDAO {
    
	public AdminDAOImpl()  {
	}
	
	@Override
	public void createTopping(String toppingName, ToppingType type) {
		this.getHibernateTemplate().saveOrUpdate(new Topping(toppingName, type));
	}

	public void createTopping(Topping topping) {
		this.getHibernateTemplate().saveOrUpdate(topping);
	}

	public void createPizzaSize(String sizeName)  {
		this.getHibernateTemplate().saveOrUpdate(new PizzaSize(sizeName));
	}
	
	public void deleteTopping(final String toppingName) {
		this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(final Session session) {
				return session.createQuery("delete from Topping where name = :topName")
				.setString("topName", toppingName).executeUpdate();
			}
		});
	}

	public void deletePizzaSize(final String sizeName)  {
		this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(final Session session) {
				return session.createQuery("delete from PizzaSize where name = :sizeName")
				.setString("sizeName", sizeName).executeUpdate();
			}
		});
	}
	
	
	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#findToppings()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PizzaSize> findAllSizes()  
	{
		List<PizzaSize> sizes = this.getHibernateTemplate()
								.find("from " + PizzaSize.class.getSimpleName());
		return sizes;
	}
	
	/* (non-Javadoc)
	 * @see com.pizza.dao.PizzaOrderDAO#findToppings()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<Topping> findAllToppings()  
	{
		List<Topping> toppings = this.getHibernateTemplate()
								.find("from " + Topping.class.getSimpleName());
		return new TreeSet<Topping>(toppings);
	}

//	// find first order with specified status
//	/* (non-Javadoc)
//	 * @see com.pizza.dao.IIAdminDAO#findFirstOrder(int)
//	 */
//	@Override
//	public Long findFirstOrderId(final int status) 
//	{
//		Long ordNo = Long.parseLong((String) this.getHibernateTemplate().execute(
//				new HibernateCallback() {
//					public Object doInHibernate(final Session session) {
//						return session.createQuery(" min(id) from PizzaOrder " 
//												+ " where status = :status")
//												.setInteger("status", status)
//												.uniqueResult();			
//					}
//				}
//		));
//		return ordNo;
//	}
	
	/* (non-Javadoc)
	 * @see com.pizza.dao.IIAdminDAO#updateOrderStatus(java.lang.Long, int)
	 */
	@Override
	public void updateOrderStatus(final Long ordNo, final StatusName newStatus) 
	{
		this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(final Session session) {
						return session.createQuery("update PizzaOrder "
												+ " set status = :newStatus" 
												+ " where id = :ordNo ")
												.setParameter("newStatus", newStatus)
												.setLong("ordNo", ordNo)
												.executeUpdate();			
					}
				}
			);		
	}
	
//	// get all orders, without toppings, between day1 and day2 (inclusive)
//	/* (non-Javadoc)
//	 * @see com.pizza.dao.IIAdminDAO#findOrdersByDays(int, int)
//	 */
//	@Override
//	@SuppressWarnings("unchecked")
//	public List<PizzaOrder> findOrdersByDays(int day1, int day2)  {
//		
//		List<PizzaOrder> ordersList = this.getHibernateTemplate().findByNamedParam(
//				" from PizzaOrder po "
//				+ " where po.day >= :day1 "
//				+ " and po.day <= :day2 "
//				, new String[] {"day1", "day2"}
//				, new Object[] {day1, day2} );
//		
//		
//		return ordersList; 
//	}
	
	
	@SuppressWarnings("unchecked")
	public Set<PizzaOrder> findAllOrders()  {
		
		List<PizzaOrder> ordersList = this.getHibernateTemplate().find(
				" from PizzaOrder po "
				+ " left join fetch po.size "
				+ " left join fetch po.toppings "
				+ " left join fetch po.address");
		
		// to avoid dups
		Set<PizzaOrder> ordersSet = new LinkedHashSet<PizzaOrder>(ordersList);
		return ordersSet; 
	}
	
	@Override
	public PizzaSize findPizzaSize(Long id) {
		return (PizzaSize) this.getHibernateTemplate().get(PizzaSize.class, id);
	}
	
	@Override
	public Topping findPizzaTopping(Long id) {
		return (Topping) this.getHibernateTemplate().get(Topping.class, id);
	}
}
