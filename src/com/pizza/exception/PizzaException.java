package com.pizza.exception;

/**
 * Base unchecked exception class.
 *
 */
@SuppressWarnings("serial")
public abstract class PizzaException
       extends RuntimeException {

	/**
	 * Create an instance with a message.
	 * @param message  the message
	 */
	protected PizzaException(final String message) {
		super(message);
	}

	/**
	 * Create an instance with a message and a cause.
	 * @param message  the message
	 * @param cause  the cause
	 */
	protected PizzaException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create an instance with a cause.
	 * @param cause  the cause
	 */
	protected PizzaException(final Throwable cause) {
		super(cause);
	}
}
