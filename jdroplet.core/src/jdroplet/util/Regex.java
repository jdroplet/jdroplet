package jdroplet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    private static final Pattern GRAB_SP_CHARS = Pattern.compile("([\\\\*+\\[\\](){}\\$.?\\^|])");

    public static String escape(String str) {
        Matcher match = GRAB_SP_CHARS.matcher(str);
        return match.replaceAll("\\\\$1");
    }

    public static String escRegEx(String str) {
        return str.replaceAll("([\\\\*+\\[\\](){}\\$.?\\^|])", "\\\\$1");
    }

    public static String replace(String content, String pattern, String replacement) {
        return content.replaceAll(pattern, replacement);
    }

    public static boolean isMobile(String phone) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(phone);
        b = m.matches();
        return b;
    }

    public static boolean isMail(String email) {
        return find(email, "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$");
    }

    public static boolean isID(String ID) {
        return find(ID, "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)");
    }
    public static boolean find(String string, String find) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile(find);
        m = p.matcher(string);
        b = m.find();
        return b;
    }
}
