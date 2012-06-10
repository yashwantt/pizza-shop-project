dojo.provide("pizza.LoginPage");


dojo.require("pizza.widget.Button");
dojo.require("dojo.cookie");

dojo.declare("pizza.LoginPage", null, {
	loginButton: null,

	constructor : function() {
		var buttonNode = dojo.byId("loginBtn");
		var enrollNode = dojo.byId("enrollBtn");
		
		if (buttonNode && enrollNode) {
			this.loginButton = new pizza.widget.Button({
				label: pizza.getMsg("login.consumer.login"),
				size: "large",
				color: "green",
				onClick: dojo.hitch(this, "handleLoginClick")
			}, buttonNode);
			new pizza.widget.Button({
				label: pizza.getMsg("login.consumer.enroll"),
				size: "large",
				color: "blue",
				onClick: dojo.hitch(this, "handleEnrollClick")
			}, enrollNode);
		}
		else if (buttonNode) {
			if (buttonNode) {
				this.loginButton = new pizza.widget.Button({
					label: pizza.getMsg("login.signin"),
					size: "small",
					onClick: dojo.hitch(this, "handleLoginClick")
				}, buttonNode);
			}
		}
		
		if (document.loginform && document.loginform.j_username) {
			dojo.connect(document.loginform.j_username, 'onkeydown', this, 'checkEnter');
			dojo.connect(document.loginform.j_password, 'onkeydown', this, 'checkEnter');
			document.loginform.j_username.focus();
		}

		var tourNode = dojo.byId("takeTour");
		if (tourNode) {
			dojo.connect(tourNode, "onclick", this, "showVideoPopup");
		}

		// Preload resources for member or provider home depending on which login screen the user is on
		var page = dojo.byId("loginPage"); page = page ? page.value : "";
		if (page === 'physician') {
			pizza.preloadResources(['pizza.layers.providerLayer'], 'async/providerHomePreload.htm');
		}
	},

	checkEnter : function(evt) {
		if (evt.keyCode == 13) {
			if (document.loginform.j_username.value == '') {
				//document.loginform.j_username.focus();
			}
			else if (document.loginform.j_password.value == '') {
				//document.loginform.j_password.focus();
			}
			else {
				this.handleLoginClick();
			}
		}
	},

	handleLoginClick: function() {
		this.disableControls();
		this.refreshSession();
	},

	handleEnrollClick: function() {
		window.location = '/memberSinglePageEnroll.htm';
	},

	refreshSession: function() {
		// Ensures we have a fresh CSRF cookie by hitting an empty page
		// asynchronously.  This prevents the edge case of sitting on
		// the login page for more than 10 minutes and the session state
		// expiring before submitting the form.
		var args = {
			url: '/touchSession.htm',
			load: dojo.hitch(this, "handleSessionRefreshed"),
			content: {}
		};
		pizza.requestManager.ajaxGet(args);
		console.info("Sending touch");
	},

	handleSessionRefreshed: function(response, ioArgs) {
		// Reply from the touchSession.htm request.
		console.info("Got touch");
		this.submitForm();
	},

	submitForm: function() {
		console.info("Submitting form");
		pizza.setCSRFField("loginform");
		document.forms.loginform.submit();
	},

	disableControls: function() {
		this.loginButton.set('disabled', true);
		if (dojo.byId("registerLink")) {
			var registerLink = dojo.byId("registerLink");
			registerLink.removeAttribute('href');
			registerLink.style.color="gray";
		}
	}


});

dojo.addOnLoad(function() {
	new pizza.LoginPage({id: "LoginPage"});
});