dojo.provide("pizza.admin.nav");

dojo.require("dojo.back");

pizza.admin.nav = {
		
	init: function() {
		
		this.State = function(fragment) {
			this.changeUrl = fragment;
		};
		dojo.extend(this.State, {
			back: function() {
				pizza.nav.showContent(this.changeUrl);
			},
			forward: function() {
				pizza.nav.showContent(this.changeUrl);
			}
		});

		
		this.productCont = dojo.byId("productCont");
		this.statusCont = dojo.byId("statusCont");
		this.productTab = dojo.query("a[href=#products]")[0];
		this.statusTab = dojo.query("a[href=#status]")[0];
		dojo.connect(dojo.byId("topNav"), "click", dojo.hitch(this, this.handleTopNav));
		
		var initialFragment = this.getFragment();
		dojo.back.setInitialState(new this.State(initialFragment));
		this.showContent(initialFragment);

	},
	
	handleTopNav: function(e) {
		dojo.stopEvent(e);
		var anchorNode = null;
		if( e.target.tagName != 'A') {// if the td element is clicked get the 'a' node
			anchorNode = dojo.query("> a", e.target)[0];
		}
		else {
			anchorNode = e.target;
		}
		var fragment = anchorNode.getAttribute("href").split('#')[1];
		this.showContent(fragment);
	},
	
	getFragment: function(){
		var parts = window.location.href.split("#");
		if (parts.length == 2) {
			return parts[1];
		}
		else {
			return "products";
		}
	},
	
	
	showContent: function (fragment) {
		if (fragment == 'products') {
			this._showProductCont();
			dojo.removeClass(this.statusTab, "selected");
			dojo.addClass(this.productTab,"selected");
		}
		if (fragment == 'status') {
			this._showStatusCont();
			dojo.removeClass(this.productTab, "selected");
			dojo.addClass(this.statusTab,"selected");
		}
		dojo.back.addToHistory(new this.State(fragment));
	},
	
	_showProductCont: function() {
		dojo.addClass(this.statusCont, "hideme");
		dojo.removeClass(this.productCont,"hideme");
	},
	
	_showStatusCont: function(){
		dojo.addClass(this.productCont,"hideme");
		dojo.removeClass(this.statusCont, "hideme");
		
//		pizza.admin.adminReport.orderGrid.startup();
//		pizza.admin.adminReport.orderStore.close();
		pizza.admin.adminReport.orderStore.fetch();
		pizza.admin.adminReport.orderGrid._refresh()
		
	}

}