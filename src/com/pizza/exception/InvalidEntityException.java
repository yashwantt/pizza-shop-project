package com.pizza.exception;

/**
 * Indicates that the data provided to create a new entity is invalid.
 *
 */
public class InvalidEntityException
       extends PizzaException {

	private static final long serialVersionUID = -3324196719073793772L;

	/**
	 * Create an instance with a message.
	 * @param message  the message
	 */
	public InvalidEntityException(final String message) {
		super(message);
	}

	/**
	 * Create an instance with a message and a cause.
	 * @param message  the message
	 * @param cause  the cause
	 */
	public InvalidEntityException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create an instance with a cause.
	 * @param cause  the cause
	 */
	public InvalidEntityException(final Throwable cause) {
		super(cause);
	}
}
