package com.pizza.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pizza.bean.bean.DataObject;


/**
 * Method-level annotation placed on attribute in a DataObject (usually a command object such as
 * UserAccountCreateCommand) that maps said attribute to a property in a bean.
 * <br/>
 * The {@link #targetClass()} attribute indicates the bean that contains the property being mapped to.
 * The {@link #propertyName()} attribute indicates the property of that bean that is being mapped to.
 * <br/>
 * This mapping information can be used for multiple purposes.<br/>
 * The {@link com.pizza.service.ValidationService} uses this annotation to properly validate
 * command object fields against the validation type specified by the bean property.<br/>
 * A {@link com.pizza.account.processing.CommandProcessor} can use this annotation to automatically
 * update the properties of an entity bean with the annotated command object.
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeanMap {
	
	/** Default group name. */
	String DEFAULT_GROUP = "_DEFAULT_GROUP";

	/**
	 * The Class of the entity bean.
	 */
	Class<? extends DataObject> targetClass();

	/**
	 * Name of the property in the bean.  Nested properties are allowed.
	 */
	String propertyName();

	/**
	 * Whether or not to validate the data mapped by this bean. By default this
	 * property evaluates to <tt>true</tt>.
	 */
	boolean validate() default true;

	/**
	 * The group attribute allows multiple mappings to be linked in a group.
	 * It is not required, it defaults to {@link #DEFAULT_GROUP}.
	 */
	String group() default  DEFAULT_GROUP;	

}
