function _dumpObject(o){
  if (o===null) {
    return '<span class="null">null</span>';
  } else if (typeof o=="number") {
    return '<span class="number">' + o + '</span>';
  } else if (typeof o=="boolean") {
    return '<span class="boolean">' + o + '</span>';
  } else if (dojo.isString(o)) {
    return '<span class="string">' + o.replace(/</g, "&lt;") + '</span>';
  } else if (dojo.isFunction(o)) {
    return '<span class="function">function</span>';
  } else if (dumpObject.level==dumpObject.maxLevel) {
    return '<span class="info">more omitted...</span>';
  }
  
  dumpObject.level++;
  var base={};
  var result= "";
  
  if (dojo.isArray(o)) {
    for (var i= 0, end= o.length; i<end; i++) {
      try {
        result += "<tr><td>[" + i + "]</td><td>" + _dumpObject(o[i]) + "</td></tr>";
      } catch(e) {
        if (dumpObject.dumpErrors) {
          result += "<tr><td>[" + i + ']</td><td><span "error">dump failed: ' + e + "</span></td></tr>";
        }
      }    
    }
  } else {
    var count= 0;
    for (p in o) if (base[p] === undefined || base[p] != o[p]) {
      count++;
      if (count>dumpObject.maxCount) {
        result += '<tr><td class="truncated">properties truncated</td><td></td></tr>';
        break;
      }
      try {
        result += "<tr><td>" + p + "</td><td>" + _dumpObject(o[p]) + "</td></tr>";
      } catch(e) {
        if (dumpObject.dumpErrors) {
          result += "<tr><td>" + p + '</td><td><span "error">dump failed: ' + e + "</span></td></tr>";
        }
      }    
    }
  }
  result= '<table class="dumpTable level' + dumpObject.level + '">' + result + "</table>"
  dumpObject.level--;
  return result;
}

function dumpObject(o, props){
  var save = [dumpObject.maxLevel, dumpObject.maxCount];
  if (props && props.maxLevel) {
    dumpObject.maxLevel = props.maxLevel
  }
  if (props && props.maxCount) {
    dumpObject.maxCount = props.maxCount
  }
  var result= _dumpObject(o);
  dumpObject.maxLevel= save[0];
  dumpObject.maxCount= save[1];
  return result;
}
dumpObject.level= -1;
dumpObject.maxLevel= 2;
dumpObject.maxCount=  7;
dumpObject.dumpErrors= false;


function doExample(e){
  //prevent submitting the form...
  dojo.stopEvent(e);
  
  var exampleId= Number(dojo.byId("exId").value);
  if (window["example"+exampleId]) {
    //clear out the last results...
    var resultNode= dojo.byId("result");
    resultNode.innerHTML= ""
    dojo.toggleClass(resultNode, "error", false);
    dojo.byId("objects").innerHTML= "";
    
    //run the example...
    window["example"+exampleId]();
  } else {
    alert("Invalid example identifier provided.  Try again.");
  }
}

dojo.addOnLoad(function(){
  dojo.connect(dojo.byId("exTrigger"), "click", doExample);
})

