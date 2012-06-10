<%@include file='/WEB-INF/jsp/include/taglib.jspf' %>

<%@attribute name='align' required='false'%>
<%@attribute name='bannerText' required='false'%>
<%@attribute name='bannerSubtitle' required='false'%>
<%@attribute name='bannerTextKey' required='false'%>
<%@attribute name='bannerIconClass' required='false'%>
<%@attribute name='diagnosticsPage' required='false'%>
<%@attribute name='hideFooter' required='false'%>
<%@attribute name='hideLogo' required='false'%>
<%@attribute name='hideLogoInner' required='false'%>
<%@attribute name='hideWelcomeBar' required='false'%>
<%@attribute name='iconAsSprite' required="false" %>
<%@attribute name='infoText' required='false'%>
<%@attribute name='infoTextKey' required='false'%>
<%@attribute name='infoIconClass' required='false'%>
<%@attribute name='logoRegionHelpOnly' required='false'%>
<%@attribute name='pageClass' required='false'%>
<%@attribute name='hideTopNav' required='false'%>
<%@attribute name='resizable' required="false"%>
<%@attribute name='showLoginBar' required="false"%>

<%-- Set the align property to "center" if it has not been passed. --%>
<c:if test="${empty align}"><c:set var="align">center</c:set></c:if>
<c:set var="logoClass">
	<c:if test="${hideTopNav eq true or resizable eq false}">class="<c:if test="${hideTopNav eq true}">hideTopNav</c:if> <c:if test="${resizable eq false}">notResizable</c:if>"
	</c:if>
</c:set>
<div id="LogoRegionContainer" ${logoClass} >
		<%-- Logo Regions --%>
		<c:if test="${hideLogo ne true}"><%@include file='/WEB-INF/jsp/include/logoRegion.jspf' %></c:if>
	</div>

<div id='PageContainer' align='${align}' class='${pageClass}'>
	<c:if test="${ empty infoText and (! empty infoTextKey)}">
		<c:set var="infoText"><pizza:message key='${infoTextKey}'/></c:set>
	</c:if>
	<c:if test="${ empty bannerText and (! empty bannerTextKey)}">
		<c:set var="bannerText"><pizza:message key='${bannerTextKey}'/></c:set>
	</c:if>
	<c:if test="${ empty resizable}">
		<c:set var="resizable" value="true"/>
	</c:if>

	<%-- Info Region --%>
	<c:if test="${! empty infoText}">
		<div align="center" <c:if test="${resizable eq false}">class="notResizable"</c:if>>
			<div id="InfoRegion">
				<div class="bodyDark infoText <c:if test="${! empty infoIconClass}">${infoIconClass }</c:if>">${infoText}</div>
			</div>
		</div>
	</c:if>	
	<div align="center" <c:if test="${resizable eq false}">class="notResizable"</c:if>>
		<div class="PageRegionContainer">
			<%-- Top Banner --%>
			<c:if test="${! empty bannerText}">
				<div id="TopBannerRegion">
					<div>
						<c:choose>
						<c:when test="${iconAsSprite eq true}">
							<div>
								<div class="bannerIcon <c:if test="${! empty bannerIconClass}"> ${bannerIconClass }</c:if>"></div>
								<h1 class="textWithIcon">
									<c:choose>
										<c:when test="${! empty bannerSubtitle}">
											<span><pizza:message key='banner.title.withSubtitle' param0="${bannerText}"/>&nbsp;</span><span>${bannerSubtitle}</span>
										</c:when>
										<c:otherwise>
											<span>${bannerText}</span>
										</c:otherwise>
									</c:choose>
								</h1>
							</div>
						</c:when>
						<c:otherwise>
							<div>
								<h1 <c:if test="${! empty bannerIconClass}"> class="${bannerIconClass }"</c:if>>
									<c:choose>
										<c:when test="${! empty bannerSubtitle}">
											<span><pizza:message key='banner.title.withSubtitle' param0="${bannerText}"/>&nbsp;</span><span>${bannerSubtitle}</span>
										</c:when>
										<c:otherwise>
											<span>${bannerText}</span>
										</c:otherwise>
									</c:choose>
								</h1>
							</div>
						</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:if>
			<%-- Content Area --%>
			<div id="CenterRegion"><jsp:doBody/></div>
			<%-- Footer --%>
			<c:if test="${hideFooter ne true}"><%@include file='/WEB-INF/jsp/include/footer.jspf' %></c:if>
		</div>
	</div>
</div>

<%-- Used for back button detection.  Inserting this manually does away with the need to call dojo.back.init() and fixes FF3 compatibility problems. --%>
<iframe class="backButtonPreventerFrame" name="dj_history" id="dj_history" src="${ct:buildResourceName(pageContext.request, '/js/dojo/resources/iframe_history.htm', false)}"></iframe> 

</body>
</html>

<!-- This page is <%=request.getServletPath()%> -->
