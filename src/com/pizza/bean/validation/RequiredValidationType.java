package com.pizza.bean.validation;



/**
 * Validation type for "required" rule. Checks for null, and rejects <code>String</code>s
 * that are empty or whitespace.
 */
public class RequiredValidationType
       extends ValidationType {

	private static final long serialVersionUID = 7778564257383793172L;

	private static final String TYPE_NAME = "required";

	/**
	 * Empty Constructor.
	 */
	RequiredValidationType() {
		// default
	}

	/**
	 * Constructor.
	 * @param errorKey the message resource key associiated with validation failure
	 * @param name the name of the SimpleValidationType( e.g. zipcode, city, name, ...)
	 */
	public RequiredValidationType(final String errorKey, final String name) {
		super(errorKey, name);
	}

//	/**
//	 * Create a new RequiredValidationType for a customer using an array from an input file.
//	 * @param inputLine data from the loader file
//	 */
//	RequiredValidationType(final String[] inputLine) {
//		super(inputLine[Indexes.ERRKEY.ordinal()], inputLine[Indexes.NAME.ordinal()]);
//	}

	/**
	 * Copy constructor.
	 * @param source The source object.
	 */
	public RequiredValidationType(final RequiredValidationType source) {
		this(source.getErrorKey(), source.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValueValid(final Object value) {

		if (value == null) {
			return false;
		}

		if (value instanceof String && ((String)value).trim().length() == 0) {
			return false;
		}

		return true;
	}
}
