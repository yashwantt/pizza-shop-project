package com.pizza.ui.url_encryption;

import org.springframework.web.bind.ServletRequestUtils;

//import com.pizza.ui.url_encryption.UrlParms.UrlParm;

/**
 * An extension of {@link ServletRequestUtils} that allows passing of the
 * {@link UrlParm} instead of a parameter name.
 *
 */
public final class EncryptedServletUtils extends ServletRequestUtils {

//	private EncryptedServletUtils() {
//		// static only
//	}
//
//	/**
//	 * Get a Long parameter, or <code>null</code> if not present.
//	 * Throws an exception if it the parameter value isn't a number.
//	 * @param request current HTTP request
//	 * @param parameter the UrlParm for the parameter
//	 * @return the Long value, or <code>null</code> if not present
//	 * @throws ServletRequestBindingException a subclass of ServletException,
//	 * so it doesn't need to be caught
//	 */
//	public static Long getLongParameter(final ServletRequest request, final UrlParm parameter)
//			throws ServletRequestBindingException {
//		return getLongParameter(request, parameter.getDecodedName());
//	}
//
//	/**
//	 * Get a long parameter, with a fallback value. Never throws an exception.
//	 * Can pass a distinguished value as default to enable checks of whether it was supplied.
//	 * @param request current HTTP request
//	 * @param parameter the UrlParm for the parameter
//	 * @param defaultVal the default value to use as fallback
//	 * @return the Long value, or <code>defaultVal</code> if not present
//	 */
//	public static long getLongParameter(final ServletRequest request, final UrlParm parameter,
//			final long defaultVal) {
//		return getLongParameter(request, parameter.getDecodedName(), defaultVal);
//	}
//
//	/**
//	 * Get an array of long parameters, return an empty array if not found.
//	 * @param request current HTTP request
//	 * @param parameter the UrlParm for the parameter
//	 * @return an array of long parameters, or an empty array if not found
//	 */
//	public static long[] getLongParameters(final ServletRequest request, final UrlParm parameter) {
//		return getLongParameters(request, parameter.getDecodedName());
//	}
//
//	/**
//	 * Get a long parameter, throwing an exception if it isn't found or isn't a number.
//	 * @param request current HTTP request
//	 * @param parameter the UrlParm for the parameter
//	 * @throws ServletRequestBindingException a subclass of ServletException,
//	 * so it doesn't need to be caught
//	 * @return the Long value
//	 */
//	public static long getRequiredLongParameter(final ServletRequest request, final UrlParm parameter)
//			throws ServletRequestBindingException {
//		return getRequiredLongParameter(request, parameter.getDecodedName());
//	}
//
//	/**
//	 * Get an array of long parameters, throwing an exception if not found or one is not a number.
//	 * @param request current HTTP request
//	 * @param parameter the UrlParm for the parameter
//	 * @throws ServletRequestBindingException a subclass of ServletException,
//	 * so it doesn't need to be caught
//	 * @return an array of long parameters
//	 */
//	public static long[] getRequiredLongParameters(final ServletRequest request, final UrlParm parameter)
//			throws ServletRequestBindingException {
//		return getRequiredLongParameters(request, parameter.getDecodedName());
//	}
//
//	/**
//	 * Get a String parameter, or <code>null</code> if not present.
//	 * Throws an exception if it the parameter value isn't a number.
//	 * @param request current HTTP request
//	 * @param parameter the UrlParm for the parameter
//	 * @return the String value, or <code>null</code> if not present
//	 * @throws ServletRequestBindingException a subclass of ServletException,
//	 * so it doesn't need to be caught
//	 */
//	public static String getEncryptedStringParameter(final ServletRequest request, final UrlParm parameter)
//			throws ServletRequestBindingException {
//		return getStringParameter(request, parameter.getDecodedName());
//	}
}
