package com.pizza.dao;

import java.util.List;

import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;


public interface PizzaJdbcDAO {

	Long findMinId(String tableName);
	
     void createTopping(String topping) ;

     void createPizzaSize(String sizeName) ;
     
     void deleteTables();
     
     void deleteTopping(String topping) ;

     void deletePizzaSize(String size) ;

     void advanceDay() ;

     int findLastReportDate() ;

     void updateLastReportDate(int lastReportDate) ;

     int findCurrentDate() ;

     List<PizzaSize> findPizzaSizes() ;

     List<Topping> findToppings() ;
     
     // advance master order number for database
     int findNextOrderNumber() ;
}