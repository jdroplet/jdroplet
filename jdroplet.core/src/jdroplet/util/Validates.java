package jdroplet.util;

public class Validates {

	public static boolean isEmail(String value, int length) {
		return value
				.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
				&& value.length() <= length;
	}

	public static boolean isIP(String value) {
		return value.matches("\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}");
	}

	public static boolean isURL(String value) {
		return value.matches("[a-zA-z]+://[^\\s]*");
	}

	public static boolean hasHtmlTag(String value) {
		return value.matches("<(\\S*?)[^>]*>.*?</\\1>|<.*? />");
	}
}
