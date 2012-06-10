package com.pizza.dao;

/**
 * Database access class for baker and admin related tasks
 * (maybe shouldn't be called a DAO)
 */
import static com.pizza.dao.DBConstants.ADDRESS_TABLE;
import static com.pizza.dao.DBConstants.ORDER_TABLE;
import static com.pizza.dao.DBConstants.PIZZA_SIZE_TABLE;
import static com.pizza.dao.DBConstants.SYS_TABLE;
import static com.pizza.dao.DBConstants.TOPPING_ORDER_TABLE;
import static com.pizza.dao.DBConstants.TOPPING_TABLE;

import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;

public class PizzaJdbcDAOImpl extends JdbcDaoSupport implements PizzaJdbcDAO {
	
    
	public PizzaJdbcDAOImpl()  {
	}
	
	
	public Long findMinId(String tableName) {
		return this.getJdbcTemplate().queryForLong("select min(id) from " + tableName);
	}
	
	public void createTopping(String toppingName) {
		int newID = this.getJdbcTemplate()
			.queryForInt("select max(id) from " + TOPPING_TABLE) + 1;
		
		this.getJdbcTemplate().update("insert into " + TOPPING_TABLE + " (id, topping_name) " 
				+ " values(?, ?) ",new Object[]{newID, toppingName});
	}

	public void createPizzaSize(String sizeName)  {
		int newID = 
			this.getJdbcTemplate()
			.queryForInt("select max(id) from " + PIZZA_SIZE_TABLE) + 1;
		
		this.getJdbcTemplate().update("insert into " + PIZZA_SIZE_TABLE + " (id, size_name) " 
				+ " values(?, ?) ",new Object[]{newID, sizeName});
	}
	
	public void deleteTables() {
		
		deleteFrom(TOPPING_ORDER_TABLE ," 1 " ," 1 " );
		deleteFrom(ORDER_TABLE ," 1 " ," 1 " );
		deleteFrom(PIZZA_SIZE_TABLE ," 1 " ," 1 " );
		deleteFrom(ADDRESS_TABLE ," 1 " ," 1 " );
		deleteFrom(TOPPING_TABLE ," 1 " ," 1 " );
		
	}
	
	public void deleteTopping(String topping) 
	{
		deleteFrom(TOPPING_TABLE ,"topping_name" ,topping );
	}
	
	public void deletePizzaSize(String size) 
	{
	    deleteFrom(PIZZA_SIZE_TABLE, "size_name", size);	
	}
	
	public void advanceDay() 
	{
		this.getJdbcTemplate().update("update " + SYS_TABLE 
									+ " set current_day = current_day + 1 ");
	}
	
	public int findLastReportDate() 
	{
		int lastReportDate = this.getJdbcTemplate()
						.queryForInt("select last_report from " + SYS_TABLE); 
		return lastReportDate;
	}
	
	public void updateLastReportDate(final int lastReportDate) 
	{
		this.getJdbcTemplate().update("update " + SYS_TABLE 
				+ " set last_report = :lastReportDate");
	}

	private void deleteFrom(final String table, final String attr, final String val) 
	{
		this.getJdbcTemplate().update("delete from " + table 
							+ " where " + attr + "='" + val + "'");
	}

	public int findCurrentDate() 
	{
		int date = 
			this.getJdbcTemplate()
			.queryForInt("select current_day from " + SYS_TABLE);	
		
		return date;
	}

	@SuppressWarnings("unchecked")
	public List<PizzaSize> findPizzaSizes()  
	{
		List<PizzaSize> sizes = this.getJdbcTemplate()
							.queryForList(" select * from " + PIZZA_SIZE_TABLE);
		return sizes; 
	}

	@SuppressWarnings("unchecked")
	public List<Topping> findToppings()  
	{
		List<Topping> sizes = this.getJdbcTemplate()
								.queryForList(" select * from " + TOPPING_TABLE);
		return sizes;
	}
	
	
	
	private void advanceOrderNumber() 
	{
		this.getJdbcTemplate().update("update " + SYS_TABLE 
											+ " set last_order_id = last_order_id + 1 ");			
	}

	public int findNextOrderNumber() 
	{
		int nextOrderNumber = this.getJdbcTemplate()
		.queryForInt(" select last_order_id from " + SYS_TABLE);		
		advanceOrderNumber();
		return nextOrderNumber;
	}
}
