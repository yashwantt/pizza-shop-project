package com.pizza.service;

import java.util.Map;

import net.sf.json.JSONObject;

import com.pizza.bean.bean.DataObject;

public interface ValidationService {

    /**
     * Generates the validationFields to Types json object for the given class.
     * @param clazz class we will be validating
     * @param fieldNames optional, the names of the fields in the class to generate validation json for.
     * If not specified all fields are used.
     * @return json object representing the fields and their validation types
     */
    JSONObject buildValidatedFieldJSONObject(final Class<?> clazz, final String ... fieldNames);
    
    
    boolean validate(final DataObject dataObject, final Map<String, String> errors);
    
    String buildValidationTypeJSON();
}
