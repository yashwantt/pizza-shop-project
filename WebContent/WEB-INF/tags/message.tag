<%@taglib prefix='pz' uri='http://pizza.com/tags'%><%--
--%><%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %><%--
--%><%@tag dynamic-attributes='extra'%><%--
--%><%@attribute name='key' required='true'%><%--
--%><%@attribute name='escapeParams' required='false'%><%--
--%><c:if test="${empty escapeParams}"><c:set var="escapeParams" value="true" /></c:if><%--
--%><pz:message key='${key}' escapeParams='${escapeParams}' extraAttributes='${extra}'/>