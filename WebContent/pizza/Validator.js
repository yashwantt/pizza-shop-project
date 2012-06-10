
dojo.provide('pizza.Validator');

dojo.require("pizza.validation.Validator");
dojo.require("pizza.validation.ValidatedObject");
dojo.require("pizza.validation.ValidationDisplay");

/**
 * <p>
 * The <tt>pizza.Validator</tt> has been transformed into a temporary bridge
 * between the old client-side validation framework and the new framework that
 * is still being fully adopted. Its purpose is to provide a standard means for
 * continuing to support existing validation implementations through effectively
 * a translation layer.
 * </p>
 *
 * USAGE:
 *
 *	new pizza.Validator( validatedFieldsJSON [, errorDisplayFunction] );
 *
 * The validatedFieldsJSON is provided by the SpringMVC controller, and is tied to the annotated fields
 * on the command object.  For more information, see com.pizza.validation.ValidationEngine
 * The errorDisplayFunction is optional; the validator defaults to a sub-optimal alert box.  Support
 * for things like in-line notification, modal dialogs, whatever, etc, can be added on a page-by-page basis 
 * & completely customized for any individual form.  (Or we can standardize & remove this optional parameter)
 *
 * In the validator.tag, an event listener is added to the page to set up your validation, 
 * like so (using dojo):
 *
 *	dojo.addOnLoad( function() { new pizza.Validator( validatedFieldsJSON ) } );
 *
 * 
 */
pizza.Validator = function(valFields, displayErrFunc) {
	dojo.deprecated("pizza.Validator is currently being used as a compatibility layer between pre-5.0 client side validation and the new validation framework.");
	pizza.valObj = new pizza.validation.Validator(valFields);
};

/**
 * Initialize the Field Validator - new pizza.Validator()
 * @param - customDisplayErr
 * @return - none
 */
pizza.initValidatedFields = function(customDisplayErr) {
	pizza.valObj = new pizza.validation.Validator();
};

pizza.Validator.prototype = {
	/**
	 * Allows a non-standard field to have client-side validation applied to it.
	 * @param fieldName  the ID of the input control
	 * @param validationRuleName  the validation rule name (e.g. "firstName", "email")
	 */
	addManualField: function(fieldName, validationRuleName) {
		if (pizza.valObj) {
			var validatorObject = pizza.valObj;
			validatorObject.addValidation({ type: "field", field: fieldName, validationRule: validationRuleName });
		}
	},
	
	initialize: function() {
		// No-op
	},

	/**
	 * 
	 */
	initNamedField : function(field) {
		dojo.deprecated("pizza.Validator#initNamedField(field) is deprecated.");
	},
	
	clearAll: function() {
		if (pizza.valObj) {
			pizza.valObj.clearAll();
		}
	},

	/**
	 * 
	 */
	defaultDisplayErr: function(field, errMsg, clearVal) {
		if (pizza.valObj) {
			var validatorObject = pizza.valObj;
			if (clearVal === true) {
				validatorObject.clear(field);
				return;
			}
			else {
				var fieldNode = dojo.byId(field);
				var validatedType = (fieldNode.validatedType ? fieldNode.validatedType : ''); 
				var validatedField = new pizza.validation.ValidatedField({
					field: field,
					validationRule: validatedType,
					errorField: field,
					customValidation: dojo.hitch(this, function(args) {
						return { valid: !clearVal, errorMsg: errMsg };
					})
				});
				validatorObject.validateUsing(field, validatedField);
			}
		}		
	},
	
	displayErr: function(field, errMsg, clearVal) {
		pizza.Validator.defaultDisplayErr(field, errMsg, clearVal);
	},

	displayErrorsFromJson: function(errors,form) {
		dojo.deprecated("pizza.Validator#displayErrorsFromJson(errors, form) is deprecated. Please use the displayErrorsFromJson function on the instantiated validator object.");
		if (pizza.valObj) {
			pizza.valObj.displayErrorsFromJson(errors, form);
		}
	},

	validateField: function(event) {
		dojo.deprecated("pizza.Validator#validateField(event) is deprecated. Please use the validate(event) function on the instantiated validator object.");
		if (pizza.valObj) {
			var validatorObject = pizza.valObj;
			validatorObject.validate(event);
		}
	},
	
	validateAllDialogFields: function(dialogId) {
		dojo.deprecated("pizza.Validator#validateAllDialogFields(dialogId) is deprecated. Please use the validateAllDialogFields(dialogId) function on the instantiated validator object.");
		if (pizza.valObj) {
			var validatorObject = pizza.valObj;
			return validatorObject.validateAllDialogFields(dialogId);
		}
		console.error('Unable to process function: validateAllDialogFields through compatibility layer; Cause: pizza.valObj not defined.');
	},

	/**
	 * 
	 */
	validateAllFields: function(beneathNode) {
		dojo.deprecated("pizza.Validator#validateAllFields(dialogId) is deprecated. Please use the validateAllFields(node) function on the instantiated validator object.");
		if (pizza.valObj) {
			var validatorObject = pizza.valObj;
			return validatorObject.validateAllFields(beneathNode);
		}
		console.error('Unable to process function: validateAllFields through compatibility layer; Cause: pizza.valObj not defined.');
	},

	_validate: function(field, extraArgs) {
		dojo.deprecated("pizza.Validator#_validate(field) is deprecated. Please use the validateField(...) function on the instantiated validator object.");
		if (this.cancelValidation) {
			return;
		}
		
		if (pizza.valObj) {
			var validatorObject = pizza.valObj;
			if (validatorObject.has(field)) {
				return validatorObject.validateField(field);
			}
			else {
				var fieldNode = dojo.byId(field);
				var validatedType = (fieldNode.validatedType ? fieldNode.validatedType : '');
				var args = dojo.mixin({ field: field, validationRule: validatedType, ignoreBlur: true }, extraArgs);
				var validatedField = new pizza.validation.ValidatedField(args);
				return pizza.valObj.validateUsing(field, validatedField);
			}
		}
		console.error('Unable to process function: _validate through compatibility layer; Cause: pizza.valObj not defined.');
	}
};

pizza.ValidationDisplay = {
    handleBlockErr: function(field, errMsg, clearVal) {
    	dojo.deprecated("pizza.ValidationDisplay#handleBlockErr has been replaced by pizza.validation.ValidationDisplay.handleBlockError#display() and #clear() methods.");
    	if (clearVal === true) {
    		pizza.validation.ValidationDisplay.handleBlockError.clear(field);
    	}
    	else {
    		pizza.validation.ValidationDisplay.handleBlockError.display(field, errMsg);
    	}
    }
};