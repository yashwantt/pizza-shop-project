package com.pizza.mvc.customer;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.pizza.domain.Address;
import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;
import com.pizza.service.CustomerService;
import com.pizza.ui.controller.ControllerUtils;
import com.pizza.util.PresentationUtils;
import com.pizza.util.StringUtils;

public class HomeAsyncController extends MultiActionController {
	private CustomerService customerService;

	public void getToppings(final HttpServletRequest request, final HttpServletResponse response ) throws IOException {
		Set<Topping> allToppings = customerService.getToppings();
		JSON results = ControllerUtils.buildToppingsJson(allToppings);
		ControllerUtils.sendJson(response, results);
	}

	public void getSizes(final HttpServletRequest request, final HttpServletResponse response ) throws IOException {
		List<PizzaSize> allSizes = customerService.getPizzaSizes();
		JSON results = ControllerUtils.buildSizesJson(allSizes);
		ControllerUtils.sendJson(response, results);
	}
	
	public ModelAndView getCustomerOrders(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Address addressCommand) throws IOException {
		
		Address verifiedAddress = customerService.findAddress(addressCommand);
		
		Set<PizzaOrder> customerOrders = new HashSet<PizzaOrder>();
		
		if (null != verifiedAddress.getId()){
			customerOrders = customerService.findOrdersByAddress(verifiedAddress);
		}
		
		JSONObject ordersJson = buildOrdersJson(customerOrders);

		ControllerUtils.sendJson(httpServletResponse, ordersJson);
		return null;
	}

	public JSONObject buildOrdersJson(Set<PizzaOrder> customerOrders) throws IOException {
		JSONObject ordersJson = new JSONObject();
		ordersJson.element("identifier", "id");

		JSONArray ordersJsonArray = new JSONArray();
		for(PizzaOrder order: customerOrders) {
			JSONObject orderJson = new JSONObject()
									.element("id", order.getId())
									.element("status", order.getStatus())
									.element("size", StringUtils.makeProper(order.getSize().getName()))
									.element("toppings", order.getToppingsString());
			ordersJsonArray = ordersJsonArray.element(orderJson);
		}		
		
		ordersJson.element("items", ordersJsonArray);
		return ordersJson;
	}		
	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	
}
