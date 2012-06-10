dojo.provide("pizza.ToppingChangeMssg");

dojo.declare("pizza.ToppingChangeMssg", null, {

	constructor: function(args){
		if(!args.action) {
			throw "pizza.ToppingChangeMssg: action is required";
		}
		if(!args.id) {
			throw "pizza.ToppingChangeMssg: id is required";
		}
		if(!args.topName) {
			throw "pizza.ToppingChangeMssg: topName is required";
		}
		this.action = args.action;
		this.id = args.id;
		this.topName = args.topName;
	}
});
