package com.pizza.mvc.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.pizza.domain.PizzaSize;
import com.pizza.service.AdminService;
import com.pizza.ui.controller.ControllerUtils;

public class AddPizzaSizeController extends SimpleFormController {

	private AdminService adminService;

	
	public AddPizzaSizeController() {
		setCommandClass(PizzaSize.class);
		
	}
	
	
	@Override
	protected ModelAndView onSubmit(final HttpServletRequest request,
			final HttpServletResponse response, final Object command,
			final BindException errors) throws IOException {
		final PizzaSize newSize = (PizzaSize) command;
		
		adminService.addPizzaSize(newSize.getName());
		
		List<PizzaSize> allSizes = adminService.getPizzaSizes();
		JSON sizesJson = ControllerUtils.buildSizesJson(allSizes);

		final JSONObject json = new JSONObject();
		json.put("status", "0");
		json.put("pizzaSizes", sizesJson);
		ControllerUtils.write(response, json);
		
		return null;
	}
	
	
	@Required
	public void setAdminService(final AdminService adminService) {
		this.adminService = adminService;
	}	
}
