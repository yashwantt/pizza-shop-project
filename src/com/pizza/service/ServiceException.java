package com.pizza.service;

/**
 *  
 *
 * This exception is thrown from business logic layer
 * to presentation layer. So the presentation layer
 * gets more generic errors than the SQLExceptions
 * thrown by the DAO layer.
 */

public class ServiceException extends RuntimeException {

	/**
	 * To satisfy Exception handling, avoid a warning, define a UID--
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(String arg0) {
		super(arg0);
	}
    public ServiceException(String reason, Throwable cause){
        super(reason, cause);
    }

}
