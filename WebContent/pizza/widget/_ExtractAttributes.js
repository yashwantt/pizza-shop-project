
dojo.provide('pizza.widget._ExtractAttributes');


/*
 * Mixin for widgets that want to extract attributes from the markup
 * and mix them in as widget properties.
 */
dojo.declare(
	'pizza.widget._ExtractAttributes',
	null,
	{
		/* Specify the node that contains the attributes that should be extracted and mixed-in
		 * to this widget's properties.
		 * If not specified srcNodeRef is used. */
		attributeNode: null,  
		
		// combine my attribs with the default attributes?
	    useDefaultAttributes: true,
	    
	    // default attribs that can be specified in markup
	    _defaultAttribs: [ { attribName: 'class', objectName: 'htmlClass' },
	                       'htmlClass' ],
	                       
		/* To be overwritten by widgets using this mixin.
		 * Specify which attributes to look for in the markup.
		 * Attributes can be specified as String values, or an object which maps an attribute name to an object name.
		 * e.g.
		 *   [ 'color', { attribName: 'donkey', objectName: 'kong' }] 
		 * and this markup: 
		 *   <div color='blue' donkey='stupid' ...
		 * would result in the object:
		 *   { color: 'blue', kong: 'stupid' }
		 * being mixed into the widget's props.
		 *  */
		markupAttribs: [], 
		
		/*
		 * During post mix in we extract whatever we can from the markup.
		 * Use srcNodeRef as the location unless a attributeNode is specified.
		 */
		postMixInProperties: function() {
			var n = this.attributeNode||this.srcNodeRef;
			if(n)
				this._extract(n, this);
		},
		
		/*
		 * Extract the attributes from the given source node, and apply them to the given context object.
		 */
		_extract: function(/*DOMNode*/srcNode,/*Object*/context) {
			var a = [].concat(this.markupAttribs);
			if(this.useDefaultAttributes) {
				a = a.concat(this._defaultAttribs);
			}
			var p = this._parseFromNode(srcNode, a);
			dojo.mixin(context, p);
		},
    
    	/*
    	 * Parse the attribues names in the attributes array from the given node.
    	 * @return An object containing the values parsed from the node.  If a requested
    	 *   attribute was not found it will not be included in the returned object. 
    	 */
	    _parseFromNode: function(/*DOMNode*/node,/*Array*/ attributes) {
	    	//console.log('Parsing from node - attributes: ' + dojo.toJson(attributes));
			var props = {};
	        dojo.forEach(attributes, function(a) {
	        	var aName = dojo.isString(a) ? a : a.attribName;
	        	var oName = dojo.isString(a) ? a : a.objectName;
	       	    var val = aName == 'class' ? node.className : node.getAttribute(aName);
	            if(val) {
	                if('true' == val.toLowerCase()) {
	                    val = true;
	                }
	                else if('false' == val.toLowerCase()) {
	                    val = false;
	                }
	                else if(aName == 'width' && !isNaN(new Number(val))) {
                        val+='px'; //the browser drops the px on this attribute for some reason,
                        		   //  it will leave a % though. this is not a very good solution
                    }                  
	                props[oName] = val;
	            }
	        });
	        //console.log('Results: ' + dojo.toJson(props));
			return props;
	    }
    }
);
