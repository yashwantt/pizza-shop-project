

dojo.provide('pizza.widget.Button');
dojo.require('pizza.widget._ExtractAttributes');
dojo.require('dijit.form.Button');

/*
 * For information on button widget features, please visit the development wiki
 * http://bos-fs-01:8080/display/CTArch/Button+widget
 */
dojo.declare(
'pizza.widget.Button',
[dijit.form.Button, pizza.widget._ExtractAttributes],
{
	templateString: dojo.cache("pizza", "templates/Button.html"),
	size: "medium",
	color: "blue",
	secondaryButtonClass: "",
	disabled: false,
	disableOnClick: false,
	addPaneOnClick: false,
	disconnects: [],
	doClickOnEnter: [],
	form: null,           /* Form - can be a form object, name of a form, form widget, or id of a form widget. 
	                       *        If specified the onClick behavior will be to submit this form. */
	requiredFields: null, /* Array - specify an array of input nodes or node id's, that must have a value,
						   *         in order for this button to be enabled.	*/
	scrollOnFocus: false, /* don't try scrolling container node to display button when focused on */
	
	/* Attribs that can be passed in via markup - see _ExtractAttributes mixin */
	markupAttribs: [ 'color', 'label', 'labelKey', 'disableOnClick', { attribName: 'buttonSize', objectName: 'size' } ],

	attributeMap: dojo.mixin(dojo.clone(dijit.form.Button.prototype.attributeMap), { color: this.color }),

	postMixInProperties :function() {
		this.inherited(arguments);
		if (this.size != 'large' && this.size != 'medium' && this.size != 'small') {
			throw 'Invalid button size. Valid buttons sizes are: large, medium, small';			
		}
		this.baseClass = this.size + "CtBtn";
		if(this.labelKey) {
			this.label = pizza.getMsg(this.labelKey);
		}
	},

	/*
	 * Overwriting Dijit method that was replacing the label with the innerHTML of the source node.
	 * This was causing the text to appear before the button is rendered on page load.  Instead, we use
	 * a 'label' attribute on the source node to fetch the label for the button.
	 */
	_fillContent: function() {},

	_setColorAttr: function(color) {
		dojo.removeClass(this.domNode, this.color + "CtBtn");
		this.color = color;
		dojo.addClass(this.domNode, this.color + "CtBtn");
	},
	
	setSpacyLabel : function(text, spaces) {
		var spacer = "";
		if (!spaces) {
			spaces = 2;
		}
		for (i = 0; i < spaces; i++) {
			spacer += "&nbsp;";
		}
		this.set('label', spacer + text + spacer);
	},
	
	postCreate : function() {
		this.inherited(arguments);

		if (this.secondaryButtonClass !== "") {
			dojo.addClass(this.domNode, this.secondaryButtonClass);
		}

		if (this.margin) {
			dojo.deprecated("TODO: Remove dojo.style that takes 'margin' as an arg for: "+this.id);
			dojo.deprecated("margin is used to make " + this.id + ", use secondaryButtonClass instead.");
			dojo.style(this.domNode, "margin", this.margin);
		}

		if (this.doClickOnEnter.length > 0) { // attach nodes for simulating button click on enter
			this._connectInputs();
		}

		// In IE, hide the focus border and use our own focus style
		if (dojo.isIE) {
			this.focusNode.hideFocus = true;
		}

		if(this.requiredFields) {
			if(!dojo.isArray(this.requiredFields)) this.requiredFields = [this.requiredFields];
			// ensure we keep a ref to the actual nodes
			this.requiredFields = dojo.map(this.requiredFields, function(n){ return dojo.byId(n); });
			for(var i=0; i<this.requiredFields.length; i++) {
				// TODO i only test this with <select> inputs
				dojo.connect(this.requiredFields[i], 'onchange', dojo.hitch(this, this._checkRequiredFields));
			}
			this._checkRequiredFields();		
		}
	},
	_checkRequiredFields: function() {
		if(dojo.every(this.requiredFields, pizza.hasValue)){
			this.set('disabled', false);
		} else {
			this.set('disabled', true);
		}
	},
	
	_setDisabledAttr: function(/*Boolean*/ value){
		this.inherited(arguments);
		this._focused = false;
		this._setStateClass();
	},

	_onClick: function(/*Event*/ e){
		if (this.disabled) { return true; } /* return false squelches the original event, which we don't want */
		// Button must pass validation before triggering onClick function
		if (this.validation) {
			if (!this.validation(e)) {
				return true; /* return false squelches the original event, which we don't want */
			}
		}
		this._clicked(); // widget click actions
		if(this.form) {
			if(dojo.isString(this.form)) {
				// try for a widget first
				var id = this.form;
				this.form = dijit.byId(id);
				// else grab the form object with that name
				if(!this.form) this.form = dojo.doc.forms[id];
			}
			this.form.submit(e);
		}
		return this.onClick(e); // user click actions
	},

	_clicked : function(/*Event*/ e){
		if (this.addPaneOnClick) {
			this._disableAllButtons();
			this.clearPane = new pizza.widget.ClearPane({ handles: this.disconnects });
		}
		else if (this.disableOnClick) {
			this.set('disabled', true);
		}
	},
	
	_disableAllButtons : function() {
		dijit.registry.byClass(this.declaredClass).forEach(
			function(button) {
				button.set('disabled', true);
			}
		);
	},

	_connectInputs : function() {
		dojo.forEach(this.doClickOnEnter, function(input) {
			if (input) {
				this.connectEnterToOnClick(input);
			}
		}, this);
	},

	connectEnterToOnClick : function(input) {
		this.disconnects.push(dojo.connect(input, 'onkeypress', this, "_onEnter"));
	},

	_onEnter : function(evt) {
		if (evt.keyCode == dojo.keys.ENTER) {
			/*
			 * In case the event occurs in a form with a submit input, we don't want the default browser behavior of submitting the form.
			 */
			if (evt.preventDefault) { 
				evt.preventDefault();
			}
			this._onButtonClick(evt);
		}
	},
	

	/**
	 * Simulates a mouseover visually.  This only affects the CSS styles used
	 * and does not fire any mouse events.
	 * @param hoverState true to simulate a mouseover state, false for
	 *   mouseout 
	 */
	fakeHover : function(hoverState) {
		var hoverClass = this.baseClass + "Hover";
		if (hoverState) {
			dojo.addClass(this.domNode, hoverClass);
		} else {
			dojo.removeClass(this.domNode, hoverClass);
		}
	}
});

dojo.declare( "pizza.widget.DropDownButton", dijit.form.DropDownButton, {

    baseClass : "dijitDropDownButton",
	templateString: dojo.cache("pizza" , "templates/DropDownButton.html"),
	
	_setDisabledAttr: function(/*Boolean*/ value){
		this.inherited(arguments);
		// hack so it looks right in IE - cleartype gets turned off when adding attribute 'disabled'
		if (dojo.isIE) {
			this.focusNode.removeAttribute('disabled');
		}
	}

});

dojo.declare( "pizza.widget.MenuDropDownButton", dijit.form.DropDownButton, {
    
    templateString:"<div id=\"${id}_focus\"class=\"dijit dijitLeft dijitInline\"\n\tdojoAttachEvent=\"onmouseenter:_onMouseEnter,onmouseleave:_onMouseLeave,onkeypress:_onKey\"\n\t><div class='dijitRight'>\n\t<button class=\"dijitStretch dijitButtonNode dijitButtonContents\" type=\"${type}\"\n\t\tdojoAttachPoint=\"focusNode,titleNode\" waiRole=\"button\" waiState=\"haspopup-true,labelledby-${id}_label\"\n\t\t><div class=\"dijitInline ${iconClass}\" dojoAttachPoint=\"iconNode\"></div\n\t\t><span class=\"dijitButtonText\" \tdojoAttachPoint=\"containerNode,popupStateNode\"\n\t\tid=\"${id}_label\">${label}</span\n\t\t><span class='dijitA11yDownArrow'>&#9660;</span>\n\t</button>\n</div></div>\n",
    
    menuHovered: false,
    dropDownDelay: 50,
    
    postCreate : function() {
        // this calls the baseclass postCreate
        this.inherited(arguments);
        dojo.subscribe("RoundedMenuBoxHover", this, "handleRoundedMenu");
    },
    handleRoundedMenu: function(topic) {
        this.menuHovered = topic.isMenuHovered(); 
        setTimeout(dojo.hitch(this, "handleDropDown"), this.dropDownDelay);
    }, 
    isMenuHovered : function() {
        return this.menuHovered;
    }, 
    _onBlur: function() {
		// summary: called magically when focus has shifted away from this widget and it's dropdown
		this.removeHoverClass();
		this.inherited(arguments);
	},
	_onMouseEnter : function(e) { 
		if (this.disabled) { 
			return; 
		}
		this.menuHovered = true;
		if (!this._opened) {
			this.addHoverClass();
			this.toggleDropDown(this);
		}
	},
	_onMouseLeave : function(e) { 
	    this.menuHovered = false;
	    // wait until the dropdown is displayed before checking its hover state
		setTimeout(dojo.hitch(this, "handleDropDown"), this.dropDownDelay);
	},
	handleDropDown : function() {
	    if(!this.isMenuHovered()) {
		   this._onBlur();
		}   
	},
	
	addHoverClass : function() {
		if (this.selected) {
			dojo.removeClass(this.domNode.parentNode.parentNode, "menuSelected");
		}
		dojo.addClass(this.domNode, "navbarHasDropDownOpen");
	},
	removeHoverClass : function() {
		if (this.selected) {
			dojo.addClass(this.domNode.parentNode.parentNode, "menuSelected");
		}
		dojo.removeClass(this.domNode, "navbarHasDropDownOpen");
	},
	setFirstChildSelected: function() {
		this._onBlur();
		var firstChild = this.dropDown.getChildren()[0]._getFirstFocusableChild();
		if (firstChild) {
		    firstChild.setMenuSelected();
		}
	}
});

dojo.declare(
	"pizza.widget.ClearPane",
	dijit._Widget,
	{
		postCreate: function() {
			this._disconnectHandlers();
			
			var b = dojo.body();
			var clearObj = dojo.byId("clearPaneUnderlay");
			if (clearObj) return;

			this.bg = dojo.create("div", {
				'class': "clearPaneUnderlay",
				id: "clearPaneUnderlay"
			}, b);
			dojo.style(this.bg, {
				'position': "absolute",
				'background-color': "transparent",
				'opacity': 0,
				'z-index': "1000",
				'display': "none"
			})
			
			this.iframe = dojo.create("iframe", {
				src: "about:blank",
				frameborder: "0",
				scrolling: "no"
			}, this.bg);
			dojo.style(this.iframe, "z-index", "999");
			
			this.layout();
			
			dojo.style(this.bg, "display", "block");
			
			var doc = this.iframe.contentDocument;
			if (!doc) {
				doc = this.iframe.contentWindow.document;
			}
			if (doc) {
				doc.open();
				doc.write("<html><body style=\"width: 100%; height: 100%; cursor: progress;\"></body></html>");
				doc.close();
			}
			
			this._resizeHandler = this.connect(window, "onresize", "layout");
			this.connect(window, "onscroll", this.layout);
		},
		
		/*
		 * A 100% height setting will only fill up the window and not cover the whole page
		 * Layout function covers whole page and will readjust on window scroll and resize
		 */
		layout: function() {
			var viewport = dijit.getViewport();
			var bg = this.bg.style;
			var ifrm = this.iframe.style;
			bg.top = viewport.t + "px";
			bg.left = viewport.l + "px";
			ifrm.width = viewport.w + "px";
			ifrm.height = viewport.h - 5 + "px";

			var viewport2 = dijit.getViewport();
			if(viewport.w != viewport2.w){ ifrm.width = viewport2.w + "px"; }
			if(viewport.h != viewport2.h){ ifrm.height = viewport2.h + "px"; }
		},
		
		_disconnectHandlers: function() {
			if (this.handles) {
				dojo.forEach(this.handles, function(handle){
					dojo.disconnect(handle);
				});
			}
		}
	}
);
