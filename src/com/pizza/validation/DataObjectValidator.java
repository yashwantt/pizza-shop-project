package com.pizza.validation;

import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.pizza.bean.bean.DataObject;
import com.pizza.service.ValidationService;
import com.pizza.util.CollectionUtils;

public class DataObjectValidator implements Validator {

	private ValidationService validationService;
	
	@SuppressWarnings("rawtypes") // Class is a raw type
	public boolean supports(final Class clazz) {
		return  DataObject.class.isAssignableFrom(clazz);
	}
	
	public void validate(final Object command, final Errors errors) {
		DataObject dataObject = (DataObject) command;
		final Map<String, String> errorsMap = CollectionUtils.newHashMap();
		validationService.validate(dataObject, errorsMap);
		for(String errField : errorsMap.keySet()) {
//			errors.reject(errField, errorsMap.get(errField));
			errors.rejectValue(errField, errorsMap.get(errField));
		}
	}

	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

}
