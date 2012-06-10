package com.pizza.bean.validation;

import java.util.Collection;

import net.sf.json.JSONObject;



/**
 * Validation type for "required" rule. Checks for null, and rejects <code>String</code>s
 * that are empty or whitespace.
 */
public class CollectionValidationType
       extends ValidationType {

	private static final long serialVersionUID = 7778564257383793172L;

	private static final String TYPE_NAME = "collectionSize";

	private Long maxSize;
	private Long minSize;
	
	/**
	 * Empty Constructor.
	 */
	CollectionValidationType() {
		// default

	}

	/**
	 * Constructor.
	 * @param errorKey the message resource key associiated with validation failure
	 * @param name the name of the SimpleValidationType( e.g. zipcode, city, name, ...)
	 */
	public CollectionValidationType(final String errorKey, final String name) {
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
	public CollectionValidationType(final CollectionValidationType source) {
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
	@SuppressWarnings("unchecked")
	@Override
	public boolean isValueValid(final Object value) {

		if (value == null) {
			return false;
		}
		
		if(! (value instanceof Collection)) {
			return false;
		}
		
		Collection<Object> colVal = (Collection<Object>) value;
		
		if (null != minSize && colVal.size() < minSize) {
			return false;
		}

		if (null != maxSize && colVal.size() > maxSize) {
			return false;
		}

		return true;
	}

	public JSONObject toJSON(final JSONObject jsonObj) {
		JSONObject jo = super.toJSON(jsonObj);
		if (maxSize != null) {
			jo.put("maxSize", maxSize);
		}

		if (minSize != null) {
			jo.put("minSize", minSize);
		}

		return jo;
	}
	
	public void setMaxSize(Long maxSize) {
		this.maxSize = maxSize;
	}

	public void setMinSize(Long minSize) {
		this.minSize = minSize;
	}

	public Long getMaxSize() {
		return maxSize;
	}

	public Long getMinSize() {
		return minSize;
	}
	
	public boolean isNoTrim() {
		return true;
	}
}
