package jdroplet.util;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transforms {

	public static String stripHtmlTag(String content, String tag) { 
		content = Regex.replace(content, "(?s)(?i)<\\/{0,1}" + tag + "[^<>]*>", "");
		return content;
	}
	
	public static String extractTagAttribute(String content, String tag, String attName) {
		String reg = MessageFormat.format("<{0} {1}=\"(\\S*)\"([^>]*)>", tag, attName);
		Pattern pattern = Pattern.compile(reg); 
		Matcher matcher = pattern.matcher(content); 
		String result = "";
		
		if (matcher.find()) {
			result = matcher.group(1);
		}
		return result;
	}
	
	public static String stripHtmlXmlTags(String content) {
		return Regex.replace(content, "<[^>]+>", "");
	}
	
	public static String ascii(String content) {
		return Regex.replace(content, "[\\x00-\\xff]+", "");
	}
	
	public static String stripScriptTags(String content) {
		content = Regex.replace(content, "<script((.|\n)*?)</script>", "");
		content = Regex.replace(content, "\"javascript:", "");
		return content;
	}
	
	public static String stripSQL(String content) {
		return Regex.replace(content, "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)", "");
	}

	public static String toUpperCasePosition(String raw, int position) {
		char[] methodName = raw.toCharArray();
		methodName[position] = toUpperCase(methodName[position]);
		return String.valueOf(methodName);
	}

	public static char toUpperCase(char chars) {
		if (97 <= chars && chars <= 122) {
			chars ^= 32;
		}
		return chars;
	}
}
