package com.pizza.bean.validation;


import net.sf.json.JSONObject;

import com.pizza.dao.ResourceManager;

/**
 * Abstract base class used by the customizable validation framework.
 * Customers will be able to define & use custom validation as appropriate for
 * their business needs.
 *
 */
public abstract class ValidationType {

	private static final long serialVersionUID = -3685550301668133435L;
	private String errorKey;
	private String name;

	/**
	 * Boolean used to tell the global Validator whether or not to String.trim the field value before applying
	 * 	the validation rule.
	 * True - Don't trim the value prior to validating.
	 * False - Trim the value prior to validating (default value).
	 */
	private boolean noTrim = false;

	/**
	 * Default constructor.
	 */
	protected ValidationType() {
		// default
	}

	/**
	 * Constructor.
	 * @param customer  the customer
	 * @param locale  the locale
	 * @param errorKey the error key
	 * @param name  the name
	 */
	protected ValidationType(final String errorKey, final String name) {
		this.errorKey = errorKey;
		this.name = name;
	}

	/**
	 * Copy constructor.
	 * @param source The source object.
	 */
	protected ValidationType(final ValidationType source) {
		this(source.getErrorKey(), source.getName());
	}

	/**
	 * Get the errorKey associated with validation failure.  Message should be stored
	 * in the resource bundle.
	 * @return errorKey the errorKey
	 */
	public String getErrorKey() {
		return errorKey;
	}

	/**
	 * Get the name of the ValidationType.  Can include the following, plus any that are subsequently specified:
	 * name, streetAddress, city, zipcode, username, password, topictitle, topicdesc
	 *
	 * @return name the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Here's our brain.  For a given object, checks the validity against the rules
	 * defined in the ValidationType subclasses.  Putting the validity check here
	 * instead of in the ValidationService class allows us greater freedom in the types
	 * of validation we can define/perform.
	 *
	 * @param value - Object which is being checked for validity
	 * @return boolean true if valid, false if not.
	 */
	public abstract boolean isValueValid(final Object value);

	/**
	 * Get the type name of this validation type.  This is used to avoid inclusion of the actual
	 * class name in client-side code.
	 * @return the type name
	 */
	public abstract String getTypeName();

	/**
	 * Construct a JSON representation of this validation type.
	 * @param jsonObj JSONObject to which this validation type's structure will be added.
	 * @param theLocale  the locale
	 * @return if jsonObj is null, a new JSONObject, otherwise jsonObj
	 */
	public JSONObject toJSON(final JSONObject jsonObj) {
		JSONObject jo = jsonObj;
		if (jo == null) {
			jo = new JSONObject();
		}
		jo.put("typeName", getTypeName());
		jo.put("errorKey", ResourceManager.instance().getMessage(errorKey));
		jo.put("noTrim", isNoTrim());

		return jo;
	}

	/**
	 * @param errorKey The error key.
	 */
	public void setErrorKey(final String errorKey) {
		this.errorKey = errorKey;
	}

	/**
	 * @param name The name.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the noTrim
	 */
	public boolean isNoTrim() {
		return noTrim;
	}

	/**
	 * @param noTrim the noTrim to set
	 */
	public void setNoTrim(final boolean noTrim) {
		this.noTrim = noTrim;
	}

}
