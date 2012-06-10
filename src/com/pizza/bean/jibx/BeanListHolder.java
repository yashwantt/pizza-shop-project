package com.pizza.bean.jibx;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.pizza.bean.bean.Bean;

/**
 * Holds a list of beans for IET.
 *
 * TODO: This needs to be fixed to be casted back to T extends Bean
 *
 * @param <T> type of this holder
 */
public class BeanListHolder<T> {
	private List<T> list;
	private Class<?> clazz;
	private Map<String, String> metadata;

	/**
	 * Default constructor.
	 */
	public BeanListHolder() {
	}

	/**
	 * Constructor.
	 * @param list the list
	 * @param clazz class of object contained by this holder
	 */
	public BeanListHolder(final List<T> list, final Class<?> clazz) {
		this.list = list;
		this.clazz = clazz;
	}
	/**
	 * Constructor.
	 * @param list the list
	 * @param clazz class of object contained by this holder
	 * @param metadata metadata for this export
	 */
	public BeanListHolder(final List<T> list, final Class<?> clazz,
			final Map<String, String> metadata) {
		this(list, clazz);
		this.metadata = metadata;
	}
	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(final List<T> list) {
		this.list = list;
	}

	/**
	 * Get the bean class name.
	 * @return the bean class name
	 */
	public String getBeanClassName() {
		return clazz.getName();
	}

	/**
	 * Get the bean class.
	 * @return the bean class
	 */
	public Class<?> getBeanClass() {
		return clazz;
	}

	/**
	 * Set the bean class.
	 * @param beanClass the beanClass
	 */
	public void setBeanClass(final Class<? extends Bean> beanClass) {
		this.clazz = beanClass;
	}
	/**
	 * @return the metadata
	 */
	public Map<String, String> getMetadata() {
		return Collections.unmodifiableMap(metadata);
	}
	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(final Map<String, String> metadata) {
		this.metadata = metadata;
	}

}
