package com.pizza.ui.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.pizza.util.HTMLUtils;
/**
 * Custom tag for outputting an arbitrary object in a form safe for a
 * Javascript &lt;script&gt; tag value.  If the value is a string it will be
 * enclosed in quotes and its contents escaped.  For other types of
 * objects, the value will usually just be rendered as a raw .toString.
 */
public class JSValueTag extends SimpleTagSupport {
	private Object value;

	/**
	 * {@inheritDoc}
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	@Override
	public void doTag() throws JspException, IOException {
		write(HTMLUtils.formatJavascriptValue(value));
	}

	/**
	 * Write to the response, converting IOException to JspException.
	 * @param string the string to write
	 * @throws JspTagException if there's an IOException
	 */
	protected void write(final String string) throws JspTagException {
		try {
			getPageContext().getOut().write(string);
		}
		catch (final IOException e) {
			throw new JspTagException(e);
		}
	}
	protected PageContext getPageContext() {
		return (PageContext)getJspContext();
	}
	
	/**
	 * Returns the Javascript value to escape.
	 * @return value object
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the Javascript value to escape.
	 * @param value value object
	 */
	public void setValue(final Object value) {
		this.value = value;
	}
}
