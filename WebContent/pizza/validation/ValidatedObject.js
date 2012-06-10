dojo.provide('pizza.validation.ValidatedObject');
dojo.require('pizza.validation.ValidationDisplay');
dojo.require('dijit._Widget');

/**
 * <p>
 * The <code>ValidatedObject</code> serves as the base <code>_Widget</code> for
 * all <tt>JSON</tt> object classes representing UI components capable of being
 * validated. It extends <tt>dijit._Widget</tt> to leverage consistent behavior
 * common Dojo's object-definition system (e.g. automatic mixin's of arguments
 * when passed through the constructor, additional life-cycle methods such as
 * <tt>postCreate</tt>, etc.).
 * </p>
 * <p>
 * The <code>ValidatedObject</code> class is a base class, not meant to be
 * directly instantiated.
 * </p>
 * <p>
 * Extending the <code>ValidatedObject</code> is possible, simply by declaring
 * a new class that extends from it. However, there are a minimum of three (3)
 * methods that must be implemented: <tt>clear</tt>, <tt>valid</tt>, and
 * <tt>validated</tt>.
 * </p>

 */
dojo.declare('pizza.validation.ValidatedObject', dijit._Widget, {

	/**
	 * By default, when the <tt>validate()</tt> method is called all validation
	 * failures will automatically display the error message associated with
	 * them. You can turn this automatic display off by setting the property
	 * value of <tt>autoDisplay</tt> to <tt>false</tt>
	 * 
	 * @var boolean
	 */
	autoDisplay: true,

	/**
	 * The custom validation function to be executed if defined during
	 * validation of an object. Custom validation functions should accept a
	 * single arguments (<tt>args</tt>) parameter that specified at a minimum
	 * the <tt>field</tt> being validated. In addition to this, it is common
	 * for a validated object to pass in whether the default validation passed
	 * its validation or not (e.g. the property <tt>valid</tt> as a result of
	 * calling <tt>valid()</tt>). In addition to this, they should return a JSON
	 * object with the following structure:
	 * <pre>
	 *   { valid: true|false, errorMsg: '' }
	 * </pre>.
	 *
	 * Where <tt>valid</tt> is whether the custom validation succeeded or failed
	 * and <tt>errorMsg</tt> is the error message to report when failure occurs.
	 * The <tt>errorMsg</tt> property should be returned even for validation
	 * that succeeds, but can be returned as an empty string (e.g. ""). This
	 * ensures object stability through standards.
	 *
	 * Example:
	 * <pre>
	 * function(args) {
	 *   ...
	 *   return { valid: true, errorMsg: '' };
	 * }
	 * </pre>
	 *
	 * @var closure 
	 */	
	customValidation: null,
	
	/**
	 * The target field for error display. This will, unless overridden at the
	 * time this validated object is instantiated (or set after the fact), be
	 * associated with the target field validation is being performed on.
	 */
	errorField: null,
	
	/**
	 * Allows for a validated object to ignore <tt>onblur</tt> events and
	 * therefore not be processed. A corresponding convenience method,
	 * <tt>ignoreBlur()</tt> is exposed to provide functional access to this
	 * property.
	 *
	 * @var boolean
	 */
	ignoreBlur: false,
	
	/**
	 * The section (if any) that this <tt>ValidatedObject</tt> is tied to. This
	 * is a reference by name to the section that this field has been added to.
	 * The actual set of sections is managed by the <tt>Validator</tt>.
	 * 
	 * @var string
	 */
	section: null,

	/**
	 * Reference to the "static" instance of the <code>ValidationDisplay</code>
	 * JSON that will be used to display and clear the validation messages for
	 * a validated field.
	 *
	 * @var Object<pizza.validation.ValidationDisplay>
	 */	
	validationDisplay: null,

	/**
	 * <p>
	 * Called automatically as part of the constructor chain managed by the
	 * Dojo Toolkit.
	 * </p>
	 * <p>
	 * Necessary because objects (<tt>arrays</tt>, JSON <tt>objects</tt>, and
	 * <tt>null</tt>) are treated by default as <i>static</i> classes by Dojo's
	 * class declaration system when they are defined outside the scope of the
	 * constructor. Redclaring them inside the scope of the constructor forces
	 * them to be evaluated on an instance-by-instance policy. Meaning that
	 * two objects of the exact same class will not share the same custom
	 * validation and validation display unless they are explicitly told to do
	 * so.
	 * </p>
	 * 
	 * @see http://dojotoolkit.org/reference-guide/dojo/declare.html#arrays-and-objects-as-member-variables
	 */
	// @Override
	constructor: function() {
		this.customValidation = null;
		this.errorField = null;
		this.section = null;
		this.sectionErrors = {};
		this.validationDisplay = null;
	},

	// @Override
	postCreate: function() {
		this.inherited(arguments);
		if (this.validationDisplay === null || this.validationDisplay === undefined) {
			this.validationDisplay = pizza.validation.ValidationDisplay.handleBlockError;
		}
	},
	
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * Adds this <tt>ValidatedObject</tt> to a section identified by the passed
	 * <tt>sectionName</tt>. If added as part of a section, 
	 * </p>
	 */
	addToSection: function(sectionName) {
		if (typeof(sectionName) != 'string') {
			console.log('Error occured when adding ValidatedObject to a validation section. The section name is expected to be a string.');
			return;
		}
		this.section = sectionName;		
	},

	/**
	 * <p>
	 * Clears the validation message for this validated object. Must be
	 * implemented by extending JSON objects.
	 * </p>
	 * 
	 * @return void
	 * @type undefined
	 */
	clear: function() {
		throw 'Validation classes extending ValidationObject must implement the clear() function.';
	},

	/**
	 * <p>
	 * Convenience method for displaying a validation message. Calling this
	 * method passes the <tt>field</tt> and <tt>message</tt> parameters to the
	 * <tt>validationDisplay</tt> property's <tt>display</tt> method.
	 * </p>
	 * 
	 * @param Node field the field that the validation message will be
	 * 			associated with.
	 * @param string message the validation message that will be displayed.
	 * @return void
	 * @type undefined 
	 */
	display: function(field, message) {
		this.validationDisplay.display(field, message);
	},
	
	getSection: function() {
		return this.section;
	},
	
	isIgnoreBlur: function() {
		return (this.ignoreBlur === true);
	},

	/**
	 * <p>
	 * The <tt>valid()</tt> method is responsible for the evaluation of an
	 * object's validity. Returning whether the object passed validation or not,
	 * as well as the error message if validation failed that is associated with
	 * the failure. This method may return any of the following, some of which
	 * are required:
	 * <ul>
	 * <li><tt>valid</tt> - Required. The valid property tells the calling
	 * 		function whether or not the object passed its validation
	 * 		testing.</li>
	 * <li><tt>errorMsg</tt> - Optional. This is the message that will be
	 * 		displayed to the user telling them the reason for the failure.
	 * 		Though optional, it is recommended that it always be returned, even
	 * 		if it is an empty string (e.g. '').</li>
	 * <li><tt>errorField</tt> - Optional. The field to target when displaying
	 * 		the error message. This is an override to the default behavior which
	 * 		uses the source field that invoked the <tt>validate()</tt> function
	 * 		as the display target. Usage of this is <i>not</i> recommended, if
	 * 		for the only purpose that it introduces a means for a developer to
	 * 		be lazy when writing their validation control logic.</li>
	 * <li><tt>sectionError</tt> - Optional. Only evaluated when a validated
	 * 		object's <tt>section</tt> is not null. If this is returned as part
	 * 		of the validation response, it will be added to an object that
	 * 		tracks section messages to display. The <tt>sectionError</tt>
	 * 		requires two properties: <tt>key</tt> and <tt>message</tt>. This is
	 * 		provided as a means for custom validation to effect the errors
	 * 		displayed as part of the section validation errors.</li> 
	 * </ul>
	 * </p>
	 *
	 * @return Object returns a JSON Object that must contain the <tt>valid</tt>
	 * 			property as well as others.
	 * @type Object
	 */
	valid: function() {
		throw 'Validation classes extending ValidatedObject must implement the valid() function.';
	},
	
	validate: function(sourceField, displayError) {
		throw 'Validation classes extending ValidatedObject must implement the validate() function.';
	}
});


/**
 * <p>
 * </p>
 * <p>
 * A number of properties may be passed into the ValidatedField's constructor,
 * they are as follows:
 * <ul>
 * <li><tt>field</tt> (Required) - denotes the field (by ID, name, or node) that
 * 		the validation will be added for.</li>
 * <li><tt>validationRule</tt> (Default: <i>empty string</i>)- the
 * 		comma-delimited validation rules to apply to the validated field.</li>
 * <li><tt>autoDisplay</tt> (Default: <tt>true</tt>) - if set to <tt>false</tt>
 * 		this property turns off the automatic error message display for a
 * 		validated fields upon calling their <tt>validate</tt> function.</li>
 * <li><tt>ignoreBlur</tt> (Default: <tt>false</tt>) - normally fields are
 * 		validated when a user agent focus shifts to another element (e.g.
 * 		"blurs" off the current input element). Setting this property to
 * 		<tt>true</tt> will make it so the validated object does not invoke the
 * 		validate function on blur events.</li>
 * <li><tt>validationDisplay</tt> (Default: handleBlockError) - the validation
 * 		display Object to use when displaying validation errors.</li>
 * </ul>
 * </p>
 *

 */
dojo.declare('pizza.validation.ValidatedField', pizza.validation.ValidatedObject, {

	/**
	 * The field that is being validated. This is the actual reference to the
	 * DOM node that the validation is being applied to.
	 * @var Node
	 */
	field: null,
	
	/**
	 * The validation rule for the field being validated.
	 * @var String
	 */
	validationRule: '',
	
	/**
	 * <p>
	 * Called automatically as part of the constructor chain managed by the
	 * Dojo Toolkit.
	 * </p>
	 * <p>
	 * Necessary because objects (<tt>arrays</tt>, JSON <tt>objects</tt>, and
	 * <tt>null</tt>) are treated by default as <i>static</i> classes by Dojo's
	 * class declaration system when they are defined outside the scope of the
	 * constructor. Redclaring them inside the scope of the constructor forces
	 * them to be evaluated on an instance-by-instance policy.
	 * </p>
	 * 
	 * @see http://dojotoolkit.org/reference-guide/dojo/declare.html#arrays-and-objects-as-member-variables
	 */
	// @Override
	constructor: function() {
		this.field = null;
	},

	/**
	 * 
	 */
	postCreate: function() {
		this.inherited(arguments);

		// **
		// If the field associated with this validated object is a string at
		// this point in time, we need to convert it to a DOMNode. This is
		// primarily because we rely on the to do some of our processing.
		if (typeof(this.field) === 'string') {
			this.field = dojo.byId(this.field);
		}

		if (this.hasValidationRule()) {
			this.field['validatedType'] = this.validationRule;
		}

		// **
		// The error field for this validated object is associated with the
		// field passed in by default. This may be overridden. 		
		if (this.errorField === null || this.errorField === undefined) {
			this.errorField = this.field;
		}
	},
	
	// -------------------------------------------------------------------------

	/**
	 * 
	 */
	// @Override
	clear: function(field) {
		if (field === undefined || field === null) {
			field = this.field;
		}
		this.validationDisplay.clear(field);
	},
	
	/**
	 * 
	 */
	getField: function() {
		return this.field;
	},

	/**
	 * <p>
	 * Convenience method for acquiring the <tt>id</tt> property for this
	 * <code>ValidatedField</code>.
	 * </p>
	 * 
	 * @return string|null the id of the field associated with this validation
	 * 			object if it is set. Otherwise, returns <tt>null</tt>.
	 * @type string|null
	 */
	getId: function() {
		var id = this.field.id;
		if (id === undefined || id === null) {
			console.log('Failed to return id for ValidatedField. Could not find attribute: id.');
			return null;
		}
		return id;
	},

	/**
	 * <p>
	 * Convenience method for acquiring the <tt>name</tt> property for this
	 * <code>ValidatedField</code>.
	 * </p>
	 * 
	 * @return string|null the name of the field associated with this validation
	 * 			object if it is set. Otherwise, returns <tt>null</tt>.
	 * @type string|null
	 */
	getName: function() {
		var _name = this.field.name;
		if (_name === undefined || _name === null) {
			console.log('Failed to return name for ValidatedField. Could not find attribute: name.');
			return null;
		}
		return _name;
	},

	/**
	 * <p>
	 * Returns whether or not this <code>ValidatedField</code> has a validation
	 * rule associated with it or not. If the validation rule is: <tt>null</tt>,
	 * <tt>undefined</tt>, or an empty string, this function will return
	 * <tt>false</tt>. Otherwise, it returns <tt>true</tt> indicating that a
	 * validation rule exists.
	 * </p>
	 * 
	 * @return boolean <tt>true</tt> if a validation rule exists. Otherwise,
	 * 			<tt>false</tt>.
	 * @type boolean 
	 */
	hasValidationRule: function() {
		if (this.validationRule === undefined || this.validationRule === null || this.validationRule.length == 0) {
			return false;
		}
		return true;
	},

	/**
	 * <p>
	 * Performs the actual, logical, evaluation of a field's validity returning
	 * whether the field is valid or not, and the associated error message. The
	 * field will not be validated against if it is disabled, or if its
	 * validation type is empty.
	 * </p>
	 * <p>
	 * Example:
	 * <pre>
	 * valid: function() {
	 * 	 ...
	 *   return { valid: true, errorMsg: 'Required field' };
	 * }
	 * </pre>
	 * </p>
	 *
	 * @return Object returns a JSON Object that must contain both the
	 * 			<tt>valid</tt> and <tt>errorMsg</tt> properties.
	 * @type Object
	 */
	valid: function() {
		var fieldValue = this.field.value;
		
		// **
		// If no validationType attribute has been added to the field we're
		// validating,
		if (!this.field['validatedType'] || this.field['validatedType'].length == 0
				|| this.field.disabled == true || this.field.disabled == 'disabled') {
			return { valid: true, errorMsg: '' };
		}
		
		// **
		// If the field is specified as a "do not require" (dnr) field, then we
		// ignore it.
		var fieldClass = this.field.className;
		if (fieldClass == 'ifield_dnr' && dojo.string.trim(fieldValue).length == 0) {
			return { valid: true, errorMsg: '' };
		}

		var validateAgainst = this.validationRule.split(',');
		for(i = 0; i < validateAgainst.length; i++){
			var type = validateAgainst[i].replace(/^\s+|\s+$/g,"");
			if (typeof(type) == "string" && type.length > 0) {
				type = pizza.validationTypes[type];
				if (!type) {
					console.log("Bad validation type! <<"+type+">>  Check your model object & make sure it has the correct annotation.");
				}
				
				// Should the field's value be trimmed before applying validation?
				if (!type['noTrim'] || !type['noTrim'] == true) {
					fieldValue = (fieldValue).replace(/^\s+|\s+$/g,"");
				}
	
				var handler = type["typeName"];
				if (pizza.validation.ValidationHandler[handler]
						&& !pizza.validation.ValidationHandler[handler].validate(type, fieldValue)) {
					return { valid: false, errorMsg: type["errorKey"] };
				}
				
				// **
				// If a custom validation function has been supplied for this
				// particular validated type, we should run it against the current
				// field. This is a validation function is at the level of a
				// [potentially] shared validated type. Therefore it is a more
				// "global" custom validation. This is as opposed to the custom
				// validation function that can optionally be set at the level of
				// the ValidatedObject. 
				if (type['validateFunction']) { 
					if (!type['validateFunction'](this.field)) {
						return { valid: false, errorMsg: type['errorKey'] };
					}
				}
			}
		}
		return { valid: true, errorMsg: '' };
	},

	/**
	 * <p>
	 * Validates against the field modeled by this <code>ValidatedObject</code>,
	 * running the default validation first--following it by the custom
	 * validation function if it was defined.
	 * </p>
	 * <p>
	 * Validation is performed and a validation response is returned that
	 * contains whether or not the field is valid, the error message, and the
	 * field that will be used as the target for the display of that error
	 * message (this is optional). We assume, provided that the property
	 * <tt>errorField</tt> is not returned in the validation response, that the
	 * field receiving the error message is the error field for the registered
	 * field on this object.
	 * </p>
	 * 
	 * @param Node sourceField the Node that is the source of the validation
	 * 			process being run. For ValidationField objects this value is
	 * 			ignored.
	 * @param string againstType Optional. Overrides the type(s) that this field
	 * 			is being validated against.
	 * @return boolean <tt>true</tt> if the ValidatedField passes the validation
	 * 			of its assigned rules. Otherwise returns <tt>false</tt>.
	 */
	validate: function(sourceField, displayError) {
		// ** Default the display option to TRUE
		if (displayError === null || displayError === undefined) {
			displayError = true;
		}

		var validationResponse = this.valid();
		if (dojo.isFunction(this.customValidation)) {
			var customArgs = {
				field: sourceField,
				valid: validationResponse.valid,
				errorMsg: validationResponse.errorMsg
			};
			validationResponse = this.customValidation(customArgs);
		}

		// **
		// If the "errorField" property is present on the validation response,
		// use it as the target field to receive the error message. 
		var errorField = this.errorField;
		if (validationResponse.errorField) {
			errorField = validationResponse.errorField; 
		}

		// ** Handle the validation response if it returned invalid.
		if (!validationResponse.valid) {
			if (displayError === true && this.autoDisplay === true) {
				this.display(errorField, validationResponse.errorMsg);
			}
			return false;
		}
		this.clear(errorField);
		return true;
	}
});











dojo.declare('pizza.validation.ValidatedElement', pizza.validation.ValidatedObject, {

	field: null,
	
	validationRule: '',
	
	validationTypes: [],
	validationHandlers:[],

	
	// @Override
	constructor: function() {
		this.field = null;
	},

	postCreate: function() {
		this.inherited(arguments);

		if (typeof(this.field) === 'string') {
			this.field = dojo.byId(this.field);
		}

		if (this.hasValidationRule()) {
			this.field['validatedType'] = this.validationRule;
			
			var validateAgainst = this.validationRule.split(',');
			for(i = 0; i < validateAgainst.length; i++){
				var typeLabel = validateAgainst[i].replace(/^\s+|\s+$/g,"");
				if (typeof(typeLabel) == "string" && typeLabel.length > 0) {
					type = pizza.validationTypes[typeLabel];
					if (!type) {
						console.log("Bad validation type! <<"+type+">>  Check your model object & make sure it has the correct annotation.");
					}
					
					var handler = type["typeName"];
					var validationHandler = pizza.validation.ValidationHandler[handler];
					if (!validationHandler) {
						console.log("No hadler found for validation type! <<"+type+">>.");;
					}
					
					this.validationTypes[typeLabel] = type;
					this.validationHandlers[typeLabel] = validationHandler;
					
				}
			}
		}

		if (this.errorField === null || this.errorField === undefined) {
			this.errorField = this.field;
		}
	},
	
	
	doValidationOnType: function(typeLabel){
		var type = this.validationTypes[typeLabel];
		var elementValue = this.getElementValue();
		var validationHandler = this.validationHandlers[typeLabel];
		var isValid =  validationHandler.validate(type, elementValue);
//		return { valid: isValid, errorMsg: type["errorKey"] }
		return isValid;
	},
	
	
	// @Override
	clear: function(field) {
		if (field === undefined || field === null) {
			field = this.field;
		}
		this.validationDisplay.clear(field);
	},
	
	getField: function() {
		return this.field;
	},
	
	getElementValue: function() {
		return dojo.query(">", this.field);
	},

	getId: function() {
		var id = this.field.id;
		if (id === undefined || id === null) {
			console.log('Failed to return id for ValidatedField. Could not find attribute: id.');
			return null;
		}
		return id;
	},

	getName: function() {
		var _name = this.field.name;
		if (_name === undefined || _name === null) {
			console.log('Failed to return name for ValidatedField. Could not find attribute: name.');
			return null;
		}
		return _name;
	},

	hasValidationRule: function() {
		if (this.validationRule === undefined || this.validationRule === null || this.validationRule.length == 0) {
			return false;
		}
		return true;
	},

	valid: function() {
		// immediate children of the given element
//		var fieldValue = dojo.query(">", this.field);
		var fieldValue = this.getElementValue();
		
		if (!this.field['validatedType'] || this.field['validatedType'].length == 0
				|| this.field.disabled == true || this.field.disabled == 'disabled') {
			return { valid: true, errorMsg: '' };
		}
		
//		var fieldClass = this.field.className;


		var validateAgainst = this.validationRule.split(',');
		for(i = 0; i < validateAgainst.length; i++){
			var type = validateAgainst[i].replace(/^\s+|\s+$/g,"");
			if (typeof(type) == "string" && type.length > 0) {
				type = pizza.validationTypes[type];
				if (!type) {
					console.log("Bad validation type! <<"+type+">>  Check your model object & make sure it has the correct annotation.");
				}
				
				var handler = type["typeName"];
				if (pizza.validation.ValidationHandler[handler]
						&& !pizza.validation.ValidationHandler[handler].validate(type, fieldValue)) {
					return { valid: false, errorMsg: type["errorKey"] };
				}
				
				if (type['validateFunction']) { 
					if (!type['validateFunction'](this.field)) {
						return { valid: false, errorMsg: type['errorKey'] };
					}
				}
			}
		}
		return { valid: true, errorMsg: '' };
	},

	validate: function(sourceField, displayError) {
		// ** Default the display option to TRUE
		if (displayError === null || displayError === undefined) {
			displayError = true;
		}

		var validationResponse = this.valid();
		if (dojo.isFunction(this.customValidation)) {
			var customArgs = {
				field: sourceField,
				valid: validationResponse.valid,
				errorMsg: validationResponse.errorMsg
			};
			validationResponse = this.customValidation(customArgs);
		}

		var errorField = this.errorField;
		if (validationResponse.errorField) {
			errorField = validationResponse.errorField; 
		}

		if (!validationResponse.valid) {
			if (displayError === true && this.autoDisplay === true) {
				this.display(errorField, validationResponse.errorMsg);
			}
			return false;
		}
		this.clear(errorField);
		return true;
	}
});













/**
 * <p>
 * Radio and checkboxes that are treated together as a grouped option field
 * that have one or more validation types associated with them are always
 * considered to be <tt>required</tt> fields. This means that unless the radio
 * field is disabled or has had validation removed from it, they will always be
 * evaluated as being required, in addition to whatever other validation schemes
 * have been applied to them.
 * </p>
 
 */
dojo.declare('pizza.validation.ValidatedOptionGroup', pizza.validation.ValidatedObject, {
	
	/**
	 * An array of fields that will be validated against as a group when
	 * validation is invoked on this object.
	 * @var array<ValidatedFields>
	 */
	fields: [],
	
	/**
	 * The number of validation passes that have been made for these validated
	 * radio fields.
	 * @var map<string, int> 
	 */
	validationPasses: {},
	
	/**
	 * <p>
	 * Called automatically as part of the constructor chain managed by the
	 * Dojo Toolkit.
	 * </p>
	 * <p>
	 * Necessary because objects (<tt>arrays</tt>, JSON <tt>objects</tt>, and
	 * <tt>null</tt>) are treated by default as <i>static</i> classes by Dojo's
	 * class declaration system when they are defined outside the scope of the
	 * constructor. Redclaring them inside the scope of the constructor forces
	 * them to be evaluated on an instance-by-instance policy. Meaning that
	 * grouped radio fields will not inadvertenly share the number of fields
	 * and validation pass counts.
	 * </p>
	 * 
	 * @see pizza.validation.ValidatedObject#constructor()
	 * @see http://dojotoolkit.org/reference-guide/dojo/declare.html#arrays-and-objects-as-member-variables
	 */
	// @Override
	constructor: function() {
		this.fields = [];
		this.validationPasses = {};
	},

	postCreate: function() {
		this.inherited(arguments);
		for(var i = 0; i < this.fields.length; i++) {
			var fieldId = this.fields[i].getId();
			this.validationPasses[fieldId] = 0;
		}
		
		// **
		// Associated the error field for the validated option group to with the
		// first option field. This is set only if not explicitly declared upon
		// instantiation of the validated object. 
		if (this.errorField === null || this.errorField === undefined) {
			if (this.fields.length > 0) {
				this.errorField = this.fields[0].getField();
			}
		}
	},
	
	// -------------------------------------------------------------------------
	
	/**
	 * 
	 */
	add: function(fieldArgs) {
		if (!fieldArgs.field) {
			console.log('Required argument exception in ValidatedOptionGroup: Unable to add field, required argument missing: field');
			return;
		}
		var theField = fieldArgs.field;
		var validatedField = new pizza.validation.ValidatedField(fieldArgs);

		// **
		// If we are adding a new field and it will be the first field of the
		// validated option group, then we should assign it to the error field
		// as well, provided an error field was not already specified. 		
		if (this.fields.length == 0 && (this.errorField === null || this.errorField === undefined)) {
			this.errorField = theField;
		}

		// ** Add the field to the array, and instantiate the validation passes.
		this.fields.push(validatedField);
		this.validationPasses[theField.id] = 0;
	},
	
	allValidated: function() {
		var passed = true;
		for(var item in this.validationPasses) {
			var count = this.validationPasses[item];
			if (count < 1) {
				passed = false;
			}
		}
		return passed;	
	},

	clear: function() {
		this.validationDisplay.clear(this.fields[0].getField());
		for (var item in this.validationPasses) {
			this.validationPasses[item] = 0;
		}
	},

	/**
	  <p>
	 * Radio fields are a special type of grouped element. For one they are an
	 * option box that can be turned <tt>on</tt> or <tt>off</tt>, and their
	 * behavior is such that turning on one option turns off the others in the
	 * group. This means that it is impossible to know whether the group of
	 * radio fields has a valid value associated with it until we have iterated
	 * over each field. One element of a radio field may be invalid (e.g. not
	 * selected, but with the <tt>required</tt> validation type), while another
	 * in the grouping is valid.
	 * </p> 
	 * In addition to complications in the validation scheme from elements
	 * described above, disabled fields introduce further headaches. Though it
	 * is unlikely, a group of radio fields may consist of any combination of
	 * enabled and disabled fields. If a field is disabled, it will be ignored
	 * by the validation process. If it is enabled however, we only validate
	 * the field if it is currently checked (no sense in validating a field that
	 * is not selected). As long as the radio button has a value selected it
	 * meets the validation condition that it is required. If it fails this
	 * condition an error message may be displayed.
	 * </p> 
	 */
	valid: function() {
		var valid = true;
		var errorMessage = "";
		var meetsRequiredCondition = false;
		var skippedFields = 0;

		for (var fieldIdx = 0; fieldIdx < this.fields.length; fieldIdx++) {
			var target = this.fields[fieldIdx];
			var field = target.getField();
			var skipField = false;

			// **
			// If the current field does not have a validated type associated
			// with it (e.g. its been removed) or the input field has been
			// disabled, we want to skip validation against this particular
			// element in the group of radio buttons.
			if (!field['validatedType'] || field['validatedType'].length == 0
					|| field.disabled == true || field.disabled == 'disabled') {
				skipField = true;
				skippedFields++;
			}

			if (!skipField) {
				if (field.checked == true) {
					var validationResponse = target.valid();
					meetsRequiredCondition = true;
					if (!validationResponse.valid) {
						valid = false;
						errorMessage = validationResponse.errorMsg;
						break;
					}
				}
			}
		}

		if (meetsRequiredCondition == false && skippedFields < this.fields.length) {
			errorMessage = pizza.getMsg("validation.required");
			return { valid: false, errorMsg: errorMessage };
		}
		return { valid: valid, errorMsg: errorMessage };
	},

	/**
	 * <p>
	 * Validates this <code>ValidatedOptionGroup</code> with respect to a
	 * <tt>sourceField</tt>. The source field allows us to know which element of
	 * the radio field group is being validated, and then allows us to determine
	 * whether or not it is appropriate to show any validation error displays.
	 * An error validation display will be shown when the selection is invalid
	 * and all elements in the radio field group have been validated at least
	 * once.
	 * </p>
	 *
	 * @param Node sourceField the Node that is the source of the validation
	 * 			process being run. For ValidatedOptionGroup objects this field is
	 * 			used to determine which of the radio fields is getting validatedvalue is
	 * 			used to determine ignored.
	 * @return boolean whether this group returned as valid or not.
	 * @type boolean
	 */
	validate: function(sourceField, displayError) {
		// ** Default the display option to TRUE
		if (displayError === null || displayError === undefined) {
			displayError = true;
		}

		var validationResponse = this.valid();
		if (dojo.isFunction(this.customValidation)) {
			var customArgs = {
				field: sourceField,
				valid: validationResponse.valid,
				errorMsg: validationResponse.errorMsg
			};
			validationResponse = this.customValidation(customArgs);
		}
		this.validationPasses[sourceField.id]++;

		if (this.allValidated()) {
			if (!validationResponse.valid) {
				var errorField = this.errorField;
				if (displayError === true && this.autoDisplay === true) {
					this.display(errorField, validationResponse.errorMsg);
				}
			}
			else {
				this.clear();
			}
		}
		return validationResponse.valid;
	}
});


/**
 * <p>
 * A ValidationGroup is a grouping of <tt>ValidatedField</tt>s that can be
 * treated as a single unit when they are processed for validation. Validation
 * can be performed at two levels with a ValidatedGroup: once at the level of
 * the ValidatedField (the default mechanism) and optionally at the level of the
 * group itself. Further, 
 * </p>

 */
dojo.declare('pizza.validation.ValidatedGroup', pizza.validation.ValidatedObject, {
	
	/**
	 * A JSON map of a validated field (keyed by the <tt>field</tt>'s ID).
	 * Inside of this object notation are the following properties:
	 *   - target, this is the reference to the ValidatedObject.
	 *   - count, the number of times that this object has been validated.
	 * @var Object<string, ValidatedObject>
	 */
	fields: {},
	
	/**
	 * The name of the field that is the "master" field for this group. This is
	 * the field that the error display element will be associated with.
	 * @var string
	 */
	masterField: '',
	
	/**
	 * 
	 */
	resetOnClear: false,

	/**
	 * <p>
	 * Called automatically as part of the constructor chain managed by the
	 * Dojo Toolkit.
	 * </p>
	 * <p>
	 * Necessary because objects (<tt>arrays</tt>, JSON <tt>objects</tt>, and
	 * <tt>null</tt>) are treated by default as <i>static</i> classes by Dojo's
	 * class declaration system when they are defined outside the scope of the
	 * constructor. Redclaring them inside the scope of the constructor forces
	 * them to be evaluated on an instance-by-instance policy. Meaning that
	 * grouped fields will not inadvertenly share the same exact fields as other
	 * grouped fields previously declared.
	 * </p>
	 * 
	 * @see pizza.validation.ValidatedObject#constructor()
	 * @see http://dojotoolkit.org/reference-guide/dojo/declare.html#arrays-and-objects-as-member-variables
	 */
	constructor: function() {
		this.fields = {};
	},

	/**
	 * <p>
	 * When created, a developer can instantiate the validated fields that are
	 * grouped by this object due to the behavioral mixin that is inherited from
	 * the <tt>_Widget</tt> class. The fields are defensively parsed to ensure
	 * that they are valid, no matter what was passed in. The fields may be
	 * passed in as an array of strings, fields, or validated fields.
	 * Furthermore developers have the capacity to pass in the fields as the
	 * validation group expects them (e.g. <tt>{ target: validatedField,
	 * count: 0 }</tt>).
	 * </p>
	 *
	 * @return void
	 * @type undefined 
	 */
	// @Override
	postCreate: function() {
		this.inherited(arguments);
		// **
		// Defensively fixes fields that have been passed in as an array, which
		// conceivably is something that could happen if the developer is
		// manually created a ValidatedOptionGroup. 		
		if (dojo.isArray(this.fields)) {
			var _fieldsFixed = {};
			var size = this.size();
			for(var i = 0; i < size; i++) {
				var current = this.fields[i];
				var key = current.getId();
				_fieldsFixed[key] = { target: current, count: 0 };
			}
			this.fields = _fieldsFixed;
		}
		else {
			for (var fieldKey in this.fields) {
				this.fields[fieldKey].count = 0;
			}
		}

		// **
		// Associates the master field with the error field by default for this
		// validation group if an error field has not been explicitly specified.
		if (this.errorField === null || this.errorField === undefined) {
			this.errorField = dojo.byId(this.masterField);
		}
	},
	
	// -------------------------------------------------------------------------
	
	/**
	 * 
	 */
	add: function(fieldArgs) {
		if (!fieldArgs.field) {
			console.log('Required argument missing exception ValidatedGroup: Unable to add validated field, missing required argument: field');
			return;
		}

		var theField = fieldArgs.field;
		if ((typeof(theField) == 'object') && (theField instanceof pizza.validation.ValidatedField)) {
			var fieldId = theField.getId();
			this.fields[fieldId] = { target: theField, count: 0 };
		}
		else {
			// ** Assume that a DOM node was passed...
			var node = dojo.byId(theField);
			var validatedField = new pizza.validation.ValidatedField(fieldArgs);
			this.fields[node.id] = { target: validatedField, count: 0 };
		}
	},

// Superfluous? Doesn't appear to be used anywhere. Must have been
// a partial thought that didn't get cleaned up on initial checkin
// of the validator framework. Leaving it in here tentatively in
// case we need to do a quick re-instantiation of this function. 
//	/**
//	 * @param closure validationFunc
//	 */
//	// @Override
//	addCustomValidation: function(validationFunc) {
//		for (var field in this.fields) {
//			this.fields[field].target.addCustomValidation(validationFunc);
//		}		
//	},

	/**
	 * 
	 */
	allValidated: function() {
		var passed = true;
		for(var item in this.fields) {
			if (this.fields[item].count < 1) {
				passed = false;
			}
		}
		return passed;	
	},

	clear: function(field) {
		if (this.size() > 0) {
			if (field === undefined || field === null) {
				var masterTarget = this.fields[this.masterField].target;
				if (masterTarget) {
					field = masterTarget.getField();
				}
				else {
					console.info("No field was specified to clear the error out of.");
				}
			}
			
			this.validationDisplay.clear(field);
			if (this.resetOnClear === true) {
				for (var fieldName in this.fields) {
					this.fields[fieldName].count = 0;
				}
			}
		}
	},
	
	size: function() {
		var size = 0;
		for (var field in this.fields) {
			size += 1;
		};
		return size;
	},

	/**
	 *
	 */
	valid: function() {
		var valid = true;
		var errorMessage = "";
		var skippedFields = 0;
		
		for (var fieldName in this.fields) {
			var target = this.fields[fieldName].target;
			var field = target.getField();
			var skipField = false;

			if (!field['validatedType'] || field['validatedType'].length == 0
					|| field.disabled == true || field.disabled == 'disabled') {
				skipField = true;
				skippedFields++;
			}

			if (!skipField) {
				var validationResponse = target.valid();
				if (!validationResponse.valid) {
					valid = false;
					errorMessage = validationResponse.errorMsg;
					break;
				}
			}
		}
		return { valid: valid, errorMsg: errorMessage };
	},

	/**
	 * 
	 */
	validate: function(sourceField, displayError) {
		// **
		// BUGFIX DEV#1418: If the validated group is empty (e.g. no fields to
		//     validated) than by default it is valid. 
		if (this.size() < 1) {
			return true;
		}

		// ** Default the display option to TRUE
		if (displayError === null || displayError === undefined) {
			displayError = true;
		}

		var validationResponse = this.valid();
		if (dojo.isFunction(this.customValidation)) {
			var customArgs = {
				field: sourceField,
				valid: validationResponse.valid,
				errorMsg: validationResponse.errorMsg
			};
			validationResponse = this.customValidation(customArgs);
		}
		// **
		// Before we increment the count for the sourceField
		//     based on its ID within the context of the fields grouped together
		//     we need to be certain that it exists. The only time it might not
		//     exist is when there are conflicting fields with the same ID/name
		//     on the form. The defect that generated this problem is
		//     reproducible with a feed provider (e.g. Member type is HP) when
		//     opening a dialog with grouped fields. There is a conflict where
		//     two elements share not only the same name, but the same ID. This
		//     is actually bad form for the structure of an HTML document--all
		//     IDs should be unique--in addition it causes problems with the
		//     validation framework. There are other ways to get around this
		//     problem which was compounded in a change to the Validator object,
		//     it can be partially fixed by allowing us to grab the first form
		//     element only (if multiple are returned) and we haven't already
		//     committed ourselves to grouping fields as a set of option groups.
		//     However that doesn't address the larger concern that fields are
		//     breaking HTML rules.    
		if (this.fields[sourceField.id]) {
			this.fields[sourceField.id].count++;
		}

		if (this.allValidated()) {
			if (!validationResponse.valid) {
				// **
				// If the "errorField" property is present on the validation
				// response, use it as the target field to receive the error
				// message. 
				var errorField = this.errorField;
				if (validationResponse.errorField) {
					errorField = validationResponse.errorField; 
				}

				if (displayError === true && this.autoDisplay === true) {
					this.display(errorField, validationResponse.errorMsg);
				}
			}
			else {
				this.clear(validationResponse.errorField);
			}
		}
		return validationResponse.valid;
	}
});


/**
 * <p>
 * The <code>ValidationWidget</code> has two uses
 */
dojo.declare('pizza.validation.ValidatedWidget', pizza.validation.ValidatedObject, {

	/**
	 * The validation rule for the field being validated.
	 * @var String
	 */
	validationRule: '',
	
	/**
	 * The widget that is being validated, this property <i>may</i> be
	 * <tt>null</tt>. Widgets may extend this object to make use of the provided
	 * functionality directly.
	 * @var dijit._Widget 
	 */
	_widget: null,
	
	postCreate: function() {
		this.inherited("postCreate", arguments);

		// **
		// The error field for this validated object is associated with the
		// field passed in by default. This may be overridden. 		
		if (this.errorField === null || this.errorField === undefined) {
			this.errorField = this._getDefaultErrorField();
		}
	},

	// -------------------------------------------------------------------------

	_getDefaultErrorField: function() {
		var field = '';
		if (this._widget !== null && this._widget !== undefined
				&& (this._widget instanceof dijit._Widget)) {
			field = this._widget.get('id') + '.errors';
			return field;
		}
		return this.get('id') + '.errors';
	},

	/**
	 * 
	 */
	// @Override
	clear: function(field) {
		if (field === undefined || field === null) {
			field = this._getDefaultErrorField();
		}

		this.validationDisplay.clear(field);
	},

	/**
	 * 
	 */
	getField: function() {
		console.log('pizza.validation.ValidatedWidget - Cannot resolve getField() for ValidatedWidget yet. I\'m sorry');
		return null;
	},

	/**
	 * <p>
	 * Convenience method for acquiring the <tt>id</tt> property for this
	 * <code>ValidatedObject</code>. <code>ValidatedWidget</code>s function a
	 * little bit differently as the widget being validated may either be an
	 * extension of this object itself, or may have a widget mixed into it and
	 * be treated like a field.
	 * </p>
	 * 
	 * @return string|null the id of the field associated with this validation
	 * 			object if it is set. Otherwise, returns <tt>null</tt>.
	 * @type string|null
	 */
	getId: function() {
		if (this._widget !== null && this._widget !== undefined
				&& (this._widget instanceof dijit._Widget)) {
			return this._widget.get('id');
		}
		return this.get('id');
	},

	/**
	 * <p>
	 * Returns whether or not this <code>ValidatedField</code> has a validation
	 * rule associated with it or not. If the validation rule is: <tt>null</tt>,
	 * <tt>undefined</tt>, or an empty string, this function will return
	 * <tt>false</tt>. Otherwise, it returns <tt>true</tt> indicating that a
	 * validation rule exists.
	 * </p>
	 * 
	 * @return boolean <tt>true</tt> if a validation rule exists. Otherwise,
	 * 			<tt>false</tt>.
	 * @type boolean 
	 */
	hasValidationRule: function() {
		if (this.validationRule === undefined || this.validationRule === null || this.validationRule.length == 0) {
			return false;
		}
		return true;
	},

	getWidgetValue: function() {
		return this._widget.value;
	},
	/**
	 * <p>
	 * Like all other validation objects, the <tt>valid</tt> method governs the
	 * actual evaluation of whether a widget is valid or not. Since a
	 * <code>ValidatedWidget</code> can be extended as part of the
	 * <tt>dojo.declare</tt> syntax you can (and should) implement the
	 * <tt>valid</tt> function when you declare a widget or when you instantiate
	 * a new one. Like all validated objects, the return value of the
	 * <code>ValidatedWidget</code>'s <tt>valid</tt> method should be a JSON
	 * object consisting of at a minimum the properties: <tt>valid</tt> and
	 * <tt>errorMsg</tt>.
	 * </p>
	 * <p>
	 * If you want to specify a <tt>valid</tt> function that will be evaluated
	 * for all widgets of a declared type, you should override the base
	 * <code>ValidatedWidget</code>'s function of the same name, for example:
	 * <pre>
	 * dojo.declare('MyWidget', pizza.validation.ValidatedWidget, {
	 *   ...
	 *   
	 *   valid: function() {
	 *      ...
	 *      return { valid: true, errorMessage: 'Required field' };
	 *   }
	 * }
	 * </pre>
	 * </p>
	 * <p>
	 * To specify a <tt>valid</tt> function at the time you instantiate a widget
	 * of a particular type that inherits behavior from the validated widget
	 * class, you can declare it as follows:
	 * <pre>
	 * var myWidget = new MyWidget({
	 *   ...
	 *   valid: function() {
	 * 	   ...
	 *     return { valid: true, errorMsg: 'Required field' };
	 *   }
	 * });
	 * </pre>
	 * </p>
	 *
	 * @return Object returns a JSON Object that must contain both the
	 * 			<tt>valid</tt> and <tt>errorMsg</tt> properties.
	 * @type Object
	 */
	valid: function() {
		var fieldValue = this.getWidgetValue();
		
		
//		if (this.hasValidationRule()) {
//			this.field['validatedType'] = this.validationRule;
//		}
//		
//		if (!this.field['validatedType'] || this.field['validatedType'].length == 0
//				|| this.field.disabled == true || this.field.disabled == 'disabled') {
//			return { valid: true, errorMsg: '' };
//		}
		
//		var fieldClass = this.field.className;


		var validateAgainst = this.validationRule.split(',');
		for(i = 0; i < validateAgainst.length; i++){
			var type = validateAgainst[i].replace(/^\s+|\s+$/g,"");
			if (typeof(type) == "string" && type.length > 0) {
				type = pizza.validationTypes[type];
				if (!type) {
					console.log("Bad validation type! <<"+type+">>  Check your model object & make sure it has the correct annotation.");
				}
				
				var handler = type["typeName"];
				if (pizza.validation.ValidationHandler[handler]
						&& !pizza.validation.ValidationHandler[handler].validate(type, fieldValue)) {
					return { valid: false, errorMsg: type["errorKey"] };
				}
				
				if (type['validateFunction']) { 
					if (!type['validateFunction'](this.field)) {
						return { valid: false, errorMsg: type['errorKey'] };
					}
				}
			}
		}
		return { valid: true, errorMsg: '' };
	},

	/**
	 * <p>
	 * Validates against the field modeled by this <code>ValidatedObject</code>,
	 * running the default validation first--following it by the custom
	 * validation function if it was defined.
	 * </p>
	 * <p>
	 * Validation is performed and a validation response is returned that
	 * contains whether or not the field is valid, the error message, and the
	 * field that will be used as the target for the display of that error
	 * message (this is optional). We assume, provided that the property
	 * <tt>errorField</tt> is not returned in the validation response, that the
	 * field receiving the error message is the error field for the registered
	 * field on this object.
	 * </p>
	 * 
	 * @param Node sourceField the Node that is the source of the validation
	 * 			process being run. For ValidationField objects this value is
	 * 			ignored.
	 * @param string againstType Optional. Overrides the type(s) that this field
	 * 			is being validated against.
	 * @return boolean <tt>true</tt> if the ValidatedField passes the validation
	 * 			of its assigned rules. Otherwise returns <tt>false</tt>.
	 */
	validate: function(sourceField, displayError) {
		// ** Default the display option to TRUE
		if (displayError === null || displayError === undefined) {
			displayError = true;
		}

		var validationResponse = this.valid();
		if (dojo.isFunction(this.customValidation)) {
			var customArgs = {
				field: sourceField,
				valid: validationResponse.valid,
				errorMsg: validationResponse.errorMsg
			};
			validationResponse = this.customValidation(customArgs);
		}

		// **
		// If the "errorField" property is present on the validation response,
		// use it as the target field to receive the error message. 
		var errorField = this.errorField;
		if (validationResponse.errorField) {
			errorField = validationResponse.errorField; 
		}

		// ** Handle the validation response if it returned invalid.
		if (!validationResponse.valid) {
			if (displayError === true && this.autoDisplay === true) {
				this.display(errorField, validationResponse.errorMsg);
			}
			return false;
		}
		this.clear(errorField);
		return true;
	}
});