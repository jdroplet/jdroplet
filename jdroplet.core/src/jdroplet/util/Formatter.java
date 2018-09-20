package jdroplet.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Formatter {

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	public static String friendlyNumber(int num) {
		double n2;
		DecimalFormat df   = new DecimalFormat("######0.0万");

		if (num > 10000000) {// kw
			n2 =  ((double) num / 10000) ;
		} else if (num > 1000000) { // bw
			n2 = ((double) num / 10000) ;
		} else  { // w
			n2 =  ((double) num / 10000) ;
		}

		return df.format(n2);
	}

	public static String friendlyTime(Date time) {
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天 ";
		} else if (days > 2 && days < 31) {
			ftime = days + "天前";
		} else if (days >= 31 && days <= 2 * 31) {
			ftime = "一个月前";
		} else if (days > 2 * 31 && days <= 3 * 31) {
			ftime = "2个月前";
		} else if (days > 3 * 31 && days <= 4 * 31) {
			ftime = "3个月前";
		} else {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	public static String nl2p(String raw) {
		return "<p>" + raw.replaceAll("\r|\n|\r\n", "</p><p>") + "</p>";
	}

	public static String checkStringLength(String stringToCheck, int maxLength) {
		String checkedString = null;

		if (stringToCheck.length() <= maxLength)
			return stringToCheck;

		if ((stringToCheck.length() > maxLength) && (stringToCheck.indexOf(" ") == -1)) {
			checkedString = stringToCheck.substring(0, maxLength);// + "...";
		} else if (stringToCheck.length() > 0) {
			checkedString = stringToCheck.substring(0, maxLength);// + "...";
		} else {
			checkedString = stringToCheck;
		}

		return checkedString;
	}
			
	public static String toDelimitedString(List<String> strs, String delimiter) {
		StringBuilder buf = new StringBuilder();
		int size = strs.size();
		
		for (int idx = 0; idx < strs.size() - 2; idx++) {
			buf.append(strs.get(idx).toString() + delimiter);
		}
		
		if (size > 0) {
			buf.append(strs.get(size - 1).toString() + delimiter);
		}
		return buf.toString();

	}
	
	public static String highLightHtml(String str, String word, int start, int len) {
		int end;
		String temp;

		if (start < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		start = str.indexOf(word);
		start -= word.length() << 1;
		if (start < 0) {
			start = 0;
		}
		end = start + len + word.length();
		if (end > str.length()) {
			end = str.length() - 1;
		}
		temp = highLightHtml(str.substring(start, end), word);
		return temp;
	}
	
	public static String highLightHtml(String str, String word) {
		String[] words = null;
		
		words = word.split(" ");
		word = StringUtils.join(words, "|");
		return str.replaceAll(word, "<em class=\"Label\">$0</em>");
	}
	
	public static String highLightHtml(String str, List<String> words) { 
		String temp = toDelimitedString(words, "|");
		return str.replaceAll(temp, "<em class=\"Label\">$0</em>");
	}

	public static String formatBody(String body) {
		body = Regex.replace(body, " |\t|　", "");
		body = Regex.replace(body, "(\n+)|([\r\n]+)", "\n");
		body = "　　" + Regex.replace(body, "\r\n|\n", "\n　　");
		return body;
	}
}
