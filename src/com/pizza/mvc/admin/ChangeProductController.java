package com.pizza.mvc.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;
import com.pizza.service.AdminService;


public class ChangeProductController extends SimpleFormController {
	private AdminService adminService;

	
	public ChangeProductController() {
		setCommandName("product");
		setCommandClass(ProductCommand.class);
		setFormView("product_form");
		setSuccessView("redirect:change_product_result.htm");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
//		ProductCommand command = new ProductCommand();
//		return command;
		ProductCommand productCommand = (ProductCommand) super.formBackingObject(request);
		return productCommand;
	}
	
	// this method provides fresh data (model vars) for the form on each time it
	// is displayed
	@SuppressWarnings("rawtypes") 
	@Override
	protected Map referenceData(HttpServletRequest request, Object o,
			Errors errors)  {
		
		List<PizzaSize> allSizes = adminService.getPizzaSizes();
		Set<Topping> allToppings = adminService.getToppings();
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("allSizes", allSizes);
		m.put("allToppings", allToppings);
		return m;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object command,
			BindException errors) throws Exception {

		ProductCommand prodCommand = (ProductCommand) command;

		String action = (String) httpServletRequest.getParameter("submitAction");
		
		if ( action.equals("add size")) {
			adminService.addPizzaSize(prodCommand.getSize().getName());
		} else if ( action.equals("remove size")) {
			adminService.deleteSize(prodCommand.getSize().getName());
		} else if ( action.equals("add topping")) {
			adminService.addTopping(prodCommand.getTopping().getName(), prodCommand.getTopping().getType());
		} else if ( action.equals("remove topping")) {
			adminService.deleteTopping(prodCommand.getTopping().getName());
		}
		
		
			
		// set session var to survive redirect to success view (causes new
		// request from browser)
//		session.setAttribute("pizzaOrder", order);
//		return new ModelAndView(new RedirectView(getSuccessView()));
		return new ModelAndView(getSuccessView());
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
}
