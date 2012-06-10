package com.pizza.bean.bean;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.usertype.UserType;

/**
 * Abstract base class for user types.
 *

 */
public abstract class AbstractUserType
       implements UserType {

	private final Log log = LogFactory.getLog(getClass());
	private final Log typesLog = LogFactory.getLog("org.hibernate.type." + getClass().getName());

   /**
    * {@inheritDoc}
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	public Object deepCopy(final Object value) {
		return value;
	}

   /**
    * {@inheritDoc}
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	public boolean isMutable() {
		return false;
	}

   /**
    * {@inheritDoc}
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
	 */
	public Object assemble(final Serializable cached, final Object owner) {
		return cached;
	}

   /**
    * {@inheritDoc}
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	public Serializable disassemble(final Object value) {
		return (Serializable)value;
	}

   /**
    * {@inheritDoc}
	 * @see org.hibernate.usertype.UserType#replace(
	 * 	java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public Object replace(
			final Object original, final Object target,
			final Object owner) {

		return original;
	}

   /**
    * {@inheritDoc}
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	public int hashCode(final Object o) {
		return o.hashCode();
	}

   /**
    * {@inheritDoc}
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(
			final Object x, final Object y) {

		if (x == y) {
			return true;
		}
		if (null == x || null == y) {
			return false;
		}
		return x.equals(y);
	}

//	/**
//	 * Log an error setting a <code>PreparedStatement</code> value.
//	 * @param value  the value
//	 * @param index  the parameter index
//	 * @param e  the exception
//	 */
//	protected void logError(final Object value, final int index, final Throwable e) {
//		Logger.error(log, e, "could not bind value \"{0}\" to parameter: {1}; {2}", value, index, e.getMessage());
//	}
//
//	/**
//	 * Log setting null for a parameter.
//	 * @param index  the parameter index
//	 */
//	protected void logSetNull(final int index) {
//		if (typesLog.isTraceEnabled()) {
//			Logger.trace(typesLog, "binding null to parameter: {0}", index);
//		}
//	}
//
//	/**
//	 * Log setting a parameter value.
//	 * @param value  the value
//	 * @param index  the parameter index
//	 */
//	protected void logSet(final Object value, final int index) {
//		if (typesLog.isTraceEnabled()) {
//			Logger.trace(typesLog, "binding \"{0}\" to parameter: {1}", value, index);
//		}
//	}

	/**
	 * Get the logger for the "org.hibernate.type." category.
	 * @return  the logger
	 */
	protected Log getTypesLog() {
		return typesLog;
	}
}
