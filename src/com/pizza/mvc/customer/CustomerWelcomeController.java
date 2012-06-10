package com.pizza.mvc.customer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;

import com.pizza.service.CustomerService;
import com.pizza.service.ValidationService;
import com.pizza.validation.ValidatorUtils;

public class CustomerWelcomeController extends AbstractFormController {
	
    /**
     * Name of the json object passed to any form that requires validation.
     */
	public static final String VALIDATED_FIELDS_OBJ_NAME = "validatedFields";
	
	private CustomerService customerService;
	private ValidationService validationService;
	
	private String[] allowedFields;
	
	public CustomerWelcomeController() {
		setCommandClass(OrderCommand.class);
	}
	
	@Override
	protected ModelAndView showForm(HttpServletRequest arg0,
			HttpServletResponse arg1, BindException arg2) throws Exception {
		
		final Map<String, Object> data = new HashMap<String, Object>();
		
		data.put(VALIDATED_FIELDS_OBJ_NAME, buildClientSideValidation());
		return new ModelAndView("customerWelcome", data);
	}
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		//if there is validation errors only need to send the JSON array of errors
		if(errors.hasErrors()) {
			
			response.setContentType("text/plain;charset=utf-8");
			// include Spring binding errors
			String cnt = ValidatorUtils.buildJavascriptValidationErrorsJSON(errors).toString();
			
			response.getWriter().write(cnt);
			response.getWriter().flush();
			return null;
		}
		else {// take the order
			OrderCommand orderCommand = (OrderCommand) command;
			
			customerService.placeOrder(orderCommand);
		}
		
		return null;
	}
	

	
	
	/**
	 * Build the JSON structure which holds the client side validation.
	 * @return json string
	 */
	protected String buildClientSideValidation() {
		final JSONObject validatedFieldsJSON = getValidationService().buildValidatedFieldJSONObject(getCommandClass(), getAllowedFields());
		return validatedFieldsJSON.toString();
	}
	
	
	/**
	 * The names of the parameters that are allowed for this controller.<br/>
	 * If this property is set then only these parameters will be bound to the command object.<br/>
	 * This field is not currently @Required, because it wouldn't be backwards compatible, but it
	 * is absolutely recommended that it be set for all future form controllers.  This is for security.
	 *
	 * TODO: This can also be used to build client side validation json
	 *
	 * 	@param allowedFields the allowedFields to set
	 */
	public void setAllowedFields(final String[] allowedFields) {
		this.allowedFields = allowedFields;
	}
	/**
	 * @return the allowedFields
	 */
	protected String[] getAllowedFields() {
		return allowedFields;
	}
	

	
	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * @param validationService the validationService to set
	 */
	public void setValidationService(final ValidationService validationService) {
		this.validationService = validationService;
	}

	/**
	 * @return the validation service
	 */
	protected ValidationService getValidationService() {
		return validationService;
	}


	
}
