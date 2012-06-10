package com.pizza.util;

public class StringUtils {

	public static String makeProper(String theString) {

		String in = theString.toLowerCase();
		boolean precededBySpace = true;
		StringBuffer properCase = new StringBuffer();
		int index = 0;
		while (index < in.length()) {
			
			int i = in.charAt(index);
			// if (i == -1) break;
			char c = (char) i;
			if (c == ' ' || c == '"' || c == '(' || c == '.' || c == '/'
					|| c == '\\' || c == ',') {
				properCase.append(c);
				precededBySpace = true;
			} else {
				if (precededBySpace) {
					properCase.append(Character.toUpperCase(c));
				} else {
					properCase.append(c);
				}
				precededBySpace = false;
			}
			index++;
		}

		return properCase.toString();

	}
	
	
	public static void main(String[] args) {
		System.out.println(makeProper("adfADF ADFSadf123afsdSFS"));
	}
}
