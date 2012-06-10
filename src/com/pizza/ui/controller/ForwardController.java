package com.pizza.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * Forwards to the success view. Useful when there's no controller setup required
 * to populate a model and you want to wrap a JSP in a virtual Spring-managed url.
 *
 */
public class ForwardController
       extends BaseSimpleController {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ModelAndView handleRequestInternal(
			final HttpServletRequest request,
			final HttpServletResponse response) {

		return new ModelAndView(getViewName());
	}
}
