package com.pizza.bean.validation;

import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

/**
 * ValidationType that uses a regular expression to validate a test string.
 * The object being tested will be coerced to a string using <code>toString()</code>.
 * If the object is <code>null</code> then he regular expression will be run on an empty string.
 */
public class RegexValidationType
       extends ValidationType {

	private static final long serialVersionUID = 4002079289363351479L;

	private static final String TYPE_NAME = "regex";

	private String regexRule;
	private Pattern regexPattern;
	private Map<String, String> masks;
	private boolean compiled;

	/**
	 * Default constructor.
	 */
	protected RegexValidationType() {
		// default
	}

	/**
	 * Constructor.
	 * @param errorKey the message resource key associiated with validation failure
	 * @param name the name of the RegexValidationType( e.g. zipcode, city, name, ...)
	 * @param regexRule regular expression the defines a valid value for this type
	 */
	public RegexValidationType(final String errorKey, final String name, final String regexRule) {

		super(errorKey, name);
		setRegexRule(regexRule);
	}

//	/**
//	 * Create a new RegexValidationType for a customer using an array from an input file.
//	 * @param customer the customer for whom to create this SimpleValidationType
//	 * @param locale  the locale
//	 * @param inputLine data from the loader file
//	 */
//	RegexValidationType(final Customer customer, final Locale locale, final String[] inputLine) {
//		super(customer, locale, inputLine[Indexes.ERRKEY.ordinal()], inputLine[Indexes.NAME.ordinal()]);
//		setRegexRule(inputLine[Indexes.REGEX.ordinal()]);
//	}

	/**
	 * Copy constructor.
	 * @param source The source object.
	 */
	public RegexValidationType(final RegexValidationType source) {
		this(source.getErrorKey(), source.getName(), source.getRegexRule());
	}

	/**
	 * Get the regexRule that this SimpleValidationType uses to determine the validity of a value.
	 *
	 * @return regexRule
	 */
	public String getRegexRule() {
		return regexRule;
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
	 * @see com.pizza.bean.validation.ValidationType#isValueValid(java.lang.Object)
	 */
	@Override
	public boolean isValueValid(final Object value) {
		return checkRegex(value);
	}

	/**
	 * {@inheritDoc}
	 * @see com.pizza.bean.validation.ValidationType#toJSON(net.sf.json.JSONObject)
	 */
	@Override
	public JSONObject toJSON(final JSONObject jsonObj) {
		JSONObject jo = super.toJSON(jsonObj);
		if (regexRule != null) {
			jo.put("regexRule", regexRule);
		}

		return jo;
	}

	public void setMasks(final Map<String, String> masks) {
		this.masks = masks;
	}

	private boolean checkRegex(final Object o) {
		Object test = o != null ? o : "";
		if (!compiled) {
			regexPattern = regexRule == null ? null : Pattern.compile(unmask(regexRule));
			compiled = true;
		}
		return regexPattern == null || regexPattern.matcher(test.toString().trim()).matches();
	}

	private String unmask(final String rule) {
		if (masks == null) {
			return rule;
		}
		String polishedRule = rule;
		for (String maskName : masks.keySet()) {
			polishedRule = polishedRule.replace(maskName, masks.get(maskName));
		}
		return polishedRule;
	}

	/**
	 * @param regexRule The regex rule.
	 */
	public void setRegexRule(final String regexRule) {
		this.regexRule = regexRule;
		compiled = false;
	}
}
