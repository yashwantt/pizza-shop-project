package com.pizza.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

	public final class SpringContext
    implements ApplicationContextAware {

	private static SpringContext instance = new SpringContext();

	private ApplicationContext applicationContext;

	/**
	 *	 Accessible via instance() only.
	 */
	private SpringContext() {
		// use instance()
	}

	/**
	 * Get the singleton instance.
	 * @return the instance
	 */
	public static SpringContext instance() {
		return instance;
	}

	/**
	 * Get a bean from Spring.
	 * @param <T>  the bean type
	 * @param name  the bean name
	 * @return  the bean
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(final String name) {
		return (T)applicationContext.getBean(name);
	}

	/**
	 * {@inheritDoc}
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(
	 * 	org.springframework.context.ApplicationContext)
	 */
	@Required
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * For testing purposes only.  Don't use.
	 * @return the application context
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
