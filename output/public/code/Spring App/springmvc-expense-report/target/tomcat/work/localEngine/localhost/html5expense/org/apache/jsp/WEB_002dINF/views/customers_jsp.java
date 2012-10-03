package org.apache.jsp.WEB_002dINF.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class customers_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
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
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<!doctype html>\n");
      out.write("<html ng-app>\n");
      out.write("<head>\n");
      out.write("\n");
      out.write("    <script src=\"<c:url value=\"resources/javascript/jquery-1.8.1.js\"/>\" ></script>\n");
      out.write("\t<script src=\"<c:url value=\"resources/javascript/angular-1.0.0rc6.js\"/>\" ></script>\n");
      out.write("\t<script src=\"<c:url value=\"resources/javascript/expense.js\"/>\" ></script>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write(" <script language = \"javascript\" type = \"text/javascript\">\n");
      out.write("     <!--\n");
      out.write("\t\t $(function(){\n");
      out.write("\t\t  utils.setup( '");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${context}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write("'); \n");
      out.write("\t\t})\n");
      out.write(" \t   //   utils.setup( '");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${context}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write("');\n");
      out.write("    //-->\n");
      out.write("    </script>\n");
      out.write("<h2>Customer Data </h2>\n");
      out.write("\n");
      out.write("<div ng-controller=\"ExpenseCtrl\">\n");
      out.write("    <div>\n");
      out.write("        <form class=\"well form-search\" ng-submit=\"lookupExpense()\">\n");
      out.write("            <label> Search by ID</label>\n");
      out.write("            <input type=\"text\" ng-model=\"id\" class=\"input-medium search-query\" width=\"5\" size=\"5\" placeholder=\"expense #\">\n");
      out.write("            <button type=\"submit\" class=\"btn btn-primary\" ng-click=\"lookupCustomer()\" >\n");
      out.write("                <a class=\"icon-search\"></a>\n");
      out.write("            </button>\n");
      out.write("        </form>\n");
      out.write("    </div>\n");
      out.write("\n");
      out.write("    <form class=\"form-horizontal\" ng-submit=\"updateExpense\">\n");
      out.write("        <fieldset>\n");
      out.write("            <legend>\n");
      out.write("                <span class=\"customer-visible-{{!isExpenseLoaded()}}\"> Create New Customer </span>\n");
      out.write("                <span class=\"customer-visible-{{isExpenseLoaded()}}\"> Update {{expense.amount}} {{expense.description}} - {{expense.id}} </span>\n");
      out.write("            </legend>\n");
      out.write("            <div class=\"control-group\">\n");
      out.write("                <label class=\"control-label\" for=\"fn\">First Name:</label>\n");
      out.write("                <div class=\"controls\">\n");
      out.write("                    <input class=\"input-xlarge\" id=\"fn\" type=\"text\" ng-model=\"expense.description\" placeholder=\"description\"  required=\"required\"/>\n");
      out.write("                    <p class=\"help-block\">Change the first name</p>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"control-group\">\n");
      out.write("                <label class=\"control-label\" for=\"ln\">Last Name:</label>\n");
      out.write("                <div class=\"controls\">\n");
      out.write("                    <input class=\"input-xlarge\" id=\"ln\" type=\"text\" ng-model=\"expense.amount\" placeholder=\"amount\"  required=\"required\"/>\n");
      out.write("                    <p class=\"help-block\">Change the last name</p>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <div class=\"form-actions\">\n");
      out.write("                <button type=\"submit\" class=\"btn btn-primary\" ng-click=\"save()\" ng-model-instant>\n");
      out.write("                    <a class=\"icon-plus\"></a> Save\n");
      out.write("                </button>\n");
      out.write("                <button class=\"btn \" ng-click=\"trash()\"><a class=\"icon-trash\"></a> Cancel</button>\n");
      out.write("            </div>\n");
      out.write("        </fieldset>\n");
      out.write("    </form>\n");
      out.write("</div>\n");
      out.write("</body>\n");
      out.write("</html>");
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
}
