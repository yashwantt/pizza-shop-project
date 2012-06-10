package com.pizza.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RedirectBuilder {
	
	/** Prefix to use when building a redirect. */
	public static final String REDIRECT_PREFIX = "redirect:";

	/** Prefix to use when forwarding. */
	public static final String FORWARD_PREFIX = "forward:";

	private String uriPrefix = REDIRECT_PREFIX;

	private final String path;

	private final Map<Object, List<Object>> parameters = new LinkedHashMap<Object, List<Object>>();
	
	public RedirectBuilder(final String uriPrefix, final String path) {
		this.uriPrefix = uriPrefix;
		this.path = path;
	}

	public RedirectBuilder addParameter(final Object parameterName, final Object parameterValue) {
		if (null == parameterValue) {
			return this;
		}
		List<Object> parmList = parameters.get(parameterName);
		if (null == parmList) {
			parmList = new ArrayList<Object>();
			parameters.put(parameterName, parmList);
		}
		parmList.add(parameterValue);
		return this;
	}
	
	
	public String buildRedirect() {
		final StringBuilder redirect =  new StringBuilder(uriPrefix);
		redirect.append(path);
		if (parameters.isEmpty()) {
			return redirect.toString();
		}
		redirect.append('?');
		
		for (final Object key : parameters.keySet()) {
			for (final Object value: parameters.get(key)) {
				redirect.append(key.toString()).append('=').append(value).append('&');
			}
		}
		final int endIdx = redirect.length() - 1;
		if (redirect.charAt(endIdx) == '&') {
			redirect.deleteCharAt(endIdx);
		}
		
		return redirect.toString();
	}
	
}
