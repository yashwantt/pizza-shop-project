dojo.provide("pizza.dialogManager");
dojo.require("pizza.widget.dialog.Dialog");
dojo.require("pizza.widget.dialog.ResizableDialog");
dojo.require("pizza.widget.dialog.ContextHelpDialog");
dojo.require("pizza.widget.dialog.TwoBtnDialog");
dojo.require("pizza.widget.dialog.PrintDialog");
dojo.require("pizza.widget.dialog.ErrorDialog");

pizza.dialogManager = {

	DEFAULT_YES_BUTTON_LABEL : "misc.yes",
	DEFAULT_NO_BUTTON_LABEL : "misc.no",
	DEFAULT_WIDGET_ID : "defaultDialogPopup",

	/**
	 * Creates and displays a modal dialog box.  Arguments are supplied as an args object with
	 * the following properties:
	 * 
	 * @param id identifier used as the id of the DOM element and the widgetId
	 * @param title string displayed in the title bar of the dialog box
	 * @param nonModal whether the dialog should be non-modal (defaults to false)
	 * @param submitAction function that will be called when the submit button is clicked
	 * @param hideAction function that will be called when <code>closeDialog</code> is called on the dialog
	 * @param width pixel width of the dialog
	 * @param height pixel height of the dialog
	 * @param closable whether the dialog should have an 'x' close button (defaults to true)
	 * @param destroyOnClose whether the dialog should be destroyed when <code>hide</code> is called (defaults to false)
	 * @param executeScripts whether the content of this dialog should be searched for script tags that need to be evaluated (defaults to false)
	 * @param noBorder whether the border on top of the button region should be omitted (defaults to false)
	 * @param validateAllFieldsOnSubmit whether to validate all fields on submit (defaults to true)
	 * @param useDefaultValidation whether to use default validation (defaults to true)
	 * @param iconSrc url of the icon to be displayed in the left side of the title bar of the dialog box
	 * @param href url used to populate the content area of the dialog box
	 * @param itemId identifier used to indicate the business object to which the dialog box pertains
	 * @param initCallback function that will be called after the first time dialog content is loaded.
	 *     Use this to execute any code that depends upon objects within the contents of the dialog.  This
	 *     function will be called every time the dialog is shown, but will wait until after the content is
	 *     load. If the href of the dialog hasn't changed, this will be called as soon as show is invoked.
	 * @param hideCallback function that will be called whenever the modal dialog is hidden.
	 *
	 * @return a reference to the dialog box widget
	 */
	showDialog : function(widgetArgs) {
		return(this._showAndReturnDialog(
			this._createDialog(widgetArgs)));
	},

	/**
	 * @return a reference to the dialog box widget
	 */
	showResizableDialog : function(widgetArgs) {
		return(this._showAndReturnDialog(
			this._createResizableDialog(widgetArgs)));
	},
	
	/**
	 * Creates and displays a modal dialog box.
	 * Same as calling showDialog with the 'nonModal' param omitted or set to false.
	 */
	showModalDialog : function(widgetArgs) {
		return this.showDialog(widgetArgs);
	},

	/**
	 * Creates and displays a non-modal dialog box.
	 * Same as calling showDialog with the 'nonModal' param set to true.
	 */
	showNonModalDialog : function(widgetArgs) {
		widgetArgs = dojo.mixin({ nonModal: true }, widgetArgs);
		return this.showDialog(widgetArgs);
	},

	/**
	 * Creates and displays a non-modal dialog box.
	 * Same as calling showDialog with the 'nonModal' param set to true.
	 */
	showNonModalResizableDialog : function(widgetArgs) {
		widgetArgs = dojo.mixin({ nonModal: true }, widgetArgs);
		return this.showResizableDialog(widgetArgs);
	},
	
	/**
	 * Creates and shows a two-button dialog with text-only content.  Takes a string
	 * (as widgetArg property "message") or a message key (as widgetArg property "messageKey").
	 * The objective of using this widget is to avoid hitting the server to populate the dialog.
	 * The widget arguments should not contain an "href" property.
	 */
	showTwoButtonDialog : function(widgetArgs) {
		return(pizza.dialogManager._showAndReturnDialog(
			pizza.dialogManager._create2ButtonDialog(widgetArgs)));
	},

	/**
	 * Creates and shows a one-button dialog with text-only content.  Takes a string
	 * (as widgetArg property "message") or a message key (as widgetArg property "messageKey").
	 * The objective of using this widget is to avoid hitting the server to populate the dialog.
	 * The widget arguments should not contain an "href" property.
	 */
	showOneButtonDialog : function(widgetArgs) {
		return(pizza.dialogManager._showAndReturnDialog(
			pizza.dialogManager._createOneButtonDialog(widgetArgs)));
	},

	/**
	 * Creates a dialog that can be shown by calling <code>show</code> on it.  Kept for legacy reasons -
	 * It's better to use <code>showDialog</code> unless you are modifing the dialog after its created but
	 * before its shown, which is rare.
	 */
	 createModalDialog : function(widgetArgs) {
	 	return this._createDialog(widgetArgs);
	 },

	/**
	 * Shows a dialog that been already been created. This is useful in managing multiple modal dialogs on a single page.
	 * Can be used with <code>createModalDialog</code> to create a dialog, modify it before it's shown, and then show it.
	 */
	show : function (dlg) {
	    return(pizza.dialogManager._showAndReturnDialog(dlg));
	},

	/**
	 * Looks up a dialog widget by it's id and, if found, hides it.
	 */
	hideDialog : function(dialogId) {
		var popup = dijit.byId(dialogId);
		if (popup) {
			popup.hide();
		}
	},

	/**
	 * @deprecated - please set the <code>destroyOnClose</code> parameter in the args to your dialog creation method
	 * to true and call <code>closeDialog</code> on the dialog to close it
	 */
	destroyDialog : function(dialogId) {
		dojo.deprecated("pizza.dialogManager.destroyDialog(widgetId) is deprecated.  Please set"
			+ " destroyOnClose to true on dialog creation and close the dialog using dialog.closeDialog()");
		var popup = dijit.byId(dialogId);
		if (popup) {
			popup.destroy();
		}
	},

	/**
	 * Show the provider details dialog.
	 * 
	 * @param providerId the provider id
	 */
	showProviderDetailsDialog : function(providerId) {
		var args = { userType: pizza.session.userType };
		if (providerId) { // provider ID can be passed
			args[pizza.urlParms.providerId] = providerId;
		}

		this._createWidgetPopup({
			id: "providerDetailsPopUp",
			widgetClass: "ProviderDetailsDialog",
			args: args
		});
	},

	/**
	 * Show the consumer rating popup.
	 */
	showConsumerRatingPopup : function() {
		var href = pizza.config.CONTEXT_PATH + "app/popups/consumerRatingPopup.htm";
		if (pizza.session.userType == "anon") {
			href = pizza.config.CONTEXT_PATH + "guest/popups/consumerRatingPopup.htm";
		}
		var args = {
			id : "consumerRatingPopUp",
			width : 600,
			title : pizza.getMsg("account.provider.consumerRating.popup.title"),
			iconSrc : pizza.getMediaHref("images/icons/ICON_24x24_Information.png"),
			href : href,
			destroyOnClose: true
		};
			
		return pizza.dialogManager.showModalDialog(args);
	},		

	/**
	 * Show the health summary dialog.
	 *
	 * @param destroyOnClose destroyOnClose
	 * @param nonModal whether the dialog should be be nonModal
	 * @param memberId the member id
	 * @param engagementId the engagement id
	 * @param providerId the provider id
	 */
	showHealthSummary : function(destroyOnClose, nonModal, memberId, engagementId, providerId) {
		var args = {};
		if (memberId) { // member ID can be passed
			args[pizza.urlParms.memberId] = memberId;
		}
		if (engagementId) {
			args[pizza.urlParms.engagementId] = engagementId;
		}
		if (providerId) { // needed for assistants
			args[pizza.urlParms.providerId] = providerId;
		}

		args["destroyOnClose"] = destroyOnClose ? true : false;
		args["nonModal"] = nonModal ? true : false;
		
		
		var dialog = this._createWidgetPopup({
			id: "healthSummaryPopUp",
			widgetClass: "HealthSummaryDialog",
			args: args			
		});
	},

	/**
	 * Creates and displays an <code>ErrorDialog</code>.
	 */
	showErrorDialog : function(widgetArgs) {
		return(pizza.dialogManager._showAndReturnDialog(
			pizza.dialogManager._createErrorDialog(widgetArgs)));
	},

	/**
	 * Creates and displays an <code>ContextHelpDialog</code>.
	 */
	showContextHelpDialog : function (widgetArgs) {
	    return(pizza.dialogManager._showAndReturnDialog(
			pizza.dialogManager._createContextHelpDialog(widgetArgs)));
	},

	/**
	 * Shows a search and add dialog.
	 */
	showSearchAndAddDialog : function(args) {
		var id = args.id ? args.id : "searchAndAddDialog";
		this._createWidgetPopup({
			id: id,
			widgetClass: "SearchAndAddDialog",
			args: args
		});
	},

	/**
	 * Handles the displaying of the dialog.
	 * 
	 * @param dialog - the dialog we want to show
	 *
	 * @return dialog
	 */
	_showAndReturnDialog : function(dialog) {
		dialog.show();
		
		return(dialog);
	},

	/**
	 * This method is what actually creates the modal dialog and returns it
	 * and will allow it to be shown.
	 */
	_createResizableDialog : function(widgetArgs) {
		this._fixArgs(widgetArgs);
		// set the default id, if not provided
		if (widgetArgs.id == null) {
			widgetArgs.id = pizza.dialogManager.DEFAULT_WIDGET_ID;
		}
		
		var dialog = dijit.byId(widgetArgs.id);
		if (! dialog) {
			dialog = new pizza.widget.ResizableDialog(widgetArgs);
		}
	
		// Unregisters function to be called after the first download
		if (dialog.onLoadHandle != null) {
			dojo.disconnect(dialog.onLoadHandle);
		}	
		// Register function to be called after the first download
		if (widgetArgs.initCallback != null) {
			dialog.onLoadHandle = dojo.connect(dialog, 'onLoad', widgetArgs.initCallback);
		}
		// Unregisters a callback method to the hide event of the modal dialog
		if (dialog.hideHandle != null) {
			dojo.disconnect(dialog.onLoadHandle);
		}	
		// Registers a callback method to the hide event of the modal dialog
		if (widgetArgs.hideCallback != null) {
			dialog.hideHandle = dojo.connect(dialog, 'hide', widgetArgs.hideCallback);
		}		
	
		return dialog;
	},
	
	/**
	 * This method is what actually creates the modal dialog and returns it
	 * and will allow it to be shown.
	 */
	_createDialog : function(widgetArgs) {
		this._fixArgs(widgetArgs);
		// set the default id, if not provided
		if (widgetArgs.id == null) {
			widgetArgs.id = pizza.dialogManager.DEFAULT_WIDGET_ID;
		}
		
		var modalDialog = dijit.byId(widgetArgs.id);
		if (! modalDialog) {
			modalDialog = new pizza.widget.Dialog(widgetArgs);
		}
		else {
			// update some of the attributes
			if (widgetArgs.itemId != null) {
				modalDialog.itemId = widgetArgs.itemId;
			}
			if (widgetArgs.title != null) {
				modalDialog.set('title', widgetArgs.title);
			}
			if (widgetArgs.dataMap != null) {
				modalDialog.dataMap = widgetArgs.dataMap;
			}
			if (widgetArgs.href != modalDialog.href) {
				modalDialog.set('href', widgetArgs.href);
			}
		}
		
		// Unregisters function to be called after the first download
		if (modalDialog.onLoadHandle != null) {
			dojo.disconnect(modalDialog.onLoadHandle);
		}	
		// Register function to be called after the first download
		if (widgetArgs.initCallback != null) {
			modalDialog.onLoadHandle = dojo.connect(modalDialog, 'onLoad', widgetArgs.initCallback);
		}
		// Unregisters a callback method to the hide event of the modal dialog
		if (modalDialog.hideHandle != null) {
			dojo.disconnect(modalDialog.onLoadHandle);
		}	
		// Registers a callback method to the hide event of the modal dialog
		if (widgetArgs.hideCallback != null) {
			modalDialog.hideHandle = dojo.connect(modalDialog, 'hide', widgetArgs.hideCallback);
		}		
/*
		//check to see if there is validation on this page, 
		//in which case we should run init on the pop-up form
		if ( pizza.valObj )
			modalDialog.addOnDownloadEnd(pizza.valObj, "initFields");
			
		// Register functions to be executed after each download
		modalDialog.clearDownloadEndStack();
*/		
		return modalDialog;
	},

	_create2ButtonDialog : function(widgetArgs) {

		if (widgetArgs.id) {
			var dlg = dijit.byId(widgetArgs.id);
			if (dlg) {
				// update title
				dlg.set('title', widgetArgs.title);

				// update message
				var message = widgetArgs.messageKey ? pizza.getMsg(widgetArgs.messageKey) : widgetArgs.message;
				dlg.setMessage(message);


				// update item ID
				if (widgetArgs.itemId) {
					dlg.itemId = widgetArgs.itemId;
				}

				// update data map
				if (widgetArgs.dataMap) {
					dlg.dataMap = widgetArgs.dataMap;					
				}

				return dlg;
			}
		}

		if (widgetArgs.yesButtonLabel == null) {
			widgetArgs.yesButtonLabel = pizza.dialogManager.DEFAULT_YES_BUTTON_LABEL;
		}
		if (widgetArgs.noButtonLabel == null) {
			widgetArgs.noButtonLabel = pizza.dialogManager.DEFAULT_NO_BUTTON_LABEL;
		}

		return new pizza.widget.TwoBtnDialog(widgetArgs);
	},

	_createOneButtonDialog : function(widgetArgs) {
	    widgetArgs.isOneBtn = true;

	    return this._create2ButtonDialog(widgetArgs);
	},

	// Prepare args before they are passed to dialog widget.
	_fixArgs : function(args) {
		if(args.href && args.href.indexOf(pizza.config.CONTEXT_PATH) != 0) {
			args.href = pizza.config.CONTEXT_PATH + args.href;
		}
		return args;  
	},

	/**
	 * Utility method for creating and showing dialogs that have their own widget class.  These
	 * widgets can extend "pizza.widget.Dialog" or contain an instance of the dialog within
	 * it.  Input is a JavaScript object with the following optional properties.
	 *		id (String) - the widget id.  Will search for existing widget with that ID.  If found, the widget
	 *	 		will be shown and added to the dialog queue.  Otherwise, it will create a new widget
	 * 			and do the same.
	 * 		widgetClass (String) - the type of widget to create. (ex. "PrintDialog", when trying to create
	 * 			an instance of "pizza.widget.PrintDialog"
	 * 		dlgKey (String) - if the widget to be declared does not extend the dialog widget, there should be
	 * 			an instance of "pizza.widget.Dialog" within the widget.  (ex. "dialog", where this.dialog
	 * 			or this['dialog'] will give us the instance dialog widget.  This property can be null when the
	 * 			widget is a subclass of the dialog widget.
	 * 		args (object) - an object containing any properties to be passed into the widget constructor
	 */
	_createWidgetPopup : function(params) {
		var dialog;
		if (params.id) {
			dialog = dijit.byId(params.id);
		}

		if (!dialog) {
			params.args.id = params.id; // Add the ID to dialog args
			dialog = new pizza.widget[params.widgetClass](params.args);
		}
		return this._showAndReturnDialog(dialog);
	},

	_createErrorDialog : function(widgetArgs) {
		if (widgetArgs.id) {
			var dlg = dijit.byId(widgetArgs.id);
			if (dlg) {
				// update title
				dlg.set('title', widgetArgs.title);
				
				// set dialog message
				dlg.set('message', message);
			}
		}
		
		return new pizza.widget.ErrorDialog(widgetArgs);
	},

	_createContextHelpDialog : function(args) {
	    var modalDialog = dijit.byId(args.id);
		return ( (modalDialog) ? modalDialog :  new pizza.widget.ContextHelpDialog(args) );
	}

};
