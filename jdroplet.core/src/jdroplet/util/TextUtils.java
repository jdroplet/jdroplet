package jdroplet.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextUtils {
	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static int getStringLength(String str) {
		String new_str = null;

		try {
			new_str = new String(str.getBytes("gb2312"), "ISO8859_1");
		} catch (UnsupportedEncodingException e) {
			new_str = str;
		}

		return new_str.length();
	}
	
	public static String substrReplace(String string, String replacement, int start, int length) {
		if (TextUtils.isEmpty(string)) {
			return string;
		}

		StringBuilder sb = new StringBuilder();
		if (start > 0) {
			sb.append(string.substring(0, start));
		}
		sb.append(replacement);
		if (start + length <= string.length() - 1) {
			sb.append(string.substring(start + length));
		}
		return sb.toString();
    }
}
