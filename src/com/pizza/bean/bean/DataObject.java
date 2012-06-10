
package com.pizza.bean.bean;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract base class for value objects. Contains reflection-based default
 * methods for hashCode, equals, and toString.
 *
 */
public abstract class DataObject
       implements Serializable {

	private static final long serialVersionUID = -8912412986615430023L;
	private final transient Log log = LogFactory.getLog(getClass());

   /**
    * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(
				this, ToStringStyle.MULTI_LINE_STYLE);
	}

   /**
    * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

   /**
    * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	/**
	 * Get the logger.
	 * @return  the logger
	 */
	protected Log getLog() {
		return log;
	}

	/**
	 * Useful for mutator methods.
	 * @param s  a string
	 * @return  the string trimmed if it's not <code>null</code>
	 */
	protected String trim(final String s) {
		return s == null ? null : s.trim();
	}
}
