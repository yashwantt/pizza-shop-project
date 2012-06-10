
package com.pizza.bean.regex;


/**
 * represents a custom regex phrase.
 */
public class RegexEntity {
	private static final long serialVersionUID = 1210278814708855614L;

	private String entityName;
	private String definition;

	/**
	 * constructor.
	 * @param entityName the regex entity name
	 * @param definition the regex string
	 */
	public RegexEntity(final String entityName, final String definition) {
		this.entityName = entityName;
		this.definition = definition;
	}

	/**
	 * default.
	 */
	RegexEntity() {
		// empty
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(final String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(final String definition) {
		this.definition = definition;
	}


}
