
dojo.provide("pizza.widget.OrderTopping");

dojo.require("pizza.ToppingChangeMssg");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");

dojo.declare(
		"pizza.widget.OrderTopping",
		[dijit._Widget, dijit._Templated],
		{
			templatePath: dojo.moduleUrl("pizza", "templates/OrderTopping.html"),
			
			constructor: function(args) {
				this.id = args.id;
				this.topName = args.topName;
				console.debug("in OrderTopping Constructor:" + args);
//				dojo.connect(this.xImg,"onclick", this, this._handleDelete);
			},
			
			
			postCreate: function() {
				dojo.connect(this.xImg,"onclick", this, this._handleDelete);
			},
			
			_handleDelete: function(){
				console.debug("in _handleDelete:" + this.topName);
				dojo.publish("toppingChange",
						[new pizza.ToppingChangeMssg({action:"delete", id:this.id,  topName:this.topName})]);
			},
			
			destroy: function(){
				console.debug("in orderTopping destroy!!!");
				this.inherited(arguments);
			}
			
			

			
		}
);

