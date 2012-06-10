<%@include file="/WEB-INF/jsp/include/header.jspf" %>
<%@include file="/WEB-INF/jsp/include/taglib.jspf" %>
<%@taglib prefix="pizza" tagdir="/WEB-INF/tags" %>

<c:set var="title"><pizza:message key="home.banner"/></c:set>

<script type='text/javascript'>
	dojo.addOnLoad(new pizza.Landing(args).init());
</script>


<%@include file="/WEB-INF/jsp/home/home.jspf" %>
