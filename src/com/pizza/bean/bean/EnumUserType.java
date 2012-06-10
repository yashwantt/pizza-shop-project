package com.pizza.bean.bean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Generic class to map a JDK 1.5 enum to a varchar column in the DB using the
 * enum name.
 *
 * http://www.hibernate.org/312.html
 */
public abstract class EnumUserType<E extends Enum<E>>
       extends AbstractUserType {

	private static final int[] SQL_TYPES = {
		Types.VARCHAR
	};

	private final Log log = LogFactory.getLog(getClass());

	private final Class<E> clazz;

	/**
	 * @param clazz  the class of the enum.
	 */
	protected EnumUserType(final Class<E> clazz) {
		this.clazz = clazz;
	}

   /**
    * {@inheritDoc}
	 * simple mapping to a SMALLINT.
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

   /**
    * {@inheritDoc}
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	public Class<?> returnedClass() {
		return clazz;
	}

   /**
    * {@inheritDoc}
	 * From the String name in the DB, get the enum.
	 * @see org.hibernate.usertype.UserType#nullSafeGet(
	 * 	java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(
			final ResultSet rs,
			final String[] names,
			final Object owner)
				throws SQLException {

		String name = rs.getString(names[0]);
		if (!rs.wasNull()) {
			try {
			  return Enum.valueOf(clazz, name);
			}
			catch (SecurityException e) {
				log.error("Cannot get value for enum class: " + clazz.getName(), e);
				throw e;
			}
			catch (IllegalArgumentException e) {
				log.error("Enum " + clazz.getName() + " doesn't include a string value of " + name
						+ ". Can't parse value of column " + names[0], e);
				throw e;
			}
		}
		return null;
	}

   /**
    * {@inheritDoc}
	 * Set the VARCHAR in the DB based on enum.name() value.
	 * @see org.hibernate.usertype.UserType#nullSafeSet(
	 * 	java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet(
			final PreparedStatement ps,
			final Object value,
			final int index) throws SQLException {

		try {
			if (value == null) {
//				logSetNull(index);
				ps.setNull(index, Types.VARCHAR);
			}
			else {
//				logSet(((Enum<?>)value).name(), index);
				ps.setString(index, ((Enum<?>)value).name());
			}
		}
		catch (SQLException e) {
//			logError(value, index, e);
			throw e;
		}
		catch (RuntimeException e) {
//			logError(value, index, e);
			throw e;
		}
	}
}
