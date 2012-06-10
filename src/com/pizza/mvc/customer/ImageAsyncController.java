package com.pizza.mvc.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.pizza.domain.Topping;
import com.pizza.service.CustomerService;
import com.pizza.ui.controller.ControllerUtils;


public class ImageAsyncController extends MultiActionController {


	private CustomerService customerService;
//	private ToppingImageCache toppingImageCache;

	
	public void getToppingImage(final HttpServletRequest request, final HttpServletResponse response )  {
		
//		final long practiceId = EncryptedServletUtils.getRequiredLongParameter(request, UrlParm.practiceId);
		Long toppingId = Long.parseLong(request.getParameter("id"));
		
		Topping topping = customerService.getToppingById(toppingId);
		
		ControllerUtils.writeBlob(topping.getLogo(), "image/png", response);
		
	}

	
	
//	public void setToppingImageCache(final ToppingImageCache toppingImageCache) {
//		this.toppingImageCache = toppingImageCache;
//	}	
	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	
}
