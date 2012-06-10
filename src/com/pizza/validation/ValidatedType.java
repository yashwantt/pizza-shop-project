package com.pizza.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method-level annotation placed on <? extends DataObject> getters to signify they must be
 * validated.  The typeName must map to a valid ValidationType.name.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidatedType {

	/**
	 * The typeName should equal a ValidationType.name.
	 */
	String typeName();
}
