package com.pizza.ui.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Abstract base class for controllers.
 *
 */
public abstract class BaseSimpleController
       extends AbstractController {

	private final Log log = LogFactory.getLog(getClass());

	private String viewName;



	/**
	 * Set the view to go to on success.
	 * @param view  the view name
	 */
	public void setViewName(final String view) {
		this.viewName = view;
	}



	/**
	 * Get the view name.
	 * @return the view name.
	 */
	protected String getViewName() {
		return viewName;
	}

	/**
	 * Get the logger.
	 * @return  the logger
	 */
	protected Log getLog() {
		return log;
	}
}
