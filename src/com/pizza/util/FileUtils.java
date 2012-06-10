package com.pizza.util;



public class FileUtils {
	
	public static String getWebappRoot() {
		
//		WebUtils.setWebAppRootSystemProperty();
		
		return System.getProperty("webapp.root");
		
//		ServletContext context = httpServletRequest.getSession().getServletContext();
//		String realContextPath = context.getRealPath(httpServletRequest.getContextPath());
//		
//		return realContextPath;
	}

}
