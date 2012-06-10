package com.pizza.util;

import net.sf.json.JSON;

public class HTMLUtils {
	
	private static final int HEX_BASE = 16;
	private static final int UTF_HEX_DIGITS = 4;
	private static final int ASCII_START = 32; // First regular printable ASCII character
	private static final int ASCII_END = 126; // Last regular printable ASCII character
	private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

	private static final char DELIM_TAG_START = '<';
	private static final char DELIM_TAG_END = '>';
	private static final char DELIM_TAG_TERMINAL = '/';
	private static final char NO_QUOTE = (char)0;
	private static final String COMMENT_TAG_START = "<!--";
	private static final String COMMENT_TAG_END = "-->";
	private static final String END_SCRIPT = "</script>";
	private static final String START_CDATA = "<![CDATA[";
	private static final String END_CDATA = "]]>";

	
	/**
	 * Formats a value as a Javascript-safe value.  The type of val is
	 * detected and formatted appropriately.  Nulls are returned as "null".
	 * Primitives are returned as their values (e.g. an int would be "123").
	 * Strings are returned with double quotes on either side and the contents
	 * properly escaped to form a valid string expression.
	 * @param val any value
	 * @return a Javascript-safe representation of the value
	 */
	public static String formatJavascriptValue(final Object val) {
		if (val == null) {
			return "null";
		}
		else if (val instanceof Number || val instanceof Boolean || val instanceof JSON) {
			return String.valueOf(val);
		}
		else {
			return "\"" + removeProblemJavascriptSequences(escapeJavascriptString(
					String.valueOf(val)), '"') + "\"";
		}
	}
	
	/**
	 * Returns a string escaped for a Javascript string.  This value is
	 * suitable for an expression such as var foo = "abc"; where abc
	 * is the result of this method.
	 * @param str string to escape
	 * @return escaped string
	 */
	public static String escapeJavascriptString(final String str) {
		return escapeJavascriptString(str, false);
	}
	
	/**
	 * Returns a string escaped for a Javascript string.  This value is
	 * suitable for an expression such as var foo = "abc"; where abc
	 * is the result of this method.
	 * @param str string to escape
	 * @param iso88591 whether to escape any characters outside the ISO-8859-1
	 *   character set (more compatible but more verbose)
	 * @return escaped string
	 */
	public static String escapeJavascriptString(final String str, final boolean iso88591) {
		final int l = str.length();
		final StringBuilder sb = new StringBuilder(l);
		for (int i = 0; i < l; i++) {
			final char c = str.charAt(i);
			if (c == '\'' || c == '"' || c == '&' || c == '\\') {
				sb.append('\\').append(c);
			}
			else if (c == '\n') {
				sb.append("\\n");
			}
			else if (c == '\r') {
				sb.append("\\r");
			}
			else if (c == '\t') {
				sb.append("\\t");
			}
			else if (c == '\b') {
				sb.append("\\b");
			}
			else if (iso88591 && (c < ASCII_START || c > ASCII_END)) {
				sb.append("\\u").append(hex(c, UTF_HEX_DIGITS));
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	
	private static String hex(final int v, final int digits) {
		// Returns a 0-padded hexidecimal string up to 4 digits long
		final String h = Integer.toString(v, HEX_BASE);
		return "0000".substring(0, digits - h.length()) + h;
	}

	
	/**
	 * Removes reserved sequences <code>&lt;/script&gt;</code>, <code>&lt;![CDATA[</code>,
	 * and <code>]]&gt;</code>.  Even if properly enclosed in quotes, these may
	 * artificially shorten or alter a script block.  This method will break up
	 * these sequences with string concatenation.  E.g. <code>&lt;/script&gt;</code>
	 * will become <code>&lt;/scr"+"ipt&gt;</code>. It evaluates exactly the
	 * same, but the HTML parser will not see the reserved sequence.  Upper/lower
	 * case will be preserved when substituting.
	 * @param str string to scrub
	 * @param quote quote character being used to enclose the string value
	 * @return scrubbed Javascript string
	 */
	private static String removeProblemJavascriptSequences(
			final String str,
			final char quote) {
		String s = str;
		int p = s.toLowerCase().indexOf(END_SCRIPT);
		while (p >= 0) {
			// We extract the original and alter that, instead of substituting
			// a hardcoded replacement, so that we can preserve the case.  We
			// don't want to change the content in ANY way, just escape it safely.
			final String match = s.substring(p, p + END_SCRIPT.length());
			final int half = END_SCRIPT.length() >> 1;
			final String replacement = match.substring(0, half) + quote + '+'
					+ quote + match.substring(half);
			s = s.substring(0, p) + replacement + s.substring(p + END_SCRIPT.length());
			p = s.toLowerCase().indexOf("</script>");
		}
		// Replace any CDATA sequences
		return s.replace(START_CDATA, "<![CD" + quote + '+' + quote + "ATA[")
				.replace(END_CDATA, "]" + quote + '+' + quote + "]>");
	}
}
