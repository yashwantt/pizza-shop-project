<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<html>
  <head><title>Login</title>
  
  <style>
  	@import "/dojoroot/dijit/themes/tundra/tundra.css";
	@import "/dojoroot/dojo/resources/dojo.css";
	@import url(/css/core.css);
	@import "/css/customerWelcome.css";
  .error {
  	color: red;
  }
  .mainCont {
  	margin-top: 50px;
  }
  
  form > div {
    margin-bottom: 12px;
  }
  
  </style>
  </head>
  
  <body class="tundra wrapper">
  
 	<div class="pizzaLogo" >
		<img src="../../images/pizza-logo.png"/>
	</div>
		
		

		
    
    <div class="mainCont">
    <c:choose>
    <c:when test='${ACEGI_SECURITY_LAST_EXCEPTION.class.simpleName == "ConcurrentLoginException"}'>
			<div class="error">You have another active session.</div>  
	</c:when>
    <c:when test="${not empty param.login_error}">
      <div class="error">Login failed...try again</div><br>
    </c:when>
	</c:choose>
  
    <form method="POST" action="<c:url value='j_acegi_security_check'/>" class="regText">
      <div><label>Username:</label><input type="text" name="j_username"></div>
      <div><label>Password:</label><input type="password" name="j_password"></div>
      <div class='hideme'><input id="remember_me" type="checkbox" name="_acegi_security_remember_me"><label>Remember Me</label></div>
      <div><input type="submit" value="Login"></div>
    </form>
    </div>
    
	<div>${ACEGI_SECURITY_LAST_EXCEPTION.class.simpleName}</div>    
    
 
    
  </body>
</html>


