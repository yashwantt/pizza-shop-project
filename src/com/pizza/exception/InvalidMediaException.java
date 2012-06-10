package com.pizza.exception;


/**
 * A runtime exception thrown when there is a problem getting
 * media from the database.
 *
 */
public class InvalidMediaException extends PizzaException {

	private static final long serialVersionUID = -5001024503516099836L;

	/**
	 * Constructor.
	 * @param message  the message
	 * @param cause  the cause
	 */
	public InvalidMediaException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 * @param message  the message
	 */
	public InvalidMediaException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause  the cause
	 */
	public InvalidMediaException(final Throwable cause) {
		super(cause);
	}


}
