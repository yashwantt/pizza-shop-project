dojo.provide('pizza.validation.Validator');
dojo.require('pizza.validation.ValidatedObject');
dojo.require('pizza.validation.ValidationDisplay');

/**
 * <p>
 * The <code>Validator</code> is the central object that should be used to
 * manage validated fields, option groups, widgets, etc. This re-design of the
 * validator from an earlier incarnation bases its operation on the concept that
 * all validated elements are reflected by a JavaScript object. Previous
 * versions of the validator were simpler (to some degree)--at least in their
 * design--since they simply used the <tt>DOMNode</tt> as the basis of their
 * object. This was fine for simple single-field validation, but when trying
 * to handle grouped fields it was very difficult to manage; requiring a lot of
 * custom code and magic to make certain things happen. This may have been a
 * result of developers not understanding how the validation was intended to be
 * used or it may have reflected the simpler design.
 * </p>
 * <p>
 * The use of objects (in addition to DOMNodes) allows for a more robust client-
 * side validation framework. Allowing us to explicitly handle field and group
 * validation as they should be handled: differently from one another.
 * </p>
 * <p>
 * The validator has a few requirements when it is taking aa field which will
 * have validation added to it: that is the field in question have either an
 * ID or a name attribute. The absence of both of these attributes is an
 * anathema to the validator as it uses one (or both) of these properties as a
 * means of indexing the validated field objects. There are ways to get around
 * this requirement if absolutely necessary but that requires you to manage your
 * own validated objects outside of a validator. Operating outside of an
 * instantiated <code>Validator</code> however is considered to be an improper
 * usage of the validation framework.
 * </p>
 * <p>
 * The <code>Validator</code> does <i>not</i> extend the base Dojo
 * <code>_Widget</code> class because it has a slightly different life-cycle
 * than other Dojo objects. This is also done to promote backwards compatibility
 * with existing calls to instantiate the validator.
 * </p>
 * 
 */
dojo.declare('pizza.validation.Validator', null, {

	/**
	 * Allows a validator's validation to be interrupted until reset. This is
	 * a very powerful property and should mostly be used only in cases where
	 * you do not want to permanently disable the on blur handling of a field,
	 * but know under certain precise circumstances that any validation handling
	 * of the onblur event is illegal/unwanted. 
	 */
	interruptValidation: false,

	/**
	 * Listeners that have been wired up to listen for <tt>onblur</tt> events
	 * for validated fields, allowing validation to take place after a user
	 * navigates off of a field.
	 * @var array 
	 */
	listeners: [],

	/**
	 * 
	 */
	sections: {},

	/**
	 * List of validated objects that have been registered to this Validator.
	 * The validated objects are keyed by the validated field's name attribute,
	 * with either a reference to the <tt>ValidatedObject</tt> or a
	 * <tt>redirectTo</tt> property which references another field. The redirect
	 * property is a special case used for grouped and section fields.
	 * 
	 * The following are both valid:
	 *   this.validatedObjects['foo'] = new pizza.validation.ValidatedField(...);
	 *   this.validatedObjects['bar'] = { redirectTo: 'foo' }
	 * @var Object
	 */
	validatedObjects: {},

	/**
	 * A collection of validated field names and the associated validation rules
	 * that should be applied to them. This is consumed by 
	 */
	valFields: {},

	/**
	 * @param Object valFields
	 * @param closure displayErrFunc
	 * @return void
	 * @type undefined
	 */
	constructor: function(validatedFields, displayErrFunc) {
		// ** Acquire instance-specific references to these data structures...
		this.interruptValidation = false;
		this.listeners = [];
		this.sections = {};
		this.validatedObjects = {};
		this.valFields = { "validatedFields": {}, "validatedWidgets": {} };
		// ** Begin instantiation of certain core values (e.g. valFields)...
		if (validatedFields) {
			this.valFields = validatedFields;
		}

		// **
		// If for some reason the validated fields have been over written and
		// no longer exist, we add them back in. The same goes for the validated
		// widgets. 
		if (!this.valFields["validatedFields"]) {
			this.valFields["validatedFields"] = {};
		}

		if (!this.valFields["validatedWidgets"]) {
			this.valFields["validatedWidgets"] = {};
		}
		
		this.postCreate();
	},

	/**
	 * <p>
	 * </p>
	 * @return void
	 * @type undefined
	 */
	// @Override
	postCreate: function() {
		// **
		// If there is a "validatedFieldsDiv" element on the page at the time
		// that the Validator is instantiated, then we want to add its contents
		// to the validated fields for the page, they will then be initialized
		// with the rest of the system.
		this.__parseValidatedFields();
		this.initialize();
	},
	
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * Parses the validated fields <tt>&lt;div&gt;</tt> element if it exists,
	 * the content of the element will be added to the JSON object map that
	 * tracks the fields for this validator.
	 * </p>
	 * 
	 * @return Object either the JSON object reflecting the validated fields in
	 * 			the <tt>&lt;div&gt;</tt> or <tt>null</tt> if the element did not
	 * 			exist.
	 * @private
	 */	
	__parseValidatedFields: function() {
		var validatedFieldsDiv = dojo.byId("validatedFieldsDiv");
		if (!validatedFieldsDiv) {
			console.log("pizza.validation.Validator - No validated fields <div> present. Unable to parse fields from html.");
			return;
		}
		
		console.log("pizza.validation.Validator - Parsing validated fields.");
		var validatedJson = pizza.dom.jsonContent(validatedFieldsDiv);
		if (validatedJson != null) {
			var validatedFields = validatedJson["validatedFields"];
			if (validatedFields) {
				for (var fieldName in validatedFields) {
					this.valFields["validatedFields"][fieldName] = validatedFields[fieldName];
				}
			}
		}
	},

	/**
	 * 
	 */
	initialize: function() {
		// ** Disconnects all previously cataloged "onblur" listeners...
		this.cleanup();

		// ** Initializes the field, and then the form(s)...
		this.initFields();
		this.initForms();
	},

	/**
	 * 
	 */
	initFields: function () {
		var fieldJSON = this.valFields['validatedFields'];
		if (fieldJSON) {
			this.listeners = []; //new Array();
			for (var fieldName in fieldJSON) {
				this.__addFieldValidation({ field: fieldName, validationRule: fieldJSON[fieldName] });
			}
		}
	},

	/**
	 * 
	 */
	initValidatedFields: function() {
		this.__parseValidatedFields();
		this.initialize();
	},

	/**
	 * 
	 */
	initForms : function( elem ) {
		for (var i = 0; i < document.forms.length; i++) {
			this.listeners.push(dojo.connect(document.forms[i], 'onsubmit', dojo.hitch(this, 'validateAllFields', document.forms[i])));
		}
	},

	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * Adds a new field-level validation for the <tt>field</tt> passed as part
	 * of the function call arguments, with a given validation rule. The full
	 * set of arguments that may be passed to this function are covered later,
	 * though many of these arguments are the same that can be passed into the
	 * <tt>addValidation</tt> function which serves as a proxy for this method.
	 * </p>
	 * <p>
	 * All fields are registered to an <tt>onblur</tt> listener. Even if the
	 * property <tt>ignoreBlur</tt> is set to <tt>false</tt>. A field must have
	 * an ID or name attribute--if it has neither no validated object will be
	 * created for the field and it will not be added to set of validated fields
	 * tracked by the validator. If a field's <tt>ID</tt> and <tt>name</tt>
	 * attributes are both present, but they are different (e.g. "fooId" and
	 * "fooName"), two entries will be added to the validated objects map: one
	 * that is keyed to the ID and one to the name. The reference added that is
	 * keyed to the field's ID will be a redirect to the object reference that
	 * is keyed to the field's name.  
	 * </p>
	 * <p>
	 * Simple form elements (e.g. <tt>text</tt>, <tt>input</tt>, and
	 * <tt>select</tt>) are treated as standard <code>ValidatedField</code>
	 * objects. The field targeted for validation will may be either the actual
	 * <tt>DOMNode</tt> itself, or a <tt>string</tt> reflecting either a node's
	 * ID or name. For example: <tt>&lt;input id="foo" name="foo" /&gt;</tt>.
	 * Using the ID or name during the process of assigning field validation
	 * allows us to discover the appropriate element. If we pass in <tt>foo</tt>
	 * here, because it is not only the element's name but also its ID we are
	 * able to quickly and conveniently acquire a node using Dojo's API. Using
	 * the ID of a node is sometimes preferable because we may have elements
	 * that require validation, but are not being submitted as part of a form.
	 * </p>
	 * <p>
	 * If the field for validation passed does not correspond with a single
	 * node then this function will instead attempt to create a different type
	 * of validated object: the <code>ValidatedOptionGroup</code>. If multiple
	 * nodes are returned it is either because multiple elements have the same
	 * ID (while possible, this is actually wrong and should never happen), or
	 * they have the same name. In the latter case, we make the assumption that
	 * if multiple nodes are returned they must be part of an option group. That
	 * is, they are either checkboxes or radio buttons that share the same name
	 * attribute value. 
	 * </p>
	 * <p>
	 * Common arguments that can be passed to this function are:
	 * <ul>
	 * <li><tt>field</tt> - Required. Denotes the field (by ID, name, or node)
	 * 		that the validation will be added for.</li>
	 * <li><tt>validationRule</tt> - the comma-delimited validation rules to
	 * 		apply to the validated field.</li>
	 * <li><tt>autoDisplay</tt> - turns off the automatic error message display
	 * 		for validated fields upon calling their <tt>validate</tt>
	 * 		method.</li>
	 * <li><tt>ignoreBlur</tt> - normally fields are validated when a user agent
	 * 		focus shifts to another element (e.g. "blurs" off the current input
	 * 		element). Setting this property to <tt>true</tt> will make it so the
	 * 		validated object does not invoke the validate function on blur
	 * 		events.</li>
	 * <li><tt>validationDisplay</tt> - the validation display Object to use
	 * 		when displaying validation errors.</li>
	 * </ul>
	 * </p> 
	 * 
	 * @param Object validationArgs the arguments that will be passed to the
	 * 			validated object's constructor.
	 * @return void
	 * @type undefined
	 * @private
	 * @see pizza.validation.Validator#addValidation(...)
	 * @see pizza.validation.ValidatedField
	 */
	__addFieldValidation: function(validationArgs) {
		var theField = validationArgs.field;
		if (!theField) {
			console.log('Unable to add field validation, required argument missing: field');
			console.log(validationArgs);
			return;
		}
		
		var formElements = this._getFormElements(theField);
		if (formElements != null) {
			if (formElements.length > 1) {
				// **
				// We only want to attempt to instantiate an option group if any
				// of the form elements previously returned are an INPUT, and
				// are either a radio or checkbox type. These are the only two
				// valid "option" group input types. 
				var options = new Array();
				for (var i=0; i < formElements.length; i++) {
					var element = formElements[i];
					var tagName = element.tagName.toLowerCase();
					var type = element.type.toLowerCase();
					if (tagName === 'input' && (type === 'radio' || type === 'checkbox')) {
						options.push(element);
					}
				}

				if (options.length > 1) {
					// **
					// Before we instantiate a new validated option group with
					// any of the arguments passed in, we need to make a copy
					// and remove any field and validation rule references
					// (these are superfluous for the encapsulating option group
					// object). 
					var optionsArgs = dojo.mixin({}, validationArgs);
					delete optionsArgs.field;
					delete optionsArgs.validationRule;
		
					var validatedOptions = new pizza.validation.ValidatedOptionGroup(optionsArgs);
					dojo.forEach(options, function(formElem) {
						var args = dojo.mixin({}, validationArgs);
						args['field'] = formElem;
						validatedOptions.add(args);
						this.listeners.push(dojo.connect(formElem, 'onblur', this, 'validate'));
					}, this);
					console.log('pizza.validation.Validator - Adding option group validation for field: ' + theField + ' with validation rule(s): '
							+ (validationArgs.validationRule ? validationArgs.validationRule : '<empty>'));
					this.valFields['validatedFields'][validationArgs.field] = validationArgs.validationRule;  //  For central, global validator, if not wanted remove this
					this.validatedObjects[theField] = validatedOptions;
				}
			}
			else {
				var formElem = formElements[0];
				validationArgs.field = formElem;
				var validatedField = new pizza.validation.ValidatedField(validationArgs);
				var fieldKey = this._resolveKey(formElem);
				console.log('pizza.validation.Validator - Adding field validation for field: ' + fieldKey + ' with validation rule(s): '
						+ (validationArgs.validationRule ? validationArgs.validationRule : '<empty>'));
				this.validatedObjects[fieldKey] = validatedField;
				
				
				// **
				// If for some reason the ID and name attributes of the
				// validated field do not match, we add two entries keyed to
				// both name and ID, with the ID redirecting to entry that has
				// been added by the field's name. 
				if (formElem.id != fieldKey) {
					console.log('pizza.validation.Validator - Field validation for ' + fieldKey + ' has different name/id attributes. The field ID ' + formElem.id + ' will redirect to ' + fieldKey);
					this.validatedObjects[formElem.id] = { redirectTo: fieldKey };
				}
				this.valFields['validatedFields'][formElem.id] = validationArgs.validationRule; //  For central, global validator, if not wanted remove this
				this.listeners.push(dojo.connect(formElem, 'onblur', this, 'validate'));
			}
		}
		else if(dijit.byId(theField)) {
			var theWidget = dijit.byId(theField);
			if (theWidget) {
				this.__addWidgetValidation(validationArgs);
			}
		}
		else {
			if(dojo.byId(theField)){// ValidatedElement
				var validatedField = new pizza.validation.ValidatedElement(validationArgs);
				var fieldKey = validationArgs.field;
				this.validatedObjects[fieldKey] = validatedField;
			}
		}
	},

	/**
	 * <p>
	 * Adds group validation on the <tt>fields</tt> specified with the
	 * appropriate validation rule. This will iterate over each of the fields
	 * specified, creating new validation objects for all of them, and then
	 * it will group them together using the <tt>groupOn</tt> parameter as the
	 * master field.
	 * </p>
	 * 
	 * @param object validationArgs
	 * 
	 * @param array fields
	 * @param string validationRule
	 * @param object extraArgs these get passed on to the actual group validator
	 * 			object; not the parts (e.g. fields) that comprise the
	 * 			group validator itself.
	 * @return void
	 * @type undefined
	 * @private
	 */
	__addGroupValidation: function(validationArgs) { //groupOn, fields, validationRule, extraArgs) {
		var groupOn = validationArgs.masterField;
		if (!groupOn) {
			console.log('Unable to add group validation, required argument missing: masterField');
			return;
		}

		var fields = validationArgs.fields;
		if (!dojo.isArray(fields)) {
			console.log('Unable to add new ValidatedGroup for ' + groupOn + ': fields was not an array.');
			return;
		}

		if (!validationArgs.validationRule) {
			validationArgs.validationRule = "";
		}

		// **
		// Create the "extraArgs" element, this is passed on to the groupOn()
		// function.
		var extraArgs = dojo.mixin({}, validationArgs);
		delete extraArgs.masterField;
		delete extraArgs.fields;

		// **
		// Only actually try to add group validation if the master field (e.g.
		// the field you are grouping on) exists. We assume ID here.		
		if (dojo.byId(groupOn)) {
			for(var i = 0; i < fields.length; i++) {
				if (!this.has(fields[i])) {
					var fieldArgs = { field: fields[i], validationRule: validationArgs.validationRule };
					this.__addFieldValidation(fieldArgs);
				}
			}
			this.groupFields(groupOn, fields, extraArgs);
		}
	},
	
	/**
	 * <p>
	 * </p> 
	 * 
	 * @param Object validationArgs the arguments that will be passed to the
	 * 			validated object's constructor.
	 * @return void
	 * @type undefined
	 * @private
	 * @see pizza.validation.ValidatedField
	 */
	__addWidgetValidation: function(validationArgs) {
		var theField = validationArgs.field;
		if (!theField) {
			console.log('Unable to add widget validation, required argument missing: field');
			return;
		}

		var targetElement = dojo.byId(theField);
		if (targetElement != null) {
			var theWidget = dijit.byId(theField);
			if (theWidget) {
				if (!(theWidget instanceof pizza.validation.ValidatedWidget)) {
					console.log('pizza.validation.Validator - Creating widget validation for field: ' + theField + ' with validation rule(s): '
							+ (validationArgs.validationRule ? validationArgs.validationRule : '<empty>'));
					var validatedWidget = new pizza.validation.ValidatedWidget({ _widget: theWidget });
					this.valFields['validatedWidgets'][theField] = validationArgs.validationRule;
					validatedWidget.set('validationRule', validationArgs.validationRule);
					this.validatedObjects[theField] = validatedWidget;
				}
				else {
					console.log('pizza.validation.Validator - Adding existing ValidatedWidget for field: ' + theField + ' with validation rule(s): '
							+ (validationArgs.validationRule ? validationArgs.validationRule : '<empty>'));
					this.valFields['validatedWidgets'][theField] = validationArgs.validationRule;
					theWidget.set('validationRule', validationArgs.validationRule);
					this.validatedObjects[theField] = theWidget;
				}
			}
		}
	},

	/**
	 * <p>
	 * Returns the form elements given a <tt>field</tt>. The <tt>field</tt> may
	 * be a <tt>string</tt> or <tt>DOMNode</tt>. This function is capable of
	 * returning a list of form elements, or the first element of a list (if
	 * one or more elements are returned).
	 * </p>
	 * <p>
	 * When passing in a <tt>field</tt> as a <tt>string</tt> the function first
	 * attempts to resolve the existance of nodes with <tt>field</tt> as an ID.
	 * If this succeeds, the function makes the assumption that one of two
	 * things are going on: either there is a form element (i.e. <tt>input</tt>,
	 * <tt>textarea</tt>, or <tt>select</tt> elements) that exist which have the
	 * specified field as an ID, or there is a non-form element (e.g.
	 * <tt>&lt;div&gt;</tt>) that exists which have the field as an ID. The
	 * latter case would be typically representative of an attempt to added
	 * validation to a widget.
	 * <p>
	 * Since we are only concerned with <i>form</i> elements in this function,
	 * we further query the elements on the page only looking for those
	 * <tt>input</tt>, <tt>textarea</tt> and <tt>select</tt> elements which have
	 * <tt>field</tt> as an ID. This list is returned, whether it is empty or
	 * has elements in it. If no nodes are found with the ID, we perform a
	 * secondary query against the string as a node's name.
	 * </p>
	 * <p>
	 * In the event that the above resolution of a <tt>string</tt> field as an
	 * ID fails at the <tt>dojo.byId(...)</tt> function call (i.e. returns
	 * <tt>null</tt>), we confirm that the <tt>field</tt> passed is a string
	 * and then attempt to resolve any and all fields based on the name
	 * attribute alone.
	 * </p>
	 * <p>
	 * Finally if the <tt>field</tt> passed is an object, it will precisley the
	 * same steps that we use to resolve the <tt>field</tt> as an ID. That is,
	 * <tt>dojo.byId(...)</tt> will return exactly what we sent in (i.e. the
	 * <tt>DOMNode</tt>).
	 * </p>
	 * 
	 * @param DOMNode|String field the field, either a <tt>string</tt> or a
	 * 			<tt>DOMNode</tt> reflecting the form element(s) we are trying
	 * 			to retrieve.
	 * @param boolean isUnique whether or not a unique result should 
	 * 			returned.
	 * @return array the array of elements that match <tt>field</tt>, or if none
	 * 			match, <tt>null</tt>.
	 * @type array|null
	 */
	_getFormElements: function(field, isUnique) {
		isUnique = (typeof(isUnique) === 'boolean') ? isUnique : false;
		var node = dojo.byId(field);

		// **
		// If the node exists at this point in time, we know that: A) the field
		// was a DOMNode, or B) the field represented the ID of a DOMNode on the
		// page. In the case of (A) we cannot assume that we have either an ID
		// or a name attribute.
		if (node) {
			if ((!node.id || node.id.length < 0) && (!node.name || node.name.length < 0)) {
				console.info('The DOMNode has neither an ID or a name attribute. Offending DOMNode: ');
				console.info(node);
			}

			// **
			// Preliminary check, if the node existed (either due to the field
			// specified being an ID or a DOMNode) and its tag name is one of
			// the "accepted" form elements (e.g. input, select, or textarea)
			// then we can proceed with our attempts at resolving all elements
			// through a Dojo query. If for some reason the query fails
			// (returning 0 elements), we will always return at least the node
			// found earlier because we know it to be an acceptable element.
			//
			// This is done to primarily get around the issue of (very) rare
			// cases where the ID (or name) may be part of an array, for
			// example: myId[0].
			// 
			// Any ID/name with this value will fail a dojo.query(). I'm not
			// entirely sure why but I suspect it is a problem with the basic
			// underlying CSS query engine which does not support the brackets
			// in a valid CSS identifier name. 
			//  
			var tagName = node.tagName.toLowerCase();
			var elements = []; 
			if (tagName === 'input' || tagName === 'select' || tagName === 'textarea') {
				var nodeId = node.id;
				if (nodeId && nodeId.length > 0) {
					var idAttrib = '[id="' + nodeId + '"]';
					var query = dojo.query('input' + idAttrib + ',select' + idAttrib + ',textarea' + idAttrib, document);
					if (query.length > 0) {
						elements = query;
					}
					else {
						elements[0] = node;
					}
					return isUnique ? elements[0] : elements;
				}
				
				var nodeName = node.name;
				if (nodeName && nodeName.length > 0) {
					var nameAttrib = '[name="' + nodeName + '"]';
					var query = dojo.query('input' + nameAttrib + ',select' + nameAttrib + ',textarea' + nameAttrib, document);
					if (query.length > 0) {
						elements = query;
					}
					else {
						elements[0] = node;
					}
					return isUnique ? elements[0] : elements;
				}
			}
		}
		else {
			if (typeof(field) === 'string') {
				var nodeName = field;
				var nameAttrib = '[name="' + nodeName + '"]';
				var query = dojo.query('input' + nameAttrib + ',select' + nameAttrib + ',textarea' + nameAttrib, document);
				if (query.length > 0) {
					return isUnique ? query[0] : query;
				}
			}
		}
		return null;
	},

	/**
	 * <p>
	 * Centralized function to contain the logic for resolving a validated
	 * object's key. This is what is used to reference the <i>main</i> object.
	 * A field is almost always exclusively keyed by its corresponding
	 * <tt>name</tt> attribute. There is only one exception to this and that is
	 * when the field does not have a <tt>name</tt> attribute specified, in this
	 * event its <tt>ID</tt> will be used.</p>
	 * 
	 * @param Node field
	 * @return string
	 * @type string 
	 */
	_resolveKey: function(field) {
		var fieldKey = (field.name && field.name.length > 0) ? field.name : field.id;
		return fieldKey;
	},

	/**
	 * @see pizza.validation.Validator#validateField(field)
	 * @deprecated please use validateField() instead.
	 */
	_validate: function(field) {
		dojo.deprecated('Validator._validate() has been renamed to Validator.validateField(). Please update function references.');
		return this.validateField(field);
	},

	/**
	 * <p>
	 * Adds custom validation to a targeted field. This method expects the
	 * <tt>name</tt> of the validated field to be supplied as the identifying
	 * property to find the proper ValidatedObject to add custom validation to.
	 * In addition it takes a <tt>closure</tt> function which will be associated
	 * with the validated object and called whenever validation is executed.
	 * </p>
	 *
	 * @param array|string|DOMNode target
	 * @param closure func
	 * @return void
	 * @type undefined
	 */
	addCustomValidation: function(target, func) {
		if (dojo.isFunction(func)) {
			if (dojo.isArray(target)) {
				for (var i = 0; i < target.length; i++) {
					this.addCustomValidation(target[i], func);
				}
			}
			else {
				if (!this.has(target)) {
					this.__addFieldValidation({ field: target, validationRule: "" });
				}

				var targetName = typeof(target) === 'string' ? target : this._resolveKey(target);
				if (this.validatedObjects[targetName]) {
					// **
					// If the validated object is a redirect, then we are either
					// adding validation to a group (most likely scenario) or we
					// are adding it to a field that had mismatched ID and name
					// attributes. Regardless, we want to go through the actual
					// validated object's internal "addCustomValidation" method and
					// therefore need to resolve the redirect. 
					if (this.validatedObjects[targetName].redirectTo) {
						targetName = this.validatedObjects[targetName].redirectTo;
					}
					console.log('pizza.validation.Validator - Adding custom validation for: ' + targetName);
					this.validatedObjects[targetName].customValidation = func;
				}
			}
		}
	},
	
	/**
	 * <p>
	 * Legacy function that acts as an alias to the <tt>addValidation</tt>
	 * method.
	 * </p>
	 * 
	 * @param Node|string field the identifier for the field. If a string, this
	 * 			will typically be a field's <tt>id</tt> attribute, and for
	 * 			certain naturally grouped fields (e.g. radio, and checkbox
	 * 			groups) this will be the <tt>name</tt> attribute. Alternatively
	 * 			this may reflect the actual node itself. 
	 * @param string validationRule the name of the validation rule to apply.
	 * @return void
	 * @type undefined
	 * @deprecated
	 * @see pizza.validation.Validator#addFieldValidation(fieldArgs)
	 */
	addManualField: function(fieldName, validationRuleName) {
		dojo.deprecated('DEPRECATED: pizza.validation.Validator.addManualField() - This method has been replaced by addValidation()');
		this.addValidation({ type: "field", field: fieldName, validationRule: validationRuleName });
	},

	/**
	 * 
	 */
	addValidation: function(validationArgs) {
		if (validationArgs.type === null || validationArgs.type === undefined || typeof(validationArgs.type) != "string") {
			validationArgs.type = "field";
		}
		
		switch (validationArgs.type) {
			case "group":
				this.__addGroupValidation(validationArgs);
				break;
				
			case "widget":
				this.__addWidgetValidation(validationArgs);
				break;
				
			case "field":
			default:
				this.__addFieldValidation(validationArgs);
				break;
		}
	},

	/**
	 * <p>
	 * Dynamically adds a new <tt>validationType</tt> to the list of validation
	 * types parsed from the core application. In order to add a new validation
	 * type, some or all, of the following details must be provided:
	 * <ul>
	 * <li><b>name</b> - Required. The name of the validation type. This is used
	 * 		as the validation type's key so that it can be individuated from the
	 * 		other validation types.</li>
	 * <li><b>errorKey</b> - Required. The message resource key that is used
	 * 		when displaying the validation error to the user.</li>
	 * <li><b>regex</b> - Optional. The regular expression that is used to
	 * 		evaluate the validity of validated field or object.</li>
	 * <li><b>min</b> - Optional. The minimum [numeric] value that the validated
	 * 		field or object can have</li>
	 * <li><b>max</b> - Optional. The maximum [numeric] value that the validated
	 * 		field or object can have</li>
	 * <li><b>validateFunction</b> - Optional. A custom validation function
	 * 		which will be run to test the validity of the field or object's
	 * 		value.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Adding a new validation type using the <tt>validateFunction</tt> property
	 * can be used to inject custom validation outside of the scope of the
	 * validated object itself. To work within the scope of the validated object
	 * one would need to explicitly pass a <tt>dojo.hitch()</tt>'d function that
	 * has been scoped to the validation object in question.
	 * </p>
	 * 
	 * @param Object bindArgs the object that contains the parameters used to
	 * 			generate a new [custom] validation type.
	 * @return void
	 * @type undefined
	 */
	addValidationType: function (bindArgs) {
		//name, regex, min, max, errorKey, validateFunction
		pizza.validationTypes[bindArgs.name] = { errorKey : bindArgs.errorKey };
		
		if (bindArgs.regex) {
			pizza.validationTypes[bindArgs.name].regexRule = bindArgs.regex;
		}
		if (bindArgs.min) {
			pizza.validationTypes[bindArgs.name].min = bindArgs.min;
		}
		if (bindArgs.max) {
			pizza.validationTypes[bindArgs.name].max = bindArgs.max;
		}
		if (bindArgs.validateFunction && dojo.isFunction(bindArgs.validateFunction)) {
			pizza.validationTypes[bindArgs.name].validateFunction = bindArgs.validateFunction;
		}
	},

	/**
	 * 
	 */
	attr: function(targetName, propertyName, propertyValue) {
		if (this.has(targetName)) {
			if (propertyValue !== undefined) {
				this.validatedObjects[targetName].propertyName = propertyValue;
			}
			return this.validatedObjects[targetName].propertyName;
		}
	},

	/**
	 * <p>
	 * Permits validation to be cancelled. Especially useful for stopping
	 * potential validation that occurs on the <tt>onblur</tt> events. This is
	 * commonly employed when stopping the validation on an overlay at the time
	 * of its destruction. If the overlay has validated fields that may no
	 * longer be available for validation
	 */
	cancelValidation: function() {
		this.interruptValidation = true;
	},

	/**
	 * <p>
	 * Iterates over the blur event listeners for this validator, removing them
	 * from the internal array and disconnecting them.
	 * </p>
	 * 
	 * @return void
	 * @type undefined
	 */
	cleanup: function() {
		this.validatedObjects = {};
		for (var i = 0; i < this.listeners.length; i++ ) {
			dojo.disconnect(this.listeners.pop());
		}
		this.resumeValidation();
	},

	/**
	 * <p>
	 * </p>
	 *
	 * @param Node|string field the field whose validation message is being
	 * 			cleared. Supports either a <tt>string</tt> or a <tt>Node</tt>
	 * 			(evaluated as an "object") as parameter types.
	 * @param Node|string errorField the field whose validation message is being
	 * 			cleared. Supports either a <tt>string</tt> or a <tt>Node</tt>
	 * 			(evaluated as an "object") as parameter types.
	 * @return void
	 * @type undefined
	 */
	clear: function(field, errorField) {
		var fieldName = (typeof(field) == 'string') ? field : this._resolveKey(field);
		var validationTarget = this.getValidatedObject(fieldName);
		if (validationTarget) {
			validationTarget.clear(errorField);
		}
	},

	/**
	 * <p>
	 * Clears all of the error messages displayed on the page. This does not
	 * necessarily reset the validation on the <tt>ValidatedObject</tt>.
	 * Validation may or may not be reset depending on the object's
	 * implementation.
	 * </p>
	 * 
	 * @param Node node
	 * @return void
	 * @type undefined
	 */
	clearAll: function(node) {
		console.log('pizza.validation.Validator - Clearing all validation messages.');
		
		var root = node ? dojo.byId(node) : document;
		var fieldSections = {};
		dojo.query("input, select, textarea", root).forEach(function(element) {
			this.clear(element);
			var section = this.getSection(element);
			if (section != null) {
				fieldSections[section.getId()] = true;
			}
		}, this);
		
		for(var widgetFieldId in this.valFields["validatedWidgets"]) {
			var widgetQuery = dojo.query('#' + widgetFieldId, root);
			if (widgetQuery.length > 0) {
				this.clear(widgetFieldId);
			}
		}

		this.clearSections(fieldSections);
	},

	/**
	 * <p>
	 * Clears all the targeted sections.
	 * </p>
	 * 
	 * @param Object<string, boolean> targetSections the sections that will have
	 * 			their <tt>clear</tt> method invoked.
	 * @return void
	 * @type undefined
	 */
	clearSections: function(targetSections) {
		for (var sectionKey in targetSections) {
			var theSection = this.sections[sectionKey];
			if (theSection) {
				theSection.clear();
			}
		}		
	},
	
	/**
	 * 
	 */
	createSection: function(sectionName, defaultMessage) {
		this.sections[sectionName] = new pizza.validation.ValidationSection(sectionName, defaultMessage);
	},

	/**
	 * <p>
	 * </p>
	 *
	 * @param Node|string field
	 * @param string errMsg
	 * @param boolean clearVal Optional.
	 * @return void
	 * @type undefined
	 * @deprecated
	 */	
	defaultDisplayErr: function (field, errMsg, clearVal) {
		dojo.deprecated('DEPRECATED: pizza.validation.Validator.defaultDisplayErr() - This method has been deprecated. The Validator does not manage the actual error display any more. ValidatedObjects have validation displays associated with them.');
		var fieldName = (typeof(field) == 'string') ? field : this._resolveKey(field);
		var validationTarget = this.getValidatedObject(fieldName);
		if (validationTarget) {
			if (clearVal === true) {
				validationTarget.clear();
			}
			else {
				validationTarget.display(field, errMsg);
			}
		}
	},

	/**
	 * <p>
	 * Iterates over the <tt>errors</tt>, a JSON object that has an error
	 * message associated with the field that the error occurred on. These
	 * errors should correspond to an element encapsulated by the <tt>form</tt>.
	 * The errors are in the form: <tt>{ fieldName: errorMessage }</tt>.
	 * </p>
	 * 
	 * @param Object errors a JSON object that matches the field name to the
	 * 			error message.
	 * @param Object form the form object that encapsulates the fields with
	 * 			errors.
	 * @return void
	 * @type undefined 
	 */
	displayErrorsFromJson: function(errors, form) {
		for(var e in errors) {
			var validationTarget = this.getValidatedObject(e);

			if(validationTarget) {
				validationTarget.display(form[e], errors[e]);
			}
			else {
				console.info("validationTarget not found -- manually setting error field display");
				pizza.validation.ValidationDisplay.handleBlockError.display(form[e], errors[e]);
			}
		}
	},

	/**
	 * <p>
	 * Based on the<tt>field</tt> passed, this method will return the name of a
	 * validation section. This can then be used to retrieve the validation
	 * section object.
	 * </p>
	 * 
	 * @param Node|field field
	 * @return string 
	 */
	getSection: function(field) {
		var fieldName = (typeof(field) == 'string') ? field : this._resolveKey(field);
		var target = this.getValidatedObject(fieldName);
		if (target != null && target.getSection() != null) {
			var sectionName = target.getSection();
			if (this.sections[sectionName]) {
				return this.sections[sectionName];
			}
		}
		return null;
	},

	/**
	 * <p>
	 * Retrieves the <code>ValidatedObject</code> from the map fields to
	 * validate on. In some cases the validated object may be a reference to
	 * another validated object. Once found, the validated object is returned
	 * to the calling function.
	 * </p>
	 *
	 * @param string fieldName the name of the field.
	 * @return ValidatedObject
	 * @type ValidatedObject 
	 */	
	getValidatedObject: function(fieldName) {
		var target = this.validatedObjects[fieldName];
		if (target) {
			// **
			// Sometimes, grouped fields will be redirected to another validation
			// target, when this is the case we need to get the validated object
			// that they are being redirected to. 
			if (target.redirectTo) {
				target = this.getValidatedObject(target.redirectTo);
			}
			return target;
		}
		return null;
	},
	
	/**
	 * <p>
	 * Groups validated objects together according to the field (or name) passed
	 * in the <tt>groupOn</tt> parameter. This will only group those validated
	 * objects that are already being tracked by this <code>Validator</code>.
	 * When grouping fields, if they have already been defined their individual
	 * validation objects will be "moved" into the grouped field.
	 * </p>
	 * 
	 * @param string groupOn the field or name of the field that the
	 * 			grouping should be performed on. This will be used as the key
	 * 			in the <tt>validatedObjects</tt> JSON to reference the newly
	 * 			grouped fields.
	 * @param array<Node|string> fields the fields (either by node, or by name) that
	 * 			are being grouped together.
	 * @param Object extraArgs the extra arguments that will be applied to the
	 * 			group of fields.
	 * @return void
	 * @type undefined
	 */
	groupFields: function(groupOn, fields, extraArgs) {
		if (typeof(groupOn) != 'string') {
			console.log('Validator.groupFields() expects its first parameter, groupOn, to be a string. ' + typeof(groupOn) + ' given.');
			return;
		}

		if (dojo.byId(groupOn)) {
			var groupArgs = dojo.mixin({ masterField: groupOn }, extraArgs);
			var validatedGroup = new pizza.validation.ValidatedGroup(groupArgs);
			for(var idx=0; idx < fields.length; idx++) {
				var fieldName = (typeof fields[idx] == 'string') ? fields[idx] : this._resolveKey(fields[idx]);
				var validatedTarget = this.getValidatedObject(fieldName);
				if (validatedTarget) {
					validatedGroup.add({ field: validatedTarget });

					// **
					// The field to be redirected is the same as the field we are
					// grouping on, then we want to ignore this--this is the field
					// that will have the validated object associated with it; its
					// safe to skip. If we didn't skip this field we can create a
					// really nasty infinite loop with recursion. 
					if (fieldName != groupOn) {
						this.validatedObjects[fieldName] = { redirectTo: groupOn };
					}
				}
			}
			console.log('pizza.validation.Validator - Adding group validation for: ' + groupOn + ', and fields: ' + fields);
			this.validatedObjects[groupOn] = validatedGroup;
		}
	},

	/**
	 * <p>
	 * Function that checks to see if this <tt>Validator</tt> has the field,
	 * <tt>field</tt> in its list of referenced validators; returning
	 * <tt>true</tt> if the validated field was found, and <tt>false</tt>
	 * otherwise.
	 * </p>
	 * <p>
	 * This function accepts its <tt>field</tt> parameter as either a
	 * <tt>string</tt> or a DOM <tt>Node</tt>. In the event that a DOM
	 * <tt>Node</tt> is passed, this function will call into the
	 * <tt>_resolveKey()</tt> method to get the proper identifier for the field.
	 * </p>
	 * 
	 * @param Node|string field
	 * @return boolean whether or not this <tt>Validator</tt>
	 * @type boolean
	 * @see pizza.validation.Validator#_resolveKey(field)  
	 */
	has: function(field) {
		var fieldName = (typeof(field) == 'string') ? field : this._resolveKey(field);
		if (this.validatedObjects[fieldName]) {
			return true;
		}
		return false;
	},

	/**
	 * 
	 */
	hasBindOrValidationErrors : function(response) {
		var errors = response.bindErrors;
		if (errors != null && errors.length >= 1) {
			for (var e in errors) {
				var validationTarget = this.getValidatedObject(errors[e].fieldName);
				if(validationTarget) {
					validationTarget.display(errors[e].fieldName + ".errors", errors[e].fieldError);
				}
				else {
					console.info("validationTarget not found -- manually setting error field display");
					pizza.validation.ValidationDisplay.handleBlockError.display(errors[e].fieldName + ".errors", errors[e].fieldError);
				}
				
			}
			return true;
		}
		return false;
	},

	/**
	 * 
	 */
	isRedirect: function(target) {
		if (target.redirectTo) {
			return true;
		}
		return false;
	},
	
	/**
	 * <p>
	 * Different from the validate methods, this simply evaluates whether a
	 * field is valid or not, without having to invoke display logic. This can
	 * be performed on a field-by-field basis and may be useful for scripts that
	 * work with the validator but whose logic is not strictly tied to
	 * validation display. For instance, if a script needs to know whether to
	 * enable or disable a certain field it may first need to know whether
	 * another field is valid. This can be used for that.
	 * </p>
	 * 
	 * @param Node|string field
	 * @return boolean
	 * @type boolean
	 */
	isValid: function(field) {
		var fieldName = (typeof(field) == 'string') ? field : this._resolveKey(field);
		var validationTarget = this.getValidatedObject(fieldName);
		if (validationTarget) {
			var validationResponse = validationTarget.valid();
			return validationResponse.valid;
		}
		return true;
	},

	/**
	 * @param string field the name or ID of the field being targetted by this
	 * 			function.
	 * @param object validationArgs the validation arguments that will be used
	 * 			to update the targetted field.
	 * @return void
	 * @type undefined
	 */
	removeValidation: function(fieldName) {
		if (this.has(fieldName) === true) {
			// TODO: Need to handle redirect
			delete this.validatedObjects[fieldName];
		}
	},

	/**
	 * 
	 */
	resumeValidation: function() {
		this.interruptValidation = false;
	},

	/**
	 * <p>
	 * Convenience function that updates the validated objects to use a
	 * specified <tt>displayHandler</tt>.
	 * </p>
	 */
	setDisplayHandler: function(displayHandler, restrictToFields) {
		var args = { validationDisplay: displayHandler };
		if (restrictToFields === null || restrictToFields === undefined) {
			var restrictToFields = [];
			for (var validatedField in this.valFields['validatedFields']) {
				restrictToFields.push(validatedField);
			}
		}
		this.updateValidation(restrictToFields, args);
	},

	/**
	 * <p>
	 * Assigns a <tt>ValidatedObject</tt> by its field name to a section. If
	 * that section does not exist, it will be created.
	 * </p>
	 * 
	 * @param string
	 * @param Node|string|array
	 */
	setSection: function(sectionName, field) {
		if (dojo.isArray(field)) {
			for (var i = 0; i < field.length; i++) {
				this.setSection(sectionName, field[i]);
			}
		}
		else {
			if (!this.sections[sectionName]) {
				this.createSection(sectionName);
			}
	
			var targetName = (typeof(field) == 'string') ? field : this._resolveKey(field);
			if (this.has(targetName)) {
				if (this.validatedObjects[targetName].redirectTo) {
					targetName = this.validatedObjects[targetName].redirectTo;
				}
				this.validatedObjects[targetName].addToSection(sectionName);
			}
		}
	},

	/**
	 * @param array|string|DOMNode field the name or ID of the field being targetted by this
	 * 			function.
	 * @param object validationArgs the validation arguments that will be used
	 * 			to update the targetted field.
	 * @return void
	 * @type undefined
	 */
	updateValidation: function(target, validationArgs) {
		if (dojo.isArray(target)) {
			for (var i = 0; i < target.length; i++) {
				this.updateValidation(target[i], validationArgs);
			}
		}
		else {
			var fieldName = typeof(target) === 'string' ? target : this._resolveKey(target);
			var validationTarget = this.getValidatedObject(fieldName);
			if (validationTarget !== null) {
				console.log('pizza.validation.Validator - Updating validated object: ' + fieldName);
				dojo.mixin(validationTarget, validationArgs);
			}
		}
	},

	/**
	 * <p>
	 * </p>
	 * <p>
	 * Puts the "onblur" handler on a timer. The reason for this is so that if a
	 * user changes a field (say a dropdown box) and then instead of clicking
	 * somewhere on the form first to trigger the onblur event immediately
	 * clicks the submit button of a form, the validation messages from the
	 * validation of all the fields and those from the onblur event will display
	 * at approximately the same time.
	 * </p> 
	 */
	validate: function(event) {
		if (event) {
			var evtTarget = event.target;
			if (event.type == 'blur' && (evtTarget.value && evtTarget.value.length != 0)) {
				this.validateField(event.target, event);
				return;
			}
			var instance = this;
			var useEvent = dojo.isIE ? dojo.clone(event) : event;
			window.setTimeout(function(){instance.validateField(event.target, useEvent);}, 200);
		}
	},
	
	/**
	 * @see pizza.validation.Validator#validateAllFields(node)
	 */
	validateAllDialogFields: function(dialogId, displayErrors) {
		if (displayErrors === null || displayErrors === undefined) {
			displayErrors = true;
		}
		return this.validateAllFields(dialogId, displayErrors);
	},

	/**
	 * <p>
	 * Validates all the fields beneath a given node. In addition to validating
	 * the fields, we keep track of sections that are also beneath the node and
	 * display any section-level validation messages there when we know that
	 * the field associated with that section has failed validation.
	 * </p>
	 * <p>
	 * All errors are cleared beneath the given root <tt>Node</tt> prior to the
	 * evaluation of field validity.
	 * </p>
	 * 
	 * @param Node|string node
	 * @return boolean
	 * @type boolean
	 */
	validateAllFields: function(node, displayErrors) {
		if (displayErrors === null || displayErrors === undefined) {
			displayErrors = true;
		}

		var valid = true;
		var root = node ? dojo.byId(node) : document;

		// ** Clear all errors before
		if (displayErrors) {
			this.clearAll(root);
		}

		var fieldSections = {};
		dojo.query("input, select, textarea", root).forEach(function(element) {
			if (this.validateField(element, null, displayErrors) == false) {
				valid = false;
				
				// **
				// If, and only if, the field being validated results in a
				// validation failure do we want to consider the possibility
				// that it needs a section validation message displayed. 
				var section = this.getSection(element);
				if (section != null) {
					fieldSections[section.getId()] = { valid: false };
				}
			}
		}, this);

		// **
		// Widgets are a different beast all together. More often than not they
		// do not inherently handle their validation through the valid() method,
		// more often custom validation is placed on them.
		for(var widgetFieldId in this.valFields["validatedWidgets"]) {
			var widgetQuery = dojo.query('#' + widgetFieldId, root);
			if (widgetQuery.length > 0) {
				this.validateField(widgetFieldId, null, displayErrors);
			}
		}
		
		// **
		// Before returning from this method, we need to iterate over any of the
		// sections associated with the fields we've just validated. This will
		// allow us to properly display the validation for only those fields
		// that were found beneath the given node. 
		for (var sectionKey in fieldSections) {
			var _section = this.sections[sectionKey];
			if (_section && fieldSections[sectionKey].valid === false) {
				_section.display();
			}
		}
		return valid;		
	},

	/**
	 * <p>
	 * Acquires the validation target by the name or ID on the field. The
	 * validator typically indexes its fields by their <tt>name</tt> attribute,
	 * and in many cases <tt>name</tt> and <tt>id</tt> will be the same. Name is
	 * preferred mostly due to the fact that under most circumstances both
	 * individual and some specially grouped fields (e.g. radio buttons) can be
	 * uniquly identified by the same value that is going to be sent to the
	 * server as the variable name.
	 * </p>
	 * <p>
	 * Developers may pass in either a <tt>string</tt> or a <tt>DOMNode</tt>
	 * into the validate field function, if a string is passed in the validator
	 * will attempt to find the validated object that it is tracking which has
	 * the matching key in its collection of validated objects. If a DOMNode is
	 * instead passed in, then the name will attempt to be resolved by making
	 * a call to the <tt>_resolveKey</tt> function which attempts to resolve the
	 * <tt>name</tt> attribute if it exists or the <tt>ID</tt> attribute failing
	 * to find a name.
	 * </p>
	 * 
	 * @param Node field the field that is being validated.
	 * @param Event event Optional.
	 * @return boolean
	 * @type boolean
	 */
	validateField: function(field, event, displayErrors) {
		if (displayErrors === null || displayErrors === undefined) {
			displayErrors = true;
		}

		var fieldName = (typeof(field) == 'string') ? field : this._resolveKey(field);
		var validationTarget = this.getValidatedObject(fieldName);
		if (validationTarget) {
			// **
			// If an event has been passed to this method, we need to check and
			// see if it is a blur event. In some cases we may ignore onblur
			// events for fields. The option to ignore blur events needs to be
			// set manually on the field(s) that will be ignoring it. While we
			// will still validate the field, an ignored blur event is really
			// treated more like "ignore error display" on this event. If the
			// field is valid, and an error was previously displayed, we still
			// want to clear the error. 
			if (event && event.type == 'blur') {
				if (validationTarget.isIgnoreBlur()) {
					this.validateUsing(field, validationTarget, false);
					return true;
				}
			}
			var valid = this.validateUsing(field, validationTarget, displayErrors);
			return valid;
		}
		return true;
	},
	
	/**
	 * 
	 */
	validateUsing: function(field, validatedObject, displayErrors) {
		// **
		// If validation has been cancelled, you can assume that the field has
		// "successfully" completed. We can always resume validation by calling
		// into the function resumeValidation(). 
		if (this.interruptValidation) {
			console.debug('Validation has been interrupted. Returning as if validation completed successfully.');
			return true;
		}

		if (displayErrors === null || displayErrors === undefined) {
			displayErrors = true;
		}

		var fieldName = (typeof(field) == 'string') ? field : this._resolveKey(field);
		var valid = true;
		if (validatedObject) {
			valid = validatedObject.validate(field, displayErrors);
			console.log('pizza.validation.Validator - Validation result for field: ' + fieldName + ': ' + (valid === true ? 'valid' : 'invalid'));
		}
		return valid;
	}	
});


/**
 * <p>
 * A <code>ValidationSection</code> object is referenced by validated fields
 * and groups allowing them to denote what portion of a page they belong to. A
 * validation section displays an error for the section; it may or may not
 * display errors for validated fields within its section.
 * </p>
 * 
 * @author Sean.Quiinn
 * @since 5.0
 */
dojo.declare("pizza.validation.ValidationSection", null, {
	id: '',
	messages: {},

	constructor: function(sectionId, defaultMessage) {
		this.id = sectionId;
		this.messages = {};
		if (typeof(defaultMessage) == 'string') {
			this.messages = { "default": defaultMessage };
		}
	},
	
	// -------------------------------------------------------------------------
	
	/**
	 * 
	 */
	_buildMessageHtml: function() {
		if (this.size() > 0) { 
			var htmlMessage = '<div class="error sectionError">' + this.messages["default"] + "</div>";
			for (var messageKey in this.messages) {
				if (messageKey.toLowerCase() != "default") {
					htmlMessage += '<div class="error">' + this.messages[messageKey] + "</div>";
				}
			}
			return htmlMessage;
		}
		return null;
	},

	/**
	 * Adds a message to the set of validation section messages.
	 */
	addMessage: function(key, message) {
		this.messages[key] = message;
	},
	
	clear: function() {
		var fieldNode = dojo.byId(this.id);
		if (fieldNode) {
			fieldNode.style.display = 'none';
			//pizza.dom.textContent(errorField, message);
		}		
	},
	
	display: function() {
		var fieldNode = dojo.byId(this.id);
		if (fieldNode) {
			fieldNode.style.display = 'block';
			var message = this._buildMessageHtml();
			if (message != null) {
				console.debug('pizza.validation.ValidationSection#display(): Look into XSS issues');
				fieldNode.innerHTML = message;
			}
		}
		
		var instance = this;
		dojo.publish("pizzaValidationSectionDisplayEvent", [{ id: this.id, validationSection: instance }]);
	},

	/**
	 * <p>
	 * Returns the ID of this particular section validation object.
	 * </p>
	 * 
	 * @return string the ID of this <code>ValidationSection</code>
	 * @type string
	 */
	getId: function() {
		return this.id;
	},

	/**
	 * <p>
	 * Removes a mesage from the <code>ValidationSection</code> by its
	 * <tt>messageKey</tt>.
	 * </p>
	 *
	 * @param string messageKey the message key, identifying a message that is
	 * 			present in this validation section.
	 * @return void
	 * @type undefined
	 */
	remove: function(mesaageKey) {
		delete this.messages[mesaageKey];		
	},

	/**
	 * <p>
	 * Removes all messages.
	 * </p>
	 */
	removeAll: function() {
		this.messages = {};
	},
	
	/**
	 * 
	 */
	size: function() {
		var size = 0;
		for (var i in this.messages) {
			size++;
		}
		return size;
	}
});

// -----------------------------------------------------------------------------
// VALIDATION HANDLERS
// -----------------------------------------------------------------------------

/** Singleton instantiation for ValidationHandler. */
pizza.validation.ValidationHandler = {};

/**
 * 
 */
pizza.validation.ValidationHandler.required = {
	validate: function(validationType, fieldVal) {
	  return fieldVal != null && fieldVal.length > 0;
	}
};

/**
 * 
 */
pizza.validation.ValidationHandler.regex = {
	unmask: function (regexRule) {
//		while (regexRule.indexOf("\\C{") != -1) {
//			var pattern = new RegExp("(.*?)\\\\C{(.*?)}(.*)");
//			var key = regexRule.replace(pattern, '\$2');
//			regexRule = regexRule.replace(pattern, '\$1' + pizza.regexEntities[key] + '\$3');
//		}
		return regexRule;
	},
	validate: function (validationType, fieldVal) {
		if (validationType['regexRule']) {
			var unmaskedRule = this.unmask(validationType['regexRule']) ;
			var re;
			if (unmaskedRule.indexOf("(?i)") != -1) {// Java regex rule contains the case insensitive modifier (?i)
				var noModifierRule = unmaskedRule.replace("(?i)","");// remove the (?i)
				re = new RegExp(noModifierRule,"i");
			}
			else {
				re = new RegExp(unmaskedRule);
			}
			return re.test(fieldVal);
		}
		return true;
	}
};

pizza.validation.ValidationHandler.len = {
  _super: pizza.validation.ValidationHandler.regex,
  
  validate: function(validationType, fieldVal) {
	if (validationType["minLength"] != null) {
	  var minLen = parseInt(validationType["minLength"]);
			// Fail or pass fast - If the value is empty or null and minLength is 0, pass automatically, otherwise fail.
	  if (fieldVal == null || fieldVal.length == 0) {
		return minLen == 0;
	  }
	  if (fieldVal.length < minLen) {
		return false;
	  }
	}
	if (validationType["maxLength"] && fieldVal != null) {
	  if (fieldVal.length > parseInt(validationType["maxLength"])) {
		return false;
	  }
	}
	return this._super.validate(validationType, fieldVal);
  }
};


pizza.validation.ValidationHandler.collectionSize = {
		  
  validate: function(validationType, fieldVal) {
	if (validationType["minSize"] != null) {
	  var minSize = parseInt(validationType["minSize"]);
			// Fail or pass fast - If the value is empty or null and minLength is 0, pass automatically, otherwise fail.
	  if (fieldVal == null || fieldVal.length == 0) {
		return minSize == 0;
	  }
	  if (fieldVal.length < minSize) {
		return false;
	  }
	}
	if (validationType["maxSize"] && fieldVal != null) {
	  if (fieldVal.length > parseInt(validationType["maxSize"])) {
		return false;
	  }
	}
	return true;
  }
};


pizza.validation.ValidationHandler.simple = {
	_super: pizza.validation.ValidationHandler.regex,
	
	validate: function(validationType, fieldVal) {
		if (!this._super.validate(validationType, fieldVal)) {
			return false;
		}
			
		if (validationType['maxVal']) {
			if (parseFloat(fieldVal) > parseFloat(validationType['maxVal'])) {
				return false;
			}
		}
		
		if ( validationType['minVal'] ) {
			if (parseFloat(fieldVal) < parseFloat(validationType['minVal'])) {
				return false;
			}
		}
		return true;
	}
};

pizza.validation.ValidationHandler.configenabled = {
	_super: pizza.validation.ValidationHandler.len,

	validate : function (validationType, fieldVal) {
		var sysProp = validationType['sysProp'];
		if (pizza.context[sysProp] !== undefined && pizza.context[sysProp]) {
			return this._super.validate(validationType, fieldVal);
		}
		return true;
	}
};

pizza.validation.ValidationHandler.ccnum = {
	LUHN_LOOKUP: [
		[0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
		[0, 2, 4, 6, 8, 1, 3, 5, 7, 9]
	],
	LUHN_RADIX: 10,

	validate: function (validationType, fieldVal) {
		if (fieldVal == null || fieldVal.length == 0) {
			return true;
		}
		var fieldValue = fieldVal.replace(/\s+/g, '');
		var regex = new RegExp("^((4\\d{15})|(4\\d{12})|(5[1-5]\\d{14})|(3[47]\\d{13})|(6011\\d{12})|(65\\d{14}))$");
		if (!regex.test(fieldValue)) {
			return false;
		}
		return pizza.validation.ValidationHandler.ccnum.calculateLuhnChecksum(fieldValue) == 0;
	},

	calculateLuhnChecksum: function(value) {
		var checksum = 0;
		for (var n = 0; n < value.length; n++) {
			var ch = value.charCodeAt(value.length - n - 1);
			var digit = ch - "0".charCodeAt(0);
			if (digit < 0 || digit >= pizza.validation.ValidationHandler.ccnum.LUHN_RADIX) {
				// Non-digit
				return -1;
			}
			checksum += pizza.validation.ValidationHandler.ccnum.LUHN_LOOKUP[n % pizza.validation.ValidationHandler.ccnum.LUHN_LOOKUP.length][digit];
		}
		return checksum % pizza.validation.ValidationHandler.ccnum.LUHN_RADIX;
	}
};