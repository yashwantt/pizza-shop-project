package com.pizza.validation;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.pizza.dao.ResourceManager;
import com.pizza.util.CollectionUtils;


public class ValidatorUtils {

	/**
	 * Load all bind/validation errors into a javascript map accessible under
	 * 'pizza.context.bindErrors' Useful if you
	 * want to process validation errors in javascript rather than with Spring JSP tags.
	 *
	 *  @param referenceData
	 *          Map to load error data into - to be passed to presentation layer.
	 * @param errors
	 *          Errors generated during bind/validation.
	 */
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static void buildJavascriptValidationErrors(final Map<String, Object> referenceData, final Errors errors) {
		JSONArray jsonErrors = buildJavascriptValidationErrorsJSON(errors);
		final Map context = (Map) referenceData.get("context");
		if (context == null) {
			referenceData.put("context", CollectionUtils.newHashMap("bindErrors", jsonErrors));
		}
		else {
			context.put("bindErrors", jsonErrors);
		}
	}
	
	
	/**
	 * Build bind/validation errors json array.
	 * @param errors the bind/validation errors
	 * @return JSONArray of the fields names and field errors
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray buildJavascriptValidationErrorsJSON(final Errors errors) {
		final List<FieldError> errorList = errors.getFieldErrors();
		final JSONArray jsonErrors = new JSONArray();
		for (final FieldError error : errorList) {
			final JSONObject jsonError = new JSONObject();
			jsonError.put("fieldName", error.getField());
			final StringBuilder errorMsg = new StringBuilder();

			if (error.getCode() != null && error.getCode().length() > 0) {
				//There can be multiple error codes on a single field.
				errorMsg.append(getMessagesForFieldError(error));
			}
			jsonError.put("fieldError", errorMsg.toString());
			jsonErrors.element(jsonError);
		}
		return jsonErrors;
	}
	
	/**
	 * Get the messages for the field error.
	 * @param error the field error
	 * @return the messages
	 */
	public static String getMessagesForFieldError(final FieldError error) {
		final StringBuilder errorMsg = new StringBuilder();
		//There can be multiple error codes on a single field.
		if (error.getCode().contains(",")) {
			final String[] codes = error.getCode().split(",");
			int count = 0;
			errorMsg.append("<ul>");
			for (final String code : codes) {
				if (code.length() > 0) {
					//Resolve each code individually on its own line.
					if (error.getArguments() != null) {
						errorMsg.append("<li>").append(ResourceManager.instance()
								.getMessage(code)).append("</li>");
					}
					else {
						errorMsg.append("<li>").append(ResourceManager.instance()
								.getMessage(code)).append("</li>");
					}
					count++;
				}
			}
			errorMsg.append("</ul>");
		}
		else {
			if (error.getArguments() != null && error.getArguments().length > 0) {
				if (error.getArguments()[0] instanceof Object[]) {
					errorMsg.append(ResourceManager.instance()
							.getMessage(error.getCode()));
				}
				else {
					errorMsg.append(ResourceManager.instance()
							.getMessage(error.getCode()));
				}
			}
			else {
				errorMsg.append(ResourceManager.instance().getMessage(error.getCode()));
			}
		}
		return errorMsg.toString();
	}

}
