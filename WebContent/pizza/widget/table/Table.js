dojo.provide('pizza.widget.table.Table');

dojo.require('pizza.widget._ExtractAttributes');
dojo.require('dijit._Widget');
dojo.require('dijit._Templated');
dojo.require("dijit._Container");

dojo.declare('pizza.widget.Table', [dijit._Widget, dijit._Templated, pizza.widget._ExtractAttributes], {

    templatePath: dojo.moduleUrl('pizza','templates/table/Table.html'),

    columns: null, /* [] of column info.
                    * Column Info: {id - column id, default get uses this value as key into json object. If this is an array then
                    *                    a multi row column will be made.
                    *               label - display label for the column
                    *               labelKey - same as label but a message key
                    *               get - function used to get the data for this column
                    *               htmlClass - an html class attribute to be added to each cell
                    *               width - specify the width for the column
                    *               padding - z | sm | md | lg | xl - Padding class to use. Overrides table setting.
                    *               link - a function, if a link function is passed in then the contents of
                    *                      the cell will be wrapped in an anchor tag and the link function will
                    *                      called with the row object when the anchor is clicked.
                    *               linkText - If the link attribute is set, and this property is set then the value of this property
                    *                      will be used as the link text in lieu of going to the model to get the link text.
                    *               linkKey - linkText but for a message key (DEPRECATED - use linkText)
                    *               subClasses - [] of html classes.  Used with multi line rows, each html class is applied to the
                    *                      appropriate sub row.
                    *               sortable - fires handleSort event when clicked
                    *               align - align attribute, if present will be added to the cell's div container
                    *               escapeHtml - whether to escape special HTML characters - set to true (default) for text-only data, false for rich text
                    *
                    */
    json: null,         /* JSON Array of table data */
    model: null,        /* pizza.widget.table.TableModel */
    sm: null,           /* pizza.widget.table.SelectionManager */
    renderFilters: null,/* Array of filters. The signature of the filter function should be (trNode, rowIndex, rowData).
                         * The filter can then manipulate the row however it needs to.
                         * Each filter is called on each row every time the data is rendered.
                         */
    dataId: '',          /* The primary key field in the data objects that back this table. */
    generateIds: false,  /* If set to true and the 'dataId' property is set and incoming data as no id,
    					  * one will be generated for it. uses _idSeq */ 
    _idSeq: 0,

    selectable: true,
    showHeader: false,
    emptyModelMessage: 'There is no data to display',
    alternateRows: true,
    oddClass: 'odd',
    evenClass: '',
    baseTableClass: 'pz',     /* This class will allways be added to the table to get the base table styles.
                               * It shouldnt have to be changed unless you want to ommit it.*/
    tableClass: 'DataTable',  /* Auxilary class added to table which is used to pick specific table styles. */
    firstChildClass: 'first',
    lastChildClass: 'last',
    headerClass: '',
    multiItemTag: 'div', // TODO: thiss should be configurable per column
    displayNoDataRow: true,
    outterPadding: 'md',      /* z | sm | md | lg | xl  - Size of the outter padding(first child pad-left, last child pad-right) */
    padding: 'sm',            /* z | sm | md | lg | xl  - Default table padding class to apply */
    truncate: true,           /* flag indicating if css 1 line truncation should be on by default */
    minRows: 0,               /* The minimum row count for the table.  Any extra rows added to meat this count will have a css class of 'empty' */
    renderChunkSize: 50,      /* The ammount of rows to render in each chunk.*/
    renderDelay: 0,           /* The time to wait between rendering chunks */
   	fixSafariIcons: true,	  /* Should we apply the icon column size fix for this table? */
    
    /* attributes that can be set through the markup - used by _ExtractAttributes mixin */
    markupAttribs: [ 'selectable', 'showHeader', 'emptyModelMessage', 'alternateRows', 
                     'oddClass', 'evenClass', 'tableClass', 'headerClass', 'displayNoDataRow',
                     'padding', 'outterPadding', 'truncate', 'dataId', 'generateIds' ],
                     
	/* attributes that can be set through the markup for columns. Use a <th> tag inside the the srcNode(<table>) */
	columnMarkupAttribs: [ 'id', { attribName: 'dataId', objectName: 'id' },
	                       'htmlClass', { attribName: 'class', objectName: 'htmlClass' }, 
	                       'subClasses', 'width', 'label', 'labelKey','padding', 'icon',
	                       'linkText', 'linkKey', 'sortable', 'align', 'doHoverTxt', 'truncateText' ],	                       
	                                 

    /* dojo attach points*/
    headerNode: null,
    bodyNode: null,
    //footerNode: null,
    
    // dont touch //
    _eRows: 0, 
    _internFilters: null, /*Array - internal render filters -- applied to all rows */
    
    _emptyGet: function(){ return '&nbsp;'; },
    
    constructor: function() {
    	this._internFilters = [];
    	this.renderFilters = [];
    },
    
    //for debug
    printWidths: function() {
        var tr = this.headerNode.rows[0];
        console.log('------------------------------------------------');
        for(var i = 0; i < tr.cells.length; i++) {
            var td = tr.cells[i];
            console.log('col: ' + i + 'style  width: ' + td.style.width);
            console.log('col: ' + i + 'actual width: ' + dojo.contentBox(td).w);
        }
    },

    postMixInProperties: function() {
    	this.inherited('postMixInProperties', arguments);
    	        
        // column descriptors parsed from markup
        // - First we check to see if an attributeNode was specified for the extractAttrib mixin,
        //   if it was we use that, otherwise use the srcNodeRef as the base node to find the <th> elements
        var markup_cols = this._parseColDescriptors(this.attributeNode||this.srcNodeRef);

        //column descriptors passed in via properties obj in constructor
        var js_cols = this.columns||[];
        
        var findIdx = function(_id){
        	for(var i=0; i<markup_cols.length; i++) {
        		if(markup_cols[i].id == _id){
        			return i;
        		}
        	}
        	return -1;
        };
        
        // if the column descriptors are passed in as a map,
        // then use the htmlId's from the markup to build out the array structure
        if(!dojo.isArray(js_cols)) {
        	var jsc=[];
        	for(var _id in js_cols) {
        		var idx = findIdx(_id);
        		if (idx == -1) {
        			throw 'Invalid dataId used when passing in column descriptors as a map';
        		}
        		jsc[idx] = js_cols[_id];
        		
      		}
      		// swap map out with new array
      		js_cols = jsc;	
        }        
        
        //combine markup descriptors with those passed via script, to make final descriptors.
        var cols = [];
        var l = Math.max(markup_cols.length, js_cols.length);
        for(var i=0; i<l; i++) {
            var cd = {};//could add default props here
            if(markup_cols[i]) dojo.mixin(cd, markup_cols[i]);
            if(js_cols[i]) dojo.mixin(cd, js_cols[i]);
            cols.push(cd);   
        }
        //finish making the column descriptors
        this.columns = this._initColDescriptor(cols);
    },
    
    //parse out any column descriptor info that has been passed in via markup
    _parseColDescriptors: function(node) {
        var rv = [];
        var self = this;
        dojo.forEach(dojo.query('th', node), function(th, i) {                
            var col = self._parseFromNode(th, self.columnMarkupAttribs);
            rv.push(col);
        });
        return rv;
    },
    
    // convert property values that could be multiples(i.e. comma seperated list), into
    // arrays, split on the comma
    _convertMultProps: function(p) {
    	if(p && dojo.isString(p) && p.indexOf(',') > -1) return p.split(',');
    	return p;
    },

    /* 
     * Set up the 'get' function for each column.
     * Do some validation on the column meta data
     */
    _initColDescriptor: function(cols) {
        var self = this;
        
        dojo.forEach(cols, function(column) {
            self._checkColAttribs(column);
            
            // allow multiple ids for a column and multiline subclasses to be passed in as
            // a comma seperated list
            column.id = self._convertMultProps(column.id);
            column.subClasses = self._convertMultProps(column.subClasses);
            if (column.escapeHtml === undefined) {
                column.escapeHtml = true;
            }
            
            if(!column.get) {

                if(column.link && dojo.isFunction(column.link)) {
                    //define a link get
                    column.get = function(rowIndex, mod, colInfo) {
                        var txt;
                        if(colInfo.linkText) {
                            txt = colInfo.linkText;
                        }
                        else if(colInfo.linkKey) {
                        	console.warn("The linkKey attribute on a table is deprecated as it prevents keys from being found by the JS message parser. Use linkText");
                        	txt = pizza.getMsg(colInfo.linkKey);
                        } 
                        else {
                            txt = mod.getDataAt(rowIndex, colInfo.id);
                        }
                        if(!txt) return null;
                        var anchor = document.createElement('a');
                        anchor.innerHTML = column.escapeHtml ? pizza.html.encodeHTML(txt) : txt;
                        if (colInfo.doHoverTxt) {
                        	dojo.attr(anchor, 'title', txt);
                        }
                        dojo.connect(anchor, 'onclick', function() {
                            colInfo.link(self.model.getData(rowIndex), rowIndex);
                        });
                        return anchor;
                    };
                }
//                else if(column.icon) {
//                     //icon get
//                    column.htmlClass = 'icon';
//                    column.get = function(rowIndex) {
//                        return "<div class='iconCont " + column.icon + "'>&nbsp</div>";
//                    };
//                    
//                } 
                else if(column.id){
                    //define a 'get' function if not supplied
                    if(!dojo.isArray(column.id)) {
                        // the default 'get'
                        column.get = function(rowIndex, mod, colInfo) {
                            var data = mod.getDataAt(rowIndex, colInfo.id);
                            return colInfo.escapeHtml ? pizza.html.encodeHTML(data) : data;
                        };
                    } else {
                        //multi line 'get'
                        column.get = function(rowIndex, mod, colInfo) {
                            var html = ["<div class='multiItemContainer'>"];
                            for(var i = 0; i < colInfo.id.length; i++) {
                            	var data = mod.getDataAt(rowIndex, colInfo.id[i]);
                                html.push('<' + self.multiItemTag);
                                if(dojo.isArray(colInfo.subClasses)) {
                                    var sc = colInfo.subClasses[i];
                                    html.push(" class='" + sc + "' ");
                                    if(sc.indexOf('truncate') > 0)
                                        html.push(" title='" + data.replace(/'/g, "&#39;") + "' ");
                                }
                                html.push('>');
                                html.push(colInfo.escapeHtml ? pizza.html.encodeHTML(data) : data);
                                html.push('</' + self.multiItemTag + '>');
                            }
                            html.push('</div>');
                            return html.join('');
                        };
                    }
                }
                else {
                    //empty get
                    column.get = self._emptyGet;
                }
            }
			else if (column.escapeHtml) {
				var g = column.get;
				column.get = function(rowIndex, model, colInfo) {
					var data = g(rowIndex, model, colInfo);
					if (dojo.isObject(data)) {
						// Assume it's a node and don't escape it
						return data;
					}
					return pizza.html.encodeHTML(data);
				};
			}
        });
        return cols;
    },


    _checkColAttribs: function(column) {
//        if((!column.id || column.id.length == 0) && !column.get) {
//            column.get = this._emptyGet;
//            return;
//        }
//        if(column.get && !dojo.isFunction(column.get)) {
//          console.warn('Table column ' + column.id + ' specified a get function that is not a function, default get will be used.');
//          column.get = null;
//        }
    },

    postCreate: function() {
    	// if we are given json data create a model from it
        if(this.json) {
            this.model = new pizza.widget.table.TableModel(this.json);
            this.json = null; //we only use this variable to get the json in, we only access it via our model object
        } else if(!this.model) {
            this.model = new pizza.widget.table.TableModel([]);
        }
        if(!dojo.isArray(this.renderFilters)) {
            this.renderFilters = dojo.isFunction(this.renderFilters) ? [this.renderFilters] : [];
        }
        if(this.alternateRows == true) {
            this._internFilters.push(dojo.hitch(this, this._altRowFilter));
        }        
        if(this.selectable == true) {
            dojo.addClass(this.domNode, 'selectable');        
            this.sm = new pizza.widget.table.SelectionManager(this);
        }
        if(this.truncate) {
            dojo.addClass(this.domNode, 'truncate');
        }        
       
        if(this.outterPadding)
            dojo.addClass(this.domNode, 'outter-' + this.outterPadding);
        
        this._render();
//        this.printWidths();
    },

    setModel: function(model) {
        if(dojo.isArray(model)) {
            //model is in json array format
            this.model = new pizza.widget.table.TableModel(model);
        } else {
            //must allready be a model object
            this.model = model;
        }
        this.clearSelections();
        this._render(true);
    },
    /* Refresh the view based on the model,
     * this shouldnt be needed in most cases, just sometimes */
    refresh: function() { this._render(true); },
    clearTable: function() {
    	this.setModel([]);
    	this.clearSelections();
	},
    findData: function(dataId, value){ return this.model.findData(dataId, value); },
    addRow: function(data, index) {
    	if(this.dataId // we have data that can be id'd
    		&& !data[this.dataId] // it is not id'd
    		&& this.generateIds) {
    		data[this.dataId] = this._idSeq++;
    	}
        if(this.model.addRow(data, index)) {
            if(this.sm) {
            	//FIXME not really a good way to know what index the row wass added at
            	//im just clearing selections for now. table model events will fix this
           	    this.sm.clearSelections();
            }
            this._render(true);
            return true;
        }
        return false;
    },
    removeRow: function(rowIndex) {
        var r = this.model.removeRow(rowIndex);
        if(r) {
            if(this.sm) this.sm.adjustForDelete(rowIndex);
            this._render(true);
        }
        return r;
    },
    removeRowById: function(rowId) {
        var i = this.model.findData(this.dataId, rowId);
        if(i > -1) return this.removeRow(i);
        return null;
    },
    getAll: function() { return this.model.getAll(); },
    getRow: function(rowIndex) {
        return this.model.getData(rowIndex);
    },
    getRowById: function(rowId) {
        var i = this.model.findData(this.dataId, rowId);
        if(i > -1) return this.getRow(i);
        return null;
    },
    getSelectedRow: function() {
        var r = this.getSelectedRows();
        if(r) return r[0];
        return null;
    },
    getSelectedRowId: function() {
        var r = this.getSelectedRow();
        if(this.dataId && r) return r[this.dataId];
        return r;    
    },
    getSelectedRows: function() {
        if(this.sm && this.sm.hasSelection()) {
            var r = [];
            dojo.forEach(this.sm.selectedIndices, dojo.hitch(this, function(i){
                    r.push(this.model.getData(i));
                }));
            return r;
        }
        return null;
    },
    getSelectedIndices: function() {
        if(this.sm) return [].concat(this.sm.selectedIndices);//clone
        else return [];
    },
    getRowCount: function() {
        return this.model.getRowCount();
    },
    clearSelections: function() {
        if(this.sm) this.sm.clearSelections();
    },
    moveRow: function(rowIndex, adjust) {
        if(this.model.moveRow(rowIndex, adjust)) {
           if(this.sm) this.sm.adjustForMove(rowIndex, adjust);
           this._render(true);
           return true;
        }
        return false;
    },
    selectRow: function(indices, append) {
        if(this.sm) this.sm.selectRow(indices, append);
    },
    
    /*
     * Handles a sortable column being clicked
     */
    handleSort: function(/*Column*/column) {
    	if (!column.sortAsc) {
    		column.sortAsc = true;
    	}
    	else {
    		column.sortAsc = !column.sortAsc;
    	}
    	this.onSort(column);

		// reset all the other sortable columns 
		dojo.forEach(this.columns, dojo.hitch(this, function(col, colIdx) {
			if (column.id != col.id) {
				col.sortAsc = null;
			}
		}));
		
    	this._setColumnSortingStatus();
    },

	resetSortingStatus: function() {
		dojo.forEach(this.columns, dojo.hitch(this, function(col, colIdx) {
			if(col.sortable) {
				col.sortAsc = null;
			}
		}));
		this._setColumnSortingStatus();
	},
	
	setBodyVisible: function(/*Boolean*/visible) {
		dojo.style(this.bodyNode, 'display', (visible == false ? 'none' : ''));
	},
	 
    /*
     * Internal functions
     */

	// NOTE - rendering can be asynchronous for large data sets
    _render: function(skipHeader) {
        if(!skipHeader) this._renderHeader();
        this._renderBody();
        if(this.sm)
            this.sm.renderSelections();
        //this._renderFooter();
    },


    _renderHeader: function() {
        if(!this.headerNode) return;

		//create the th nodes and fill with label
        if(this.headerNode.rows.length == 0) {

            //even if we are not showing the header we still build it because
            //it drives the layout for some browsers
            if(this.showHeader == true) this.headerNode.style.display = '';
            else this.headerNode.style.display = 'none';

            var tr = this.headerNode.insertRow(0);
            if (this.headerClass) {
            	dojo.addClass(tr, this.headerClass);
            }
            dojo.forEach(this.columns, dojo.hitch(this, function(col, colIdx) {
                var th = document.createElement('th');
                this._setUpCol(th, col, colIdx);
                var innerHTML = col.label ? col.label : (col.labelKey ? pizza.getMsg(col.labelKey) : '&nbsp;');
                var sortIndDiv = 'sortIndDiv'+colIdx;
                
                // add a sorting image indicator container
                innerHTML = '<span>' + innerHTML + '</span><div id=\''+sortIndDiv+'\' class=\'dijitInline\'></div>';
                 
                th.innerHTML = innerHTML;
                tr.appendChild(th, colIdx);
                if(col.sortable) {
                	dojo.connect(th, 'onclick', dojo.hitch(this, 'handleSort', col)); 
                	dojo.query('span', th).addClass('link');               	
                }
            }));
        }
        //set appropriate sort state class, if any of the column is sortable
        this._setColumnSortingStatus();
    },
 
 	_setColumnSortingStatus: function() {
        dojo.forEach(this.columns, dojo.hitch(this, function(col, colIdx) {
    		var th = this.headerNode.rows[0].cells[colIdx];
            if(col.sortable) {
            	dojo.addClass(th, 'sortableHeader');
            	var sortIndDivId= 'sortIndDiv'+colIdx;
        		var sortIndDiv = dojo.byId(sortIndDivId);
        		dojo.removeClass(sortIndDiv, 'sortAsc');
        		dojo.removeClass(sortIndDiv, 'sortDsc');
            	if(col.sortAsc == true){
            		dojo.addClass(sortIndDiv, 'sortAsc');
            	}
            	else if(col.sortAsc == false){
            		dojo.addClass(sortIndDiv, 'sortDsc');
            	}
            }
    	}));
	},
 	
    _renderBody: function() {
		this._delayedRender(0);
    },
    
    /**
     * Render the body rows.  Rendering will be done in chunks according to the defined chunk size.
     * When a chunk is completed a timeout will be created to render the next chunk. This is to avoid scripts
     * that run to long.
     * Hook into the renderComplete event to find out when rendering is completed.
     */
    _delayedRender: function(start) {
    	if(!this._renderRange(start, start + this.renderChunkSize)) {
	    	var self = this;
			setTimeout(function(){
				self._delayedRender(start + self.renderChunkSize);	
    		}, this.renderDelay);	
    	}else{
    		this.renderComplete();
    	}
    },
    
    _renderRange: function(start,stop) {
//    	console.log('rendering rows ' + start + ' - ' + stop);
		var dataCount = this.model.getRowCount();
        var rows = this.bodyNode.rows;
        // useableRowCount is the count of rows we can use(not empty rows)
        var useableRowCount = rows.length - this._eRows; 
    	var tr;

        dojo[dataCount > 0 ? 'removeClass' : 'addClass'](this.bodyNode, 'empty');
        var lastDataRow = Math.min(stop, dataCount);        
        var last = -1;
        for(var i = start; i < lastDataRow; i++){
        	if(i >= useableRowCount) {
        		// we need to make the row
        		tr = this._makeTR(i);
        	}
        	else {
        		tr = rows[i];
        	}
        	// BUGFIX #9592 - Bail if tr doesn't still exist (because the dialog containing the table closed, for example)
        	if (!tr) {
        		return true;
        	}
        	this._fillRow(tr, i);
        	
        	if(i == 0)             dojo.addClass(tr, 'first');
        	if(i == dataCount - 1) dojo.addClass(tr, 'lastData');
        	
        	var data = this.model.getData(i);
        	this._applyFilters(this.renderFilters, tr, i, data);
        	this._applyFilters(this._internFilters, tr, i, data);        	
        	last = i;
        }        
       
       // this block will only be executed after all rows have been rendered
       if (last == dataCount - 1 || last == -1) {
			// remove extra rows we didnt need
			for(var i = useableRowCount - 1; i >= dataCount; i--)
				this.bodyNode.deleteRow(i);
			
	        //empty row management
	        this._manageEmpty(dataCount);
	        //if there are e rows apply the internal filters to them
	        if(this._eRows > 0){
				for(var x = rows.length - this._eRows; x < rows.length; x++){
        			this._applyFilters(this._internFilters, rows[x], x, null);
				}
			}	       
	       
	       //if we are empty and have an empty row
		    if(dataCount == 0 && this.displayNoDataRow && rows.length > 0){
		    	tr = rows[0];
		        tr.cells[0].innerHTML = this.emptyModelMessage;
		        dojo.addClass(tr, 'eMessage');
		    }
		    
		   	if(rows.length > 0) dojo.addClass(rows[rows.length - 1], 'last');

			/*
			 * Safari only:
			 * When table-layout in CSS is set to fixed, cells with fixed widths (as opposed to
			 * percentage widths, which are more commonly used in our apps for liquid layout) disregard
			 * padding.  Adding a hack to add the left and right padding to the width of the cell content.
			 * This hack applies to icon columns only, and only when fixSafariIcons is true (default). For
			 * an example of a page where this hack isn't applied, see Admin>Support>Member Health History.
			 */
			if (dojo.isSafari || dojo.isChrome && this.fixSafariIcons) {
				var icons = dojo.query("td.icon", this.bodyNode);
				var tdCont = dojo.query("div.tdCont", icons[0]);
				var width = icons.style("padding-left")[0] + icons.style("padding-right")[0]
					+ tdCont.style("width")[0];
				//console.debug("width: " + width);
				icons.style("width", width + "px");

				dojo.query("th.icon", this.headerNode).style("width", width + "px");
			}
		    
		    // we is done
		    return true;
        }
        // more left to render
        return false;        
    },

    /*
     * Create a new TR element in the body at the given index.
     */
	_makeTR: function(idx) {
		var tr = this.bodyNode.insertRow(idx);
        //build the columns
        for(var j=0; j<this.columns.length;j++){
        	this._makeTD(tr,this.columns[j],j);
        }
        //add listener
        if(this.sm)
            dojo.connect(tr, 'onclick', this.sm, 'handleRowClick');
        return tr;
     },
     _makeTD: function(tr, col, colIdx){
    	var td = tr.insertCell(colIdx);
        this._setUpCol(td, col, colIdx);
        var align = col.align ? ' align=\'' + col.align + '\' ' : '';
        td.innerHTML = "<div class='tdCont'" + align + "></div>";
        return td;	
	}, 
    /* 
     * Set up  column node based on the info in the column descriptor.
     * This function is called to configure the TD element when it is first created.
     * It should set static properties on the column, rather then things that are dynamic
     * based on the data currently being displayed in the column.
     * @param colNode - the TD element
     * @param colInfo - the descriptor
     * @param idx - the column index 
     */
    _setUpCol: function(colNode, colInfo, idx) {
		// build the column class
    	var cls = '';
        if(colInfo.htmlClass) cls = colInfo.htmlClass;        
        if(colInfo.icon)      cls += ' icon '  + colInfo.icon;
        if(idx == 0)          cls += ' ' + this.firstChildClass;
        else if(idx==this.columns.length-1) cls += ' ' + this.lastChildClass;
        // set padding class - check for colInfo override, else use global
        cls += ' ' + (colInfo.padding ? colInfo : this)['padding'];
		dojo.addClass(colNode, cls);    
        if(colInfo.width) colNode.style.width = colInfo.width;
    },
    
    /**
     * Fill the given row with the data from the model.
     * @param tr - the TR element, all columns(TD) should be created and ready.
     * @param rowIdx - the row index
     * @param dataCount - the number of records in the model, passed as a parm for efficiency
     */
    _fillRow: function(tr,rowIdx) {
    	var _cols = this.columns;
    	
    	// when the record that a row represents changes then the class needs to be wiped 
    	tr.className = '';

        //fill the columns
        dojo.forEach(tr.cells, function(td, colIdx){            
            var tdCont = td.firstChild;            
            colInfo = _cols[colIdx];
            var cellData = colInfo.get(rowIdx, this.model, colInfo);
            if (!cellData && cellData != 0) cellData = '&nbsp;';

			// If column contains a link, add the link class to the cell so the ellipsis in IE will
			// be have the link color too.
			if (colInfo.link) {
				dojo.addClass(tdCont, "ellipsisLink");
			}
			
			// If column text should be truncated, add the "truncate" class to
			// the cell. Truncated text will be shown on a single line and in
			// some cases be followed by ellipsis (e.g. "Foo..."); this allows
			// us to apply the truncation attribute on a more selective basis.
			if (colInfo.truncateText) {
				dojo.addClass(tdCont, "truncate");
			}

            //the data is either primitive or a node
            if(dojo.isString(cellData) || typeof cellData == 'number' || typeof celData == 'boolean') {
                tdCont.innerHTML = cellData;

				/*
				 * If the column data or markup has an attribute of "doHoverTxt" set to true, 
				 * put the cell data into the containing div's title attribute so we have full 
				 * mouseover text on that cell.  This is only when the cell data is not an HTML node.
				 */ 
                if (colInfo.doHoverTxt) {
                	dojo.attr(tdCont, 'title', cellData);
                }
            } else {
            	//node
                dojo.empty(tdCont);
                tdCont.appendChild(cellData);
            }
        }, this);
    },    
	/*
	 * Manage our 'empty' rows.  These rows are kept distinct from our actual data rows
	 * because they are made up of just one cell that spans the enitre row.  So they can't
	 * be used as actual data rows.
	 */
    _manageEmpty: function(dataCount) {
        if(dataCount < this.minRows) {
            var weNeed = this.minRows - dataCount;
            var firstERow = this.bodyNode.rows.length - this._eRows;
            if(this._eRows && dojo.hasClass(this.bodyNode.rows[firstERow], 'eMessage')){
                //check if the first empty row contains the no data message, if it does replace it
                this.bodyNode.deleteRow(firstERow);
                this._insertEmpty();    
            }
            
            if(this._eRows > weNeed){
                //remove some empties
                for(; this._eRows > weNeed; this._eRows--){
                    this._removeLast();
                }
            }
            else if(this._eRows < weNeed){
                //add some empties
                for(; weNeed - this._eRows > 0; weNeed--){
                    this._insertEmpty();
                }
            }
            // store the number of empty rows
            this._eRows = this.minRows - dataCount;
        }
        else{
        	// we dont need any e rows, if we have some delete them
            if(this._eRows){
                for(; this._eRows > 0; this._eRows--){
                    this._removeLast();                    
                }
            }
        }    	
    },
    // insert an 'empty' row as the last row in the tbody    
    _insertEmpty: function() {
        var tr = this.bodyNode.insertRow(this.bodyNode.rows.length);
        dojo.addClass(tr, 'emptyRow');
        var td = tr.insertCell(0); 
        td.colSpan = this.columns.length;
        td.innerHTML = '&nbsp;';
    },
    // remove the last row in the tbody
    _removeLast: function() { this.bodyNode.deleteRow(this.bodyNode.rows.length - 1); },

    _applyFilters: function(filters, tr, i, data) {
//    	console.log('applying filters to row: ' + i);
    	dojo.forEach(filters, function(filter){ filter(tr, i, data); });
    },
    

    _altRowFilter: function(tr, index) {
        // trs get classes wiped but not for empty rows!
        // so for now we need to remove the opposite class everytime
        if(index % 2 == 1) {
            if(this.oddClass)  dojo.addClass(tr, this.oddClass);
            if(this.evenClass) dojo.removeClass(tr, this.evenClass); 
        } else {
            if(this.evenClass) dojo.addClass(tr, this.evenClass);
            if(this.oddClass)  dojo.removeClass(tr, this.oddClass);
        }
    },

    /*
     * Events
     */

    onRowSelected: function(selectedRowData) {},
    
	/*
	 * Callback for a sortable column being clicked
	 */
	onSort: function(/*Column*/col) {
    	// stub, use dojo.connect(yourTable, 'onSort', ...
	},
	     
	renderComplete: function() {
    	// callback when all rows have been rendered
    }
}
);

dojo.declare('pizza.widget.table.TableModel', null, {

    json: null, /* JSON Array data backing */
    constructor: function(jsonArray) {
        if(jsonArray) this.json = jsonArray;
        else this.json = [];
    },
    getAll: function() { return this.json; },
    /*
     * Get the data at the specified row with the specified id.
     * The id can be a nested element, using '.'
     */
    getDataAt: function(rowIndex, dataId) {
        if(this._isInBounds(rowIndex)) {
            return dojo.getObject(dataId, false, this.json[rowIndex]);
        }
        return null;
    },

    getData: function(rowIndex) {
        return this._isInBounds(rowIndex) ? this.json[rowIndex] : null;
    },

    getRowCount: function() {
        return this.json && this.json.length ? this.json.length : 0;
    },

    /*
     * Add the object to the data model.
     * return true if successfully added, otherwise false.
     */
    addRow: function(data, index) {
        if(data) {
            if(this._isInBounds(index)) {
                this.json.splice(index, 0, data);
            } else {
               this.json.push(data);
            }
            return true;
        }
        return false;
    },

    removeRow: function(rowIndex) {
        if(this._isInBounds(rowIndex)) {
            var ret = this.json.splice(rowIndex, 1);
            return ret[0];
        }
        return null;
    },

    moveRow: function(rowIndex, adjust) {
        if(this._isInBounds(rowIndex) && this._isInBounds(rowIndex + adjust)) {
            var mv = this.json.splice(rowIndex, 1);
            this.json.splice(rowIndex + adjust, 0, mv[0]);
            return true;
        }
        return false;
    },

    findData: function(dataId, value) {
        for (i = 0; i < this.json.length; i++) {
            if (this.json[i][dataId] == value) return i;
        }
        return -1;
    },

    _isInBounds: function(rowIndex) {
        return rowIndex != undefined && rowIndex >= 0 && rowIndex < this.getRowCount();
    }
}
);

dojo.declare('pizza.widget.table.SelectionManager', null, {
    table: null, /* pizza.table.Table */
    blockSelections: false, /* boolean - flag to allow for the blocking of table selections, e.g. for a temporary shut off */
    selectedIndices: [],
    constructor: function(table) {
        this.table = table;
    },
    /**
     * Return true if any rows are selected, else false.
     */
    hasSelection: function() {
        return this.selectedIndices.length > 0;
    },
    /**
     * Clear the all current selections.
     */
    clearSelections: function() {
        this.selectedIndices = [];
        this.renderSelections();
    },
    /**
     * Is the row with the given index currently selected?
     */
    isRowSelected: function(index) {
        dojo.forEach(this.selectedIndices, function(i) {
            if(i == index) return true;
        });
        return false;
    },
    /**
     * Callback for tr row clicks. Internal
     */
    handleRowClick: function(evt) {

        var tr_clicked = this._findTRTarget(evt.target);
        if(!tr_clicked) {//sanity check
            console.warn('SelectionManager#handleRowClick called and TR could not be determined.');
            return;
        }
        if(tr_clicked.sectionRowIndex < 0) {
        	// this is possible if a previous event handler removed the clicked row as part of its handling action
        	return;
        }
        
       // if(dojo.hasClass(tr_clicked, 'emptyRow')) return;//empty
        if(dojo.hasClass(tr_clicked, 'selected')) return;//row allready selected

        this.selectRow(tr_clicked.sectionRowIndex);
    },

    /**
     * Selct the row or rows with the given indice.
     * If you pass append=true then the current selections
     * will remain, otherwise they will be cleared.
     */
    selectRow: function(indices, append) {
        if(!this.blockSelections) {
            if(append != true) {
                this.selectedIndices = [];//clear selections
            }
    
            if(!dojo.isArray(indices)) indices = [indices];
    
            dojo.forEach(indices, dojo.hitch(this, function(idx) {
            	// make sure the selection idx is not already marked selected and is a valid idx 
                if(!this.isRowSelected(idx) && idx >= 0)
                    this.selectedIndices.push(idx);
            }));
    
            this.table.onRowSelected(this.selectedIndices);
    
            this.renderSelections();
        }
    },

    renderSelections: function() {
        var bdy = this.table.bodyNode;
        //clean slate
        dojo.forEach(bdy.rows, function(tr) { dojo.removeClass(tr, 'selected'); });

        //add class to selected rows
        dojo.forEach(this.selectedIndices, function(index) {
            if(bdy.rows.length > index) {
                dojo.addClass(bdy.childNodes[index], 'selected');
            }
        });
    },
    adjustForDelete: function(deletedRow) {
    	var newSelection = [];
        dojo.forEach(this.selectedIndices, dojo.hitch(this, function(idx){
            if(idx >= deletedRow) {
            	if(idx != deletedRow) newSelection.push(idx - 1);
            }else{
                newSelection.push(idx);
            }
        }));
        this.selectedIndices = newSelection;
        this.renderSelections();
    },
    adjustForMove: function(movedRow, adjust) {
    	var newSelection = [];
    	var newHome = movedRow + adjust;
    	dojo.forEach(this.selectedIndices, dojo.hitch(this, function(idx){
    		if(idx == movedRow) {
    			//row being moved was selected
    			newSelection.push(newHome);
    		}else if(idx < movedRow && idx < newHome) {
    		  	//index being examined is not effected by the move
    		  	newSelection.push(idx);
    		}else if(idx > movedRow && idx > newHome) {
    			//index being examined is not effected by the move
    			newSelection.push(idx);
    		}else if(idx > movedRow && idx <= newHome) {
    			//shift down
    			newSelection.push(idx - 1);
    		}else if(idx < movedRow >= newHome) {
    			//shift up
    			newSelection.push(idx + 1);
    		}
    		else console.log('Serious flaw in logic adjusting selection. movedRow: '
		                   + movedRow + ' adjust: ' + ajust + ' examining idx: ' + idx);
    	}));
    	this.selectedIndices = newSelection;
    	this.renderSelections();

    },

    /**
     * Internal, finds the TR element for the onclick event.
     * This is needed because sometimes the source element of the
     * event is a child of the TR and not the TR itself.
     */
    _findTRTarget: function(node) {
        while(node && node.nodeName.toUpperCase() != 'TABLE') {
            if(node.nodeName.toUpperCase() == 'TR') return node;
            node = node.parentNode;
        }
    }
}
);
