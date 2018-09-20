package jdroplet.util;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie[] parse(String source) {
        ArrayList<Cookie> list = null;
        Cookie[] cookies = null;
        Pattern pattern = null;
        Matcher matcher = null;
        String key = null;
        String value = null;

        list = new ArrayList<Cookie>();
        pattern = Pattern.compile("([^=]+)=([^\\;]+);\\s?");
        matcher = pattern.matcher(source);
        while (matcher.find()) {
            key = matcher.group(1);
            value = matcher.group(2);

            list.add(new Cookie(key, value));
        }

        cookies = (Cookie[]) list.toArray(new Cookie[list.size()]);
        return cookies;
    }


    public static String getString(Cookie[] cookies, String name) {
        String value = "";
        if (cookies == null)
            return null;

        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (name.equals(cookie.getName())) {
                value = cookie.getValue();
                value = unescape(value);
                break;
            }
        }
        return value;
    }

    public static int getInt32(Cookie[] cookies, String name, Integer default_value) {
        int val = 0;
        String temp = null;

        temp = getString(cookies, name);
        if (TextUtils.isEmpty(temp))
            return default_value;

        val = Integer.parseInt(temp);
        return val;
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j)
                    || Character.isUpperCase(j)) {
                tmp.append(j);
            } else if (j < 256) {
                tmp.append("%");
                if (j < 16) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }

        return tmp.toString();
    }
}