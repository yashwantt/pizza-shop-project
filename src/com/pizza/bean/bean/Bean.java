
package com.pizza.bean.bean;

import java.io.IOException;
import java.io.ObjectInputStream;


/**
 * Base class for all business entities.
 *
 */
public abstract class Bean
       extends DataObject {

	private static final long serialVersionUID = 8198400400331244447L;
	private transient int hashCode;
	private transient String toString;

	/**
	 * Constructor.
	 */
	public Bean() {
		cacheHashAndToString();
	}

	/**
	 * Caches the hashCode and toString values.
	 */
	private void cacheHashAndToString() {
		hashCode = System.identityHashCode(this);
		toString = getClass().getName() + '@' + Integer.toHexString(hashCode());
	}

	/**
	 * {@inheritDoc}
	 * Two beans are equal only if they reference the same object in memory.
	 * Note that other definitions of equality can be problematic in some
	 * object-relational mapping contexts, so this method is final. If you
	 * wish to create alternative definitions of equality that take into
	 * account properties of the bean, one approach is to define an inner
	 * class that groups the properties of interest.
	 */
	@Override
	public final boolean equals(final Object o) {
		return o == this;
	}

	/**
	 * {@inheritDoc}
	 * Generate a hash code for this bean. This method is final for
	 * consistency with {@link #equals(Object)}.
	 */
	@Override
	public final int hashCode() {
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toString;
	}

	/**
	 * Deserializes an instance and refreshes the transient hashCode and toString values; otherwise they would be
	 * default values.
	 * @param in the ObjectInputStream
	 * @throws IOException IOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		cacheHashAndToString();
	}
}
