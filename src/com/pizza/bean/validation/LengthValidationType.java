
package com.pizza.bean.validation;

import java.util.Set;

import net.sf.json.JSONObject;


public class LengthValidationType
       extends RegexValidationType {

	private static final long serialVersionUID = -4644072231762508714L;

	private static final String TYPE_NAME = "len";

	private Long maxLength;
	private Long minLength;

	/**
	 * Default constructor.
	 */
	protected LengthValidationType() {
		// default
	}

//	/**
//	 * Create a new LengthValidationType for a customer using an array from an input file.
//	 * @param inputLine data from the loader file
//	 */
//	LengthValidationType(final String[] inputLine) {
//		super(customer, locale, inputLine);
//
//		maxLength = (inputLine.length > Indexes.MAXVAL.ordinal())
//			? parseLong(inputLine[Indexes.MAXVAL.ordinal()]) : null;
//		minLength = (inputLine.length > Indexes.MINVAL.ordinal())
//			? parseLong(inputLine[Indexes.MINVAL.ordinal()]) : null;
//	}

	/**
	 * Copy constructor.
	 * @param source The source object.
	 */
	public LengthValidationType(final LengthValidationType source) {
		super(source);
		maxLength = source.getMaxLength();
		minLength = source.getMinLength();
	}

	/**
	 * @return the maxLength
	 */
	public Long getMaxLength() {
		return maxLength;
	}

	/**
	 * @return the minLength
	 */
	public Long getMinLength() {
		return minLength;
	}

	/**
	 * {@inheritDoc}
	 * @see com.pizza.bean.validation.ValidationType#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	/**
	 * {@inheritDoc}
	 * @see com.pizza.bean.validation.ValidationType#isValueValid(java.lang.Object)
	 */
	@Override
	public boolean isValueValid(final Object value) {
		boolean retVal;

		if (value != null && value instanceof Set) {
			retVal = validateSet(value);
		}
		else {
			retVal = validateSingleValue(value);
		}

		return retVal;
	}

	/**
	 * Performs length validation on a single object <code>value</code>. In addition the
	 * parent class's validation will be performed.
	 * @param value
	 * 			The object to perform validation on.
	 * @return
	 * 			<code>False</code> if the object is not valid, otherwise <code>true</code>.
	 */
	private boolean validateSingleValue(final Object value) {
		if (minLength != null) {
			// Fail or pass fast - If the value is empty or null and minLength is 0, pass automatically, otherwise fail.
			if (value == null || value.toString().length() == 0) {
				return minLength == 0;
			}
			// Fail fast
			if (value.toString().length() < minLength) {
				return false;
			}
		}

		if (maxLength != null && value != null) {
			// Fail fast
			if (value.toString().length() > maxLength) {
				return false;
			}
		}

		return super.isValueValid(value);
	}

	/**
	 * Cycle through the set, performing validation on each object.
	 * @param value
	 * 			The {@linkplain java.util.set} to perform validation for.
	 * @return
	 * 			<code>False</code> if an of the objects are not valid, otherwise <code>true</code>.
	 */
	@SuppressWarnings("unchecked")
	private boolean validateSet(final Object value) {
		boolean retVal = true;

		for (Object o : (Set<Object>)value) {
			if (!validateSingleValue(o)) {
				retVal = false;
				break;
			}
		}


		return retVal;
	}

	/**
	 * {@inheritDoc}
	 * @see com.pizza.bean.validation.ValidationType#toJSON(net.sf.json.JSONObject)
	 */
	@Override
	public JSONObject toJSON(final JSONObject jsonObj) {
		JSONObject jo = super.toJSON(jsonObj);
		if (maxLength != null) {
			jo.put("maxLength", maxLength);
		}

		if (minLength != null) {
			jo.put("minLength", minLength);
		}

		return jo;
	}

	private Long parseLong(final String input) {
		if (input == null || input.trim().length() == 0) {
			return null;
		}
		try {
			return Long.parseLong(input);
		}
		catch (NumberFormatException nfx) {
			return null;
		}
	}

	/**
	 * @param maxLength The maximum length.
	 */
	public void setMaxLength(final Long maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @param minLength The minimum length.
	 */
	public void setMinLength(final Long minLength) {
		this.minLength = minLength;
	}
}
