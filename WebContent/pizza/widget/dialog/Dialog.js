dojo.provide('pizza.widget.dialog.Dialog');

dojo.require('dijit.Dialog');

dojo.declare(
	'pizza.widget.Dialog',
	dijit.Dialog,
	{
		onLoad : function() {
			this.inherited("onLoad", arguments);

			this._initializeButtons();
		},
		_initializeButtons : function() {
			var hideButton = dojo.byId(this.id + "-hider");
			if (hideButton) {
					this.noBtn = new dijit.form.Button({
						onClick: dojo.hitch(this, "closeDialog")
					}, hideButton);
			}

			var submitButton = dojo.byId(this.id + "-submit");
			if (submitButton) {
					this.yesBtn = new dijit.form.Button({
						onClick: dojo.hitch(this, "submit"),
					}, submitButton);
				}

		},
		closeDialog: function() {
			this.hide();
//			this.destroy();
		},
		submit: function() {
			this.submitAction();
		}

	}
);
