dojo.provide("pizza.nav");

dojo.require("dojo.back");

pizza.nav = {
		
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

		
		this.orderCont = dojo.byId("orderCont");
		this.addressCont = dojo.byId("addressCont");
		this.placeOrderMssg = dojo.byId("placeOrderMssg");
		this.statusCont = dojo.byId("statusCont");
		
		this.orderTab = dojo.query("a[href=#order]")[0];
		this.addressTab = dojo.query("a[href=#address]")[0];
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
			return "order";
		}
	},
	
	
	showContent: function (fragment) {
		if (fragment == 'order') {
			this._showOrderCont();
			dojo.addClass(this.orderTab, "selected");
			dojo.removeClass(this.addressTab,"selected");
			dojo.removeClass(this.statusTab,"selected");
		}
		if (fragment == 'address') {
			this._showAddressCont();
			dojo.removeClass(this.orderTab, "selected");
			dojo.addClass(this.addressTab,"selected");
			dojo.removeClass(this.statusTab,"selected");
		}
		if (fragment == 'status') {
			this._showStatusCont();
			dojo.removeClass(this.orderTab, "selected");
			dojo.removeClass(this.addressTab,"selected");
			dojo.addClass(this.statusTab,"selected");
		}
		dojo.back.addToHistory(new this.State(fragment));
	},
	
	_showOrderCont: function() {
		dojo.addClass(this.addressCont,"hideme");
		dojo.addClass(this.statusCont, "hideme");

		pizza.landing.updateOrderBtnTxt();
		dojo.removeClass(this.orderCont,"hideme");
		dojo.removeClass(this.placeOrderMssg, "hideme");
	},
	
	_showAddressCont: function() {
		dojo.addClass(this.orderCont,"hideme");
		dojo.addClass(this.statusCont, "hideme");
		dojo.addClass(this.placeOrderMssg, "hideme");
		
		
//		pizza.landing.updateAddressBtnLabel();
		dojo.removeClass(this.addressCont,"hideme");

	},
	
	_showStatusCont: function(){
		dojo.addClass(this.addressCont,"hideme");
		dojo.addClass(this.placeOrderMssg, "hideme");
		dojo.addClass(this.orderCont,"hideme");
		dojo.removeClass(this.statusCont, "hideme");
		
		
		pizza.customerReport.showCustomerAddress();
		// the grid won't start up unless it is not hidden
		pizza.customerReport.orderGrid.startup();
		
		// force the grid to reload
		pizza.customerReport.loadOrders();
//		pizza.customerReport.orderStore.close();
//		pizza.customerReport.orderStore.fetch();
//		pizza.customerReport.orderGrid._refresh();
//		pizza.customerReport.init();
	}

}