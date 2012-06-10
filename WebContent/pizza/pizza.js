dojo.provide('pizza.pizza');


/* core system */
if(pizza.isDebug) console.log("Loading pizza core...");
dojo.require('pizza.Validator');
dojo.require('pizza.validation.Validator');

/*
 * 
 * begin page initialization
 * 
 * 
 */
	
(function() {
	

	// **
	// The application (pizza) will automatically instantiate
	// a validator (pizza.validation.Validator) if dynamic validation data
	// has been supplied upon loading the page. 
	//
	// The concept of "adding custom validation" is kept through the
	// pizza.addCustomValidation(...) function. 
	//
	pizza.valObj = new pizza.validation.Validator();
	if (pizza._valData){
		console.log('Adding validation data: ' + dojo.toJson(pizza._valData));			
		dojo.addOnLoad(function() {
			pizza.valObj = new pizza.validation.Validator(pizza._valData);
			if (pizza.addCustomValidation) {
				pizza.addCustomValidation(pizza.valObj);
			}
		});		
	}
	
	
	//flag that the core modules have finished loading
	pizza._loaded = true;
	
	// DEFERRED LOADING
	// dynamically load page specific modules
	if(pizza._requires) {
		for (var i=0; i<pizza._requires.length; i++) {
			var mod = pizza._requires[i];
			console.log('Loading module: ' + dojo.toJson(mod));
			dojo['require'](mod.module);
			if(mod.init) {
				dojo.addOnLoad(eval(mod.initScope), mod.init);
			}
		}	
	}
	//	// add onloads that were stored in _onLoads prior to our core libs being loaded
	//	while(pizza._onLoads.length) {
	//		console.log('firing an onloads');
	//		dojo.addOnLoad(pizza._onLoads.shift());		
	//	}
	
})();
