<%@attribute name='title' required='false'%>              <%-- The page title. --%>
<%@attribute name='titleKey' required='false'%>           <%-- The mesage key for the page title. --%>
<%@attribute name='styleImports' required='false'%>
<%@attribute name='styleLinks' required='false'%>
<%@attribute name='bodyClass' required='false'%>         <%-- Append this class to the body class. --%>
<%@attribute name='bodyId' required='false'%>            <%-- Slap an id on the body element. --%>
<%@attribute name='logoRegionDisable' required='false'%>
<%@attribute name='widgets' required='false'%>           <%-- Include the names of pizza widgets this page will use. --%>
<%@attribute name='preventBack' required='false'%>

<%@attribute name='bodyOnload' required='false'%>         <%-- deprecated. use pizza:dojoRequire tag and specify an init function --%>

<%@include file='/WEB-INF/jsp/include/taglib.jspf' %>


<style type='text/css'>
	<pizza:style.import name='core' />
	<c:if test='${isAdmin}'><pizza:style.import name='buttonLiquid' /></c:if>

	<%-- css import includes --%>
	<c:forEach var='styleImp' items='${styleImports}'>
		<pizza:style.import name='${styleImp}' />
	</c:forEach>
	
	<%-- the sprite file is generated at runtime, non-overridable, and always in the release folder --%>
	<ct:cssImport name="sprites" forceRelease="true" />
</style>

<%-- css link includes  --%>
<c:forEach var='styleLink' items='${styleLinks}'>
	<pizza:styles name='${styleLink}' />
</c:forEach>

<%-- widget includes --%>
<c:forEach var='widget' items='${widgets}'>
    <style type='text/css'>
        <pizza:style.import name='widgets/${widget}'/>
    </style>
</c:forEach>

<c:if test='${! empty titleKey}'>
<c:set var='title'><ct:message key='${titleKey}' /></c:set>
</c:if>
<title>${title}</title>

</head>

<c:set var='_bodyClass'><c:choose>	
    <c:when test='${isAdmin}'>AdminBody</c:when>
    <c:when test='${isProvider}'>ProviderBody</c:when>
    <c:when test='${isMember}'>MemberBody</c:when>
    <c:when test='${isAssistant}'>AssistantBody</c:when>
</c:choose></c:set>
<c:if test='${! empty bodyClass}'><c:set var='_bodyClass'>${_bodyClass} ${bodyClass}</c:set></c:if>

<%-- Add class to apply to all non-member page for easy styling --%>
<c:if test='${isProvider or isAdmin or isAssistant}'><c:set var='_bodyClass'>${_bodyClass} NonMemberBody</c:set></c:if>

<body <c:if test='${! empty bodyId}'>id='${bodyId}'</c:if> class='pizza ${_bodyClass}' onload='${bodyOnload}' >

<%@include file='/WEB-INF/jsp/include/js.jspf' %>

<jsp:doBody/>

<c:if test='${preventBack}'>
	<script type='text/javascript'>
		pizza.config.preventBack = true;
	</script>
</c:if>
<pizza:preloadImg img0='poplayers/IMG_BG_PopupHeader.gif' />
<pizza:preloadSprites img0='IMG_ConnectionLost.png' />