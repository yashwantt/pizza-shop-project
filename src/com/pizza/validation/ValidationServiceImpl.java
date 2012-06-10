package com.pizza.validation;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.util.ReflectionUtils;

import com.pizza.bean.bean.DataObject;
import com.pizza.bean.validation.ValidationType;
import com.pizza.dao.ValidationTypeDaoImpl;
import com.pizza.mvc.customer.OrderCommand;
import com.pizza.service.ValidationService;
import com.pizza.util.CollectionUtils;



public class ValidationServiceImpl implements ValidationService {
	
    private ValidationTypeDaoImpl validationTypeDao = new ValidationTypeDaoImpl();
    private Map<String, ValidationType> validationTypes;

    /**
     * {@inheritDoc}
     */
    public JSONObject buildValidatedFieldJSONObject(final Class<?> clazz, final String ... fieldNames) {
    	final List<Class<?>> classes = new ArrayList<Class<?>>();
    	classes.add(clazz);
    	return this.buildValidatedFieldJSONObject(classes, fieldNames);
    }
    
    public JSONObject buildValidatedFieldJSONObject(final List<Class<?>> classes, final String ... fieldNames) {
    	final JSONObject ret = new JSONObject();
    	JSONObject vFieldsJson;
    	final Map<String, String> validatedFields = CollectionUtils.newHashMap();
    	for (final Class<?> clazz : classes) {
    		validatedFields.putAll(getValidatedFields(clazz));
    	}
    	if (fieldNames != null && fieldNames.length > 0) {
    		vFieldsJson = new JSONObject();
    		for (final String field : fieldNames) {
    			final String vFieldVal = validatedFields.get(field);
    			if (vFieldVal != null) {
    				vFieldsJson.put(field, vFieldVal);
    			}
    			else {
//    				Logger.debug(getLog(), "Field name specified for building JSON has no validation type."
//    						+ " field=[{0}]", field);
    			}
    		}
    	}
    	else {
    		vFieldsJson = JSONObject.fromObject(validatedFields);
    	}
    	ret.put("validatedFields", vFieldsJson);

    	return ret;
    }
	
    
    public Map<String, String> getValidatedFields(final Class<?> clazz) {
        final Map<String, String> valFields = CollectionUtils.newHashMap();

        try {
        	final PropertyDescriptor[] descs = Introspector.getBeanInfo(clazz).getPropertyDescriptors();

            for (final PropertyDescriptor desc : descs) {
                final Method read = desc.getReadMethod();

                if (read != null) {
                	final String typeName = getValidationTypeName(read.getAnnotations());
	                if (typeName != null) {
	                	valFields.put(desc.getName(), typeName);
	                }
                }
            }
        }
        catch (final IntrospectionException ie) {
            throw new RuntimeException("Could not getValidatedFields.  Is the propertyName correct?",
                ie);
        }

        return valFields;
    }
    
    /*
     * Given an array of annotations, this method cycles through them & returns the FIRST
     * validation type name it finds.
     */
    private String getValidationTypeName(final Annotation[] annotations)
        throws IntrospectionException {
        for (final Annotation a : annotations) {
            if (a instanceof BeanMap) {
                return getTypeNameFromMap((BeanMap) a);
            }

            if (a instanceof ValidatedType) {
                return ((ValidatedType) a).typeName();
            }
        }

        return null;
    }
	
    
    /**
     * <p>
     * Finds the type name associated with the class/property specified in the
     * {@link BeanMap} if the {@code BeanMap} is validating. If the
     * {@code BeanMap#validate()} annotation is set to <tt>false</tt> it a
     * <tt>null</tt> validation type string will be returned.
     * </p>
     *
     * @param vtm the {@code BeanMap}.
     * @return the string validation type, or <tt>null</tt>.
     * @throws IntrospectionException an {@code IntrospectionException}.
     */
    private String getTypeNameFromMap(final BeanMap vtm)
        throws IntrospectionException {
    	if (!vtm.validate()) {
//        	Logger.debug(getLog(), "Property: [{0}] is not being validated, validation has been turned off.",
//        			vtm.propertyName());
        	return null;
    	}

        final Annotation[] anno = getPropertyAnnotations(vtm.propertyName(), vtm.targetClass());
        if (anno == null) {
//        	Logger.debug(getLog(), "Property: [{0}] has no validation type mapped", vtm.propertyName(), anno);
        	return null;
        }

        for (final Annotation a : anno) {
            if (a instanceof ValidatedType) {
                return ((ValidatedType) a).typeName();
            }
        }

        return null;
    }
    
    /*
     * Finds all the annotations for this property in this class
     */
    private Annotation[] getPropertyAnnotations(final String propertyName,
        final Class<?> clazz) throws IntrospectionException {

    	PropertyDescriptor[] descs = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        String pname = propertyName;

        while (pname.indexOf(".") > -1) {
			final String first = pname.substring(0, pname.indexOf("."));
        	pname = pname.substring(pname.indexOf(".") + 1);
        	descs = findNestedPropertyDescriptors(first, descs);
        }

		for (final PropertyDescriptor desc : descs) {
        	if (desc.getName().equals(pname)) {
                final Method read = desc.getReadMethod();

                if (read == null) {
                	/// TODO exception ??? maybe this is fine...
                    throw new IntrospectionException(pname);
                }

                return read.getAnnotations();
            }
        }

        // no descriptor found for this property!
        throw new IntrospectionException(propertyName);
    }
    
    private PropertyDescriptor[] findNestedPropertyDescriptors(final String pname, final PropertyDescriptor[] descs)
	throws IntrospectionException {
	for (final PropertyDescriptor pd : descs) {
		if (pd.getName().equals(pname)) {
			return Introspector.getBeanInfo(pd.getPropertyType()).getPropertyDescriptors();
		}
	}
		throw new RuntimeException("The nested property name " + pname + " could not be found.");
	}
    
    
    /**
     * {@inheritDoc}
     */
    public boolean validate(final DataObject dataObject, final Map<String, String> errors) {
    	Set<String> fieldsToUse = null;

    	return doValidate(dataObject, fieldsToUse, errors);
    }

    private boolean doValidate(final DataObject dataObject, final Set<String> fields, final Map<String, String> errors) {
        try {
            final Map<String, ValidationType> valTypes = getValidationTypes();
            final PropertyDescriptor[] descs = Introspector.getBeanInfo(dataObject.getClass())
                                                     .getPropertyDescriptors();

//            if (getLog().isTraceEnabled()) {
//            	Logger.trace(getLog(), "Validating command object of class: {0}", dataObject.getClass().getName());
//            }

            for (final PropertyDescriptor desc : descs) {
//            	if (getLog().isTraceEnabled()) {
//            		Logger.trace(getLog(), "Inspecting [{0}]", desc.getName());
//            	}

            	// if the fields arg was given, and the set doesn't contain this property, then we might skip it
            	if (fields != null && !fields.contains(desc.getName())) {
            		// check if it is a solid field
					final Field f = ReflectionUtils.findField(dataObject.getClass(),
							desc.getName(), desc.getPropertyType());
            		if (f == null || Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers())) {
            			//not back by a legitimate field
//            			Logger.trace(getLog(), "Skipping [{0}]. It may be an unset dynamic field,"
//            					+ " or not considered a property to validate.", desc.getName());
            			continue;
        			}
            	}

                final Method read = desc.getReadMethod();

                if (read != null) {
                    final String typeName = getValidationTypeName(read.getAnnotations());

                    if (typeName != null) {
//                    	if (getLog().isTraceEnabled()) {
//                    		Logger.trace(getLog(), "Property [{0}] has validation type: [{1}]",
//                    				desc.getName(), typeName);
//                    	}
                        if (typeName.contains(",")) {
                        	final String[] typeNames = typeName.split(",");
                        	for (final String type : typeNames) {
                        		// fail fast and record the first error message
                        		// in the same order as described in the @ValidatedType
                        		if(!errors.containsKey(desc.getName())) {
                            		checkValue(type,
                                			getValue(desc, dataObject),
                                			valTypes,
                                			desc.getName(),
                                			errors);
                        		}
                        	}
                        }
                        else {
                        	checkValue(typeName,
                        			getValue(desc, dataObject),
                        			valTypes,
                        			desc.getName(),
                        			errors);
                        }
                    }
                }
            }

            return errors.isEmpty();
        }
        catch (final IntrospectionException ie) {
            throw new RuntimeException("Could not perform validation.  Is the propertyName correct? {"
            		+ ie.getMessage() + "}",
                ie);
        }
    }
    
    /*
     * Finds the value of the property of our data object.
     */
    private Object getValue(final PropertyDescriptor desc,
        final DataObject dataObject) throws IntrospectionException {
        final String propName = desc.getName();

        final Method read = desc.getReadMethod();

        if (read == null) {
            throw new IntrospectionException(propName);
        }

        return ReflectionUtils.invokeMethod(read, dataObject);
    }   
    
    public Map<String, ValidationType> getValidationTypes() {
        if (validationTypes != null) {
        	return validationTypes;
        }

    	final Map<String, ValidationType> valTypes = CollectionUtils.newHashMap();

        for (final ValidationType vt : validationTypeDao.findDefaultValidationTypes()) {
            valTypes.put(vt.getName(), vt);
        }

        return valTypes;
    }

    private void checkValue(final String tn, final Object value,
    		final Map<String, ValidationType> valTypes, final String propName,
    		final Map<String, String> errors) {
		final String typeName = tn.trim();
    	if (!valTypes.containsKey(typeName)) {
            throw new RuntimeException(typeName
               + " is not a recognized validation type.");
        }

        final ValidationType vt = valTypes.get(typeName);

		boolean pass = true;
		// TODO: If different kinds of array handling become necessary,
		// consider adding another field to the @ValidatedType annotation (and/or
		// @BeanMap).  Though it seems like this setting would vary per command
		// object, not necessarily per bean field (e.g. some commands might use
		// a username singly, others as an array).  So perhaps even a new
		// command annotation would be needed.
		//
		// The kind of array handling done here applies the validation rule to
		// each value, and if any value fails the whole field fails.  Other
		// applications may require that the field contain a certain number of
		// elements or not contain duplicates, in which case the validation type
		// should be handed the whole array at once, not value-by-value.
		if (value != null && Object[].class.isAssignableFrom(value.getClass())) {
			final Object[] values = (Object[])value;
			for (final Object val : values) {
				if (!vt.isValueValid(val)) {
					errors.put(propName, vt.getErrorKey());
					pass = false;
					break; // No reason to continue validating or adding redundant error messages
				}
			}
		}
		else {
			if (!vt.isValueValid(value)) {
				errors.put(propName, vt.getErrorKey());
				pass = false;
			}
		}
//        if (getLog().isTraceEnabled()) {
//        	Logger.trace(getLog(), "Checking property: [{0}] against validation type: [{1}] with value: [{2}]."
//        			+ " Result: {3}", propName, vt.getName(), value, pass ? "PASS" : "FAIL");
//        }
    }

    
    
    public String buildValidationTypeJSON() {
        //loop through to create the JSON
        final JSONObject vTypeJSON = new JSONObject();

        for (final ValidationType vt : validationTypeDao.findDefaultValidationTypes()) {
            vTypeJSON.put(vt.getName(), vt.toJSON(null));
        }

        final StringBuilder valTypes = new StringBuilder();
        valTypes.append("pizza.validationTypes = ");
        valTypes.append(vTypeJSON.toString() + ";");
        return valTypes.toString();
    }
    
    
    
    public static void main(String[] args) {
    	
    	ValidationServiceImpl valService = new ValidationServiceImpl();
    	
    	final Map<String, String> errors = CollectionUtils.newHashMap();
    	
    	OrderCommand dataObject = new OrderCommand(); 
    	
    	valService.validate(dataObject, errors);
    	
    	System.out.println("");
    	
    	
    }
    
    
    
}
