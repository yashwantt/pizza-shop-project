package com.pizza.mvc.admin;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;
import com.pizza.domain.Topping.ToppingType;
import com.pizza.service.AdminService;
import com.pizza.ui.controller.ControllerUtils;

public class AddPizzaToppingController extends SimpleFormController {

	private AdminService adminService;

	
	public AddPizzaToppingController() {
		setCommandClass(ToppingCommand.class);
		setCommandName("toppingCommand");
		
	}
	
	@Override
	protected void initBinder(
			HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		// Convert multipart object to byte[]
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}
	
	@Override
	protected ModelMap referenceData(
			final HttpServletRequest request,
			final Object command,
			final Errors errors) {
		
		final ModelMap data = new ModelMap()
			.addAttribute("toppingTypes", ToppingType.values());
		return data;
	}
	
	@Override
	protected ModelAndView onSubmit(final HttpServletRequest request,
			final HttpServletResponse response, final Object command,
			final BindException errors) throws IOException {
		final ToppingCommand toppingCommand = (ToppingCommand) command;
		
		MultipartFile imageFile = toppingCommand.getImageFile();
		
		InputStream imageStream = imageFile.getInputStream();
		
		adminService.addTopping(toppingCommand.getName(), toppingCommand.getType(), imageStream);
		
		Set<Topping> allToppings = adminService.getToppings();
		JSON toppingsJson = ControllerUtils.buildToppingsJson(allToppings);

		final JSONObject json = new JSONObject();
		json.put("status", "0");
		json.put("toppings", toppingsJson);
		ControllerUtils.writeInTextarea(response, json);
		
		return null;
	}
	
	
	@Required
	public void setAdminService(final AdminService adminService) {
		this.adminService = adminService;
	}	
}
