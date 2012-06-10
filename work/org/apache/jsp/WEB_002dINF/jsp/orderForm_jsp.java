package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class orderForm_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fspring_005fmessage_0026_005fcode_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fform_005fform_0026_005fmethod_005fcommandName_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fform_005fradiobuttons_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fform_005ferrors_0026_005fpath_005fcssClass_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fform_005fcheckboxes_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fspring_005fmessage_0026_005fcode_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fform_005fform_0026_005fmethod_005fcommandName_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fform_005fradiobuttons_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fform_005ferrors_0026_005fpath_005fcssClass_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fform_005fcheckboxes_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fspring_005fmessage_0026_005fcode_005fnobody.release();
    _005fjspx_005ftagPool_005fform_005fform_0026_005fmethod_005fcommandName_005faction.release();
    _005fjspx_005ftagPool_005fform_005fradiobuttons_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody.release();
    _005fjspx_005ftagPool_005fform_005ferrors_0026_005fpath_005fcssClass_005fnobody.release();
    _005fjspx_005ftagPool_005fform_005fcheckboxes_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>");
      if (_jspx_meth_spring_005fmessage_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("</title>\r\n");
      out.write("<style type=\"text/css\">\r\n");
      out.write("\t@import \"/dojoroot/dijit/themes/tundra/tundra.css\";\r\n");
      out.write("\t@import \"/dojoroot/dojo/resources/dojo.css\"\r\n");
      out.write("</style>\r\n");
      out.write("<script type=\"text/javascript\" src=\"/dojoroot/dojo/dojo.js\"  \r\n");
      out.write("        djConfig=\"parseOnLoad: true\">\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("<script>\r\n");
      out.write("\tdojo.require(\"dojo.parser\");\r\n");
      out.write("\tdojo.require(\"dijit.layout.ContentPane\");\r\n");
      out.write("\tdojo.require(\"dijit.layout.TabContainer\");\r\n");
      out.write("\tdojo.require(\"dijit.form.ValidationTextBox\");\r\n");
      out.write("\tdojo.require(\"dijit.form.DateTextBox\");\r\n");
      out.write("\tdojo.require(\"dijit.form.Button\");\r\n");
      out.write("    dojo.require(\"dijit.form.Textarea\");\r\n");
      out.write("    dojo.require(\"dijit.InlineEditBox\");\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("(function(){\r\n");
      out.write("function addBRs(){\r\n");
      out.write("\tvar sizeGroup = dojo.byId(\"sizeList\");\r\n");
      out.write("\tdojo.query(\"span\", sizeGroup).forEach(function(node){\r\n");
      out.write("\t\tvar br = document.createElement(\"br\");\r\n");
      out.write("\t\tdojo.place(br,node,\"after\");\r\n");
      out.write("\t});\r\n");
      out.write("\r\n");
      out.write("\tvar toppingGroup = dojo.byId(\"toppingList\");\r\n");
      out.write("\tdojo.query(\"span\", toppingGroup).forEach(function(node){\r\n");
      out.write("\t\tvar br = document.createElement(\"br\");\r\n");
      out.write("\t\tdojo.place(br,node,\"after\");\r\n");
      out.write("\t});\r\n");
      out.write("\t\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("dojo.addOnLoad(function(){\r\n");
      out.write("   \taddBRs();\r\n");
      out.write("   \t\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("})();\r\n");
      out.write("\r\n");
      out.write("function validateForm(){\r\n");
      out.write("\tconsole.info(\"in validateForm\");\r\n");
      out.write(" \r\n");
      out.write("// \tdojo.query(\"#sizeList input\").(function(node){  \r\n");
      out.write("// \t\tif(node.checked){  \r\n");
      out.write("// \t\t    console.info(\"checked:\" + node.id); \r\n");
      out.write("// \t\t} else {\r\n");
      out.write("//                     console.info(\"not checked:\" + node.id);\r\n");
      out.write("//                 } \r\n");
      out.write("\t\t\r\n");
      out.write("//    });\r\n");
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\tvar sizeChecked = false;\r\n");
      out.write("\tsizeChecked = dojo.query(\"#sizeList input\").some(function(node){  \r\n");
      out.write("\t\treturn node.checked;\r\n");
      out.write("\t});\r\n");
      out.write("\tif(sizeChecked){\r\n");
      out.write("\t\tdojo.byId(\"sizeError\").innerHTML = \"\";\r\n");
      out.write("\t} else {\r\n");
      out.write("\t\tdojo.byId(\"sizeError\").innerHTML = \"Please select a size.\";\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\tvar toppingChecked = false;\r\n");
      out.write("\ttoppingChecked = dojo.query(\"#toppingList input\").some(function(node){  \r\n");
      out.write("\t\treturn node.checked;\r\n");
      out.write("\t});\r\n");
      out.write("\tif(toppingChecked){\r\n");
      out.write("\t\tdojo.byId(\"toppingError\").innerHTML = \"\";\r\n");
      out.write("\t} else {\r\n");
      out.write("\t\tdojo.byId(\"toppingError\").innerHTML = \"Please select at least one topping.\";\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\r\n");
      out.write("\treturn sizeChecked && toppingChecked;\r\n");
      out.write("}; \r\n");
      out.write("\t     \t\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<style>\r\n");
      out.write(" .formContainer { \r\n");
      out.write("    width:auto; \r\n");
      out.write("    height:auto; \r\n");
      out.write(" } \r\n");
      out.write("/* label { */\r\n");
      out.write("/*    width:150px; */\r\n");
      out.write("/*    float:left; */\r\n");
      out.write("/* } */\r\n");
      out.write(".error {\r\n");
      out.write("\tcolor: #ff0000;\r\n");
      out.write("\tfont-weight: bold;\r\n");
      out.write("}\r\n");
      out.write("</style>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body class=\"tundra\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t");
      //  form:form
      org.springframework.web.servlet.tags.form.FormTag _jspx_th_form_005fform_005f0 = (org.springframework.web.servlet.tags.form.FormTag) _005fjspx_005ftagPool_005fform_005fform_0026_005fmethod_005fcommandName_005faction.get(org.springframework.web.servlet.tags.form.FormTag.class);
      _jspx_th_form_005fform_005f0.setPageContext(_jspx_page_context);
      _jspx_th_form_005fform_005f0.setParent(null);
      // /WEB-INF/jsp/orderForm.jsp(116,1) name = method type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_form_005fform_005f0.setMethod("post");
      // /WEB-INF/jsp/orderForm.jsp(116,1) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_form_005fform_005f0.setAction((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${orderFormURL}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      // /WEB-INF/jsp/orderForm.jsp(116,1) name = commandName type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_form_005fform_005f0.setCommandName("pizzaOrder");
      int[] _jspx_push_body_count_form_005fform_005f0 = new int[] { 0 };
      try {
        int _jspx_eval_form_005fform_005f0 = _jspx_th_form_005fform_005f0.doStartTag();
        if (_jspx_eval_form_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          do {
            out.write("\r\n");
            out.write("\r\n");
            out.write("\t\t<div class=\"formContainer\" dojoType=\"dijit.layout.TabContainer\"\r\n");
            out.write("\t\t\tstyle=\"width: 200px; height: 100px\">\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\t\t\t<div dojoType=\"dijit.layout.ContentPane\" title=\"Size\">\r\n");
            out.write("\r\n");
            out.write("\t\t\t<span id=\"sizeList\">\r\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_form_005fradiobuttons_005f0(_jspx_th_form_005fform_005f0, _jspx_page_context, _jspx_push_body_count_form_005fform_005f0))
              return;
            out.write("\r\n");
            out.write("\t\t\t\t");
            //  form:errors
            org.springframework.web.servlet.tags.form.ErrorsTag _jspx_th_form_005ferrors_005f0 = (org.springframework.web.servlet.tags.form.ErrorsTag) _005fjspx_005ftagPool_005fform_005ferrors_0026_005fpath_005fcssClass_005fnobody.get(org.springframework.web.servlet.tags.form.ErrorsTag.class);
            _jspx_th_form_005ferrors_005f0.setPageContext(_jspx_page_context);
            _jspx_th_form_005ferrors_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_form_005fform_005f0);
            // /WEB-INF/jsp/orderForm.jsp(128,4) name = path type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
            _jspx_th_form_005ferrors_005f0.setPath("chosenSizeId");
            // /WEB-INF/jsp/orderForm.jsp(128,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
            _jspx_th_form_005ferrors_005f0.setCssClass("error");
            int[] _jspx_push_body_count_form_005ferrors_005f0 = new int[] { 0 };
            try {
              int _jspx_eval_form_005ferrors_005f0 = _jspx_th_form_005ferrors_005f0.doStartTag();
              if (_jspx_th_form_005ferrors_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                return;
              }
            } catch (Throwable _jspx_exception) {
              while (_jspx_push_body_count_form_005ferrors_005f0[0]-- > 0)
                out = _jspx_page_context.popBody();
              _jspx_th_form_005ferrors_005f0.doCatch(_jspx_exception);
            } finally {
              _jspx_th_form_005ferrors_005f0.doFinally();
              _005fjspx_005ftagPool_005fform_005ferrors_0026_005fpath_005fcssClass_005fnobody.reuse(_jspx_th_form_005ferrors_005f0);
            }
            out.write("\r\n");
            out.write("\t\t\t</span>\r\n");
            out.write("\t\t\t<div id=\"sizeError\" class=\"error\"></div>\r\n");
            out.write("\t\t\t</div>\r\n");
            out.write("\r\n");
            out.write("\t\t\t<div dojoType=\"dijit.layout.ContentPane\" title=\"Topping\">\r\n");
            out.write("\t\t\t\t<span id=\"toppingList\">\r\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_form_005fcheckboxes_005f0(_jspx_th_form_005fform_005f0, _jspx_page_context, _jspx_push_body_count_form_005fform_005f0))
              return;
            out.write("\r\n");
            out.write("\t\t\t\t</span>\r\n");
            out.write("\t\t\t\t");
            //  form:errors
            org.springframework.web.servlet.tags.form.ErrorsTag _jspx_th_form_005ferrors_005f1 = (org.springframework.web.servlet.tags.form.ErrorsTag) _005fjspx_005ftagPool_005fform_005ferrors_0026_005fpath_005fcssClass_005fnobody.get(org.springframework.web.servlet.tags.form.ErrorsTag.class);
            _jspx_th_form_005ferrors_005f1.setPageContext(_jspx_page_context);
            _jspx_th_form_005ferrors_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_form_005fform_005f0);
            // /WEB-INF/jsp/orderForm.jsp(138,4) name = path type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
            _jspx_th_form_005ferrors_005f1.setPath("chosenToppingIds");
            // /WEB-INF/jsp/orderForm.jsp(138,4) name = cssClass type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
            _jspx_th_form_005ferrors_005f1.setCssClass("error");
            int[] _jspx_push_body_count_form_005ferrors_005f1 = new int[] { 0 };
            try {
              int _jspx_eval_form_005ferrors_005f1 = _jspx_th_form_005ferrors_005f1.doStartTag();
              if (_jspx_th_form_005ferrors_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                return;
              }
            } catch (Throwable _jspx_exception) {
              while (_jspx_push_body_count_form_005ferrors_005f1[0]-- > 0)
                out = _jspx_page_context.popBody();
              _jspx_th_form_005ferrors_005f1.doCatch(_jspx_exception);
            } finally {
              _jspx_th_form_005ferrors_005f1.doFinally();
              _005fjspx_005ftagPool_005fform_005ferrors_0026_005fpath_005fcssClass_005fnobody.reuse(_jspx_th_form_005ferrors_005f1);
            }
            out.write("\r\n");
            out.write("\t\t\t\t<div id=\"toppingError\" class=\"error\"></div>\r\n");
            out.write("\t\t\t</div>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\r\n");
            out.write("\t\t<button  dojoType=\"dijit.form.Button\" type=\"submit\">\r\n");
            out.write("\t\t\tPlace Your Order\r\n");
            out.write("\t\t\t<script type=\"dojo/method\" event=\"onClick\" args=\"evt\">\r\n");
            out.write("\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\tif (validateForm()){\r\n");
            out.write("\t\t\t\t\t\tconsole.info(\"validation passed.\");\r\n");
            out.write("\t\t\t\t\t} else{\r\n");
            out.write("\t\t\t\t\t\tconsole.info(\"validation failed.\");\r\n");
            out.write("\t\t\t\t\t\tdojo.stopEvent(evt);\r\n");
            out.write("\t\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t}\r\n");
            out.write("              </script>   \r\n");
            out.write("\t\t</button>\r\n");
            out.write("\t\t<input type=\"reset\" value=\"Reset\" />\r\n");
            out.write("\t");
            int evalDoAfterBody = _jspx_th_form_005fform_005f0.doAfterBody();
            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
              break;
          } while (true);
        }
        if (_jspx_th_form_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          return;
        }
      } catch (Throwable _jspx_exception) {
        while (_jspx_push_body_count_form_005fform_005f0[0]-- > 0)
          out = _jspx_page_context.popBody();
        _jspx_th_form_005fform_005f0.doCatch(_jspx_exception);
      } finally {
        _jspx_th_form_005fform_005f0.doFinally();
        _005fjspx_005ftagPool_005fform_005fform_0026_005fmethod_005fcommandName_005faction.reuse(_jspx_th_form_005fform_005f0);
      }
      out.write("\r\n");
      out.write("<!-- \t<h3></h3> -->\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!-- \t<div>your order: -->\r\n");
      out.write("<!-- \t<div id=\"orderSize\"></div> -->\r\n");
      out.write("<!-- \t</div> -->\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<div>your address:\r\n");
      out.write("\r\n");
      out.write("\t<br />\r\n");
      out.write("\t<label >Street Address: </label>\r\n");
      out.write("\t<span id=\"street_address\" dojoType=\"dijit.InlineEditBox\" \r\n");
      out.write("\t\teditor=\"dijit.form.TextBox\">\r\n");
      out.write("\t\r\n");
      out.write("\t        <script type=\"dojo/connect\" event=\"onSet\" \r\n");
      out.write("               args=\"item,attribute,oldValue,newValue\">\r\n");
      out.write("\t\t\tconsole.debug(\"item \" + item.id);\r\n");
      out.write("            console.debug(attribute+\" changed from \"+oldValue+\" to \"+newValue);\r\n");
      out.write("\t\t\tdojo.xhrPost({\r\n");
      out.write("\t\t\t\turl: \"changeOrderStatus.htm\"\r\n");
      out.write("\t\t\t\t, content: {\r\n");
      out.write("\t\t\t\t\tid: this.getValue(item, \"id\")\r\n");
      out.write("\t\t\t\t\t,statusString: this.getValue(item, \"statusString\")\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\t,timeout: 1000\r\n");
      out.write("\t\t\t\t,error: function(){\r\n");
      out.write("\t\t\t\t\talert(\"error saving the new status\");\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\t,load: function(){\r\n");
      out.write("\t\t\t\t\tconsole.debug(\"status changed successfully\");\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t});\r\n");
      out.write("        </script>\r\n");
      out.write("\t</span>\r\n");
      out.write("\t<br />\r\n");
      out.write("\t<label >City: </label>\r\n");
      out.write("\t<span id=\"city\"></span>\r\n");
      out.write("\t<br />\r\n");
      out.write("\t<label >State: </label>\r\n");
      out.write("\t<span id=\"state\"></span>\r\n");
      out.write("\t<br />\r\n");
      out.write("\t<label >Zip: </label>\r\n");
      out.write("\t<span id=\"zip\"></span>\r\n");
      out.write("\t</div>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("<!-- \t\t<div class=\"formContainer\" dojoType=\"dijit.layout.TabContainer\"> -->\r\n");
      out.write("<!-- \t\t\t<div dijoType=\"dijit.layout.ContentPane\" title=\"Size\"> -->\r\n");
      out.write("\r\n");
      out.write("<!-- \t\t\t\t<label for=\"chosenSizeId\">size label:</label> -->\r\n");
      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("<!-- \t\t\t</div> -->\r\n");
      out.write("\r\n");
      out.write("<!-- \t\t\t<div dijoType=\"dijit.layout.ContentPane\" title=\"Toppings\"> -->\r\n");
      out.write("\r\n");
      out.write("<!-- \t\t\t\t<label for=\"chosenToppingIds\">toppings label:</label> -->\r\n");
      out.write("\r\n");
      out.write("<!-- \t\t\t\t\titemValue=\"id\" itemLabel=\"name\" /> -->\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("<!-- \t\t\t</div> -->\r\n");
      out.write("<!-- \t\t</div> -->\r\n");
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("<!-- \t\t<br> -->\r\n");
      out.write("<!-- \t\t<br> -->\r\n");
      out.write("\r\n");
      out.write("<!-- \t\t<input type=\"submit\" value=\"Place Your Order\" /> -->\r\n");
      out.write("<!-- \t\t<input type=\"reset\" value=\"Reset\" /> -->\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_spring_005fmessage_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  spring:message
    org.springframework.web.servlet.tags.MessageTag _jspx_th_spring_005fmessage_005f0 = (org.springframework.web.servlet.tags.MessageTag) _005fjspx_005ftagPool_005fspring_005fmessage_0026_005fcode_005fnobody.get(org.springframework.web.servlet.tags.MessageTag.class);
    _jspx_th_spring_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_spring_005fmessage_005f0.setParent(null);
    // /WEB-INF/jsp/orderForm.jsp(8,7) name = code type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_spring_005fmessage_005f0.setCode("title.orderForm");
    int[] _jspx_push_body_count_spring_005fmessage_005f0 = new int[] { 0 };
    try {
      int _jspx_eval_spring_005fmessage_005f0 = _jspx_th_spring_005fmessage_005f0.doStartTag();
      if (_jspx_th_spring_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_spring_005fmessage_005f0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_spring_005fmessage_005f0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_spring_005fmessage_005f0.doFinally();
      _005fjspx_005ftagPool_005fspring_005fmessage_0026_005fcode_005fnobody.reuse(_jspx_th_spring_005fmessage_005f0);
    }
    return false;
  }

  private boolean _jspx_meth_form_005fradiobuttons_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_form_005fform_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_form_005fform_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  form:radiobuttons
    org.springframework.web.servlet.tags.form.RadioButtonsTag _jspx_th_form_005fradiobuttons_005f0 = (org.springframework.web.servlet.tags.form.RadioButtonsTag) _005fjspx_005ftagPool_005fform_005fradiobuttons_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody.get(org.springframework.web.servlet.tags.form.RadioButtonsTag.class);
    _jspx_th_form_005fradiobuttons_005f0.setPageContext(_jspx_page_context);
    _jspx_th_form_005fradiobuttons_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_form_005fform_005f0);
    // /WEB-INF/jsp/orderForm.jsp(127,4) name = items type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fradiobuttons_005f0.setItems((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${allSizes}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/orderForm.jsp(127,4) name = path type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fradiobuttons_005f0.setPath("chosenSizeId");
    // /WEB-INF/jsp/orderForm.jsp(127,4) name = itemValue type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fradiobuttons_005f0.setItemValue("id");
    // /WEB-INF/jsp/orderForm.jsp(127,4) name = itemLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fradiobuttons_005f0.setItemLabel("name");
    int[] _jspx_push_body_count_form_005fradiobuttons_005f0 = new int[] { 0 };
    try {
      int _jspx_eval_form_005fradiobuttons_005f0 = _jspx_th_form_005fradiobuttons_005f0.doStartTag();
      if (_jspx_th_form_005fradiobuttons_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_form_005fradiobuttons_005f0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_form_005fradiobuttons_005f0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_form_005fradiobuttons_005f0.doFinally();
      _005fjspx_005ftagPool_005fform_005fradiobuttons_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody.reuse(_jspx_th_form_005fradiobuttons_005f0);
    }
    return false;
  }

  private boolean _jspx_meth_form_005fcheckboxes_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_form_005fform_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_form_005fform_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  form:checkboxes
    org.springframework.web.servlet.tags.form.CheckboxesTag _jspx_th_form_005fcheckboxes_005f0 = (org.springframework.web.servlet.tags.form.CheckboxesTag) _005fjspx_005ftagPool_005fform_005fcheckboxes_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody.get(org.springframework.web.servlet.tags.form.CheckboxesTag.class);
    _jspx_th_form_005fcheckboxes_005f0.setPageContext(_jspx_page_context);
    _jspx_th_form_005fcheckboxes_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_form_005fform_005f0);
    // /WEB-INF/jsp/orderForm.jsp(135,4) name = items type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fcheckboxes_005f0.setItems((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${allToppings}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/orderForm.jsp(135,4) name = path type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fcheckboxes_005f0.setPath("chosenToppingIds");
    // /WEB-INF/jsp/orderForm.jsp(135,4) name = itemValue type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fcheckboxes_005f0.setItemValue("id");
    // /WEB-INF/jsp/orderForm.jsp(135,4) name = itemLabel type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fcheckboxes_005f0.setItemLabel("name");
    int[] _jspx_push_body_count_form_005fcheckboxes_005f0 = new int[] { 0 };
    try {
      int _jspx_eval_form_005fcheckboxes_005f0 = _jspx_th_form_005fcheckboxes_005f0.doStartTag();
      if (_jspx_th_form_005fcheckboxes_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_form_005fcheckboxes_005f0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_form_005fcheckboxes_005f0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_form_005fcheckboxes_005f0.doFinally();
      _005fjspx_005ftagPool_005fform_005fcheckboxes_0026_005fpath_005fitems_005fitemValue_005fitemLabel_005fnobody.reuse(_jspx_th_form_005fcheckboxes_005f0);
    }
    return false;
  }
}
