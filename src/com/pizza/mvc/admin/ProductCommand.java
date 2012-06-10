package com.pizza.mvc.admin;

import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;

/**
 *
 * Command object for Pizza Order Form.
 */

	
public class ProductCommand  {

	private PizzaSize size;
	private Topping topping;
	
	public ProductCommand() {
		size = new PizzaSize();
		topping = new Topping();
	}
	
	public PizzaSize getSize() {
		return size;
	}
	public void setSize(PizzaSize size) {
		this.size = size;
	}
	public Topping getTopping() {
		return topping;
	}
	public void setTopping(Topping topping) {
		this.topping = topping;
	}
	
	
	
}
