package com.pizza.mvc.admin;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;
import com.pizza.domain.PizzaOrder.StatusName;
import com.pizza.service.AdminService;
import com.pizza.ui.controller.ControllerUtils;



public class AdminWelcomeController extends AbstractController {
	private AdminService adminService;

	protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse){
		ModelMap model = new ModelMap();
		ControllerUtils.addToContext(model, "statusNames",  buildStatusNamesJsonArray());
		
		
		Set<Topping> allToppings = adminService.getToppings();
		JSON topsJson = ControllerUtils.buildToppingsJson(allToppings);

		List<PizzaSize> allSizes = adminService.getPizzaSizes();
		JSON sizesJson = ControllerUtils.buildSizesJson(allSizes);

		ControllerUtils.addToContext(model, "toppings", topsJson);
		ControllerUtils.addToContext(model, "pizzaSizes", sizesJson);
		
		ModelAndView mav = new ModelAndView("/admin/adminWelcome", model);
		return mav;
	}
	
	
	private JSONArray buildStatusNamesJsonArray() {
		
		JSONArray statusJsonArray = new JSONArray();
		for(StatusName status: StatusName.values()) {
			statusJsonArray.element(status);
		}
		return statusJsonArray;
	}
	
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
}
