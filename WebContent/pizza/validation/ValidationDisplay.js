dojo.provide('pizza.validation.ValidationDisplay');

/** Singleton instantiation for ValidationDisplay. */
pizza.validation.ValidationDisplay = {};

/**
 * 
 */
pizza.validation.ValidationDisplay.handleBlockError = {

	/**
	 * <p>
	 * The display function for handling block errors. This function displays
	 * the error message passed, the <tt>message</tt> for a given
	 * <tt>field</tt>. The field passed may be either a <tt>string</tt> or an
	 * <tt>object</tt>. If the field is an object (e.g. <tt>DOMNode</tt>) then
	 * the name of the eror field will attempt to resolve itself to the field's
	 * name first and foremost if it is defined, otherwise it will use the
	 * field's ID and appending <tt>.errors</tt> to the end of it. If a string
	 * is passed, the string will be used for the error message field ID.
	 * </p> 
	 * 
	 * @param object|string field the field receiving the validation error
	 * 			message. If this field is a <tt>DOMNode</tt> the target field
	 * 			for the error message will be either the <tt>name</tt> or
	 * 			<tt>ID</tt> of the node, appended with <tt>.errors</tt>.
	 * @param string message the error message to display.
	 * @return void
	 * @type undefined
	 */
	display: function(field, message) {
		var errorFieldName = field;
		var isNode = (typeof(field) !== 'string');
		if (isNode) {
			errorFieldName = (field.name && field.name.length > 0) ? field.name : field.id;
			errorFieldName += ".errors";
		}
	
		var errorField = dojo.byId(errorFieldName);
		if (errorField) {
			errorField.style.display = "block";
			//pizza.dom.textContent(errorField, message);
			errorField.innerHTML = message;
		}
		else {
			if (isNode) {
				dojo.create("div", { id: errorFieldName, 'class': "error", innerHTML: message },
						field, "after");
			}
			else {
				console.info("pizza.validation.ValidationDisplay - The field passed for the display of errors "
					+ "was not a DOMNode and was not explicitly defined in the markup. Unable to create a field to "
					+ "be placed after non-existant field: " + field);
				console.info("Please either pass a DOMNode, or add the appropriate error field to your HTML markup.");
			}
		}
	},

	/**
	 * 
	 */	
	clear: function(field) {
		var errorFieldName = field;
		if (typeof(field) != 'string') {
			errorFieldName = (field.name && field.name.length > 0) ? field.name : field.id;
			errorFieldName += ".errors";
		}

		var errorField = dojo.byId(errorFieldName);
		if (errorField) {
			errorField.style.display = 'none';
		}
	}
};

/**
 * 
 */
pizza.validation.ValidationDisplay.handleBlockErrorById = {

	/**
	 * <p>
	 * </p> 
	 * 
	 * @param object|string field
	 * @return void
	 * @type undefined
	 */
	display: function(field, message) {
		var errorFieldName = field;
		var isNode = (typeof(field) !== 'string');
		if (isNode) {
			errorFieldName = field.id + ".errors";
		}
		
		var errorField = dojo.byId(errorFieldName);
		if (errorField) {
			errorField.style.display = "block";
			//pizza.dom.textContent(errorField, message);
			errorField.innerHTML = message;
		}
		else {
			if (isNode) {
				dojo.create("div", { id: errorFieldName, 'class': "error", innerHTML: message },
						field, "after");
			}
			else {
				console.info("pizza.validation.ValidationDisplay - The error field passed display an error on "
					+ "was not a DOMNode and had no error field explicitly defined in the markup. Unable to create "
					+ "new error field for: " + field);
				console.info("Please either pass a DOMNode, or add the appropriate error field to your HTML markup.");
			}
		}
	},

	/**
	 * 
	 */	
	clear: function(field) {
		var errorFieldName = field;
		if (typeof(field) != 'string') {
			errorFieldName = field.id + ".errors";
		}

		var errorField = dojo.byId(errorFieldName);
		if (errorField) {
			errorField.style.display = 'none';
		}
	}
};

/**
 * 
 */
pizza.validation.ValidationDisplay.handleSectionError = {

	/**
	 * 
	 */
	_super: pizza.validation.ValidationDisplay.handleBlockError,

	/**
	 * 
	 */
	sectionErrorField: null,

	constructor: function() {
		this.sectionErrorField = null;
	},

	/**
	 * 
	 */
	display: function(field, message) {
		var errorFieldName = field.name+ ".errors";
		var errorField = dojo.byId(errorFieldName);
		if (errorField) {
			errorField.style.display = "block";
			pizza.dom.textContent(errorField, message);
		}
		else {
			errorField = document.createElement("div");
			with (errorField) {
				id = errorFieldName;
				className = "error";
			};
			dojo.place(errorField, field, "after");
			pizza.dom.textContent(errorField, message);
		}
	},

	/**
	 * 
	 */	
	clear: function(field) {
		var errorFieldName = field.name+ ".errors";
		var errorField = dojo.byId(errorFieldName);
		if (errorField) {
			errorField.style.display = 'none';
		}
	}
};