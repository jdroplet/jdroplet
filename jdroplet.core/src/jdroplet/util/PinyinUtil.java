package jdroplet.util;

import jdroplet.exceptions.ApplicationException;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by kuibo on 2018/8/26.
 */
public class PinyinUtil {


    public static String toPinYin(String str) {
        return toPinYin(str, "");
    }

    public static String toPinYin(String str,String spera) {
        return toPinYin(str, spera, HanyuPinyinCaseType.LOWERCASE);
    }



    /**
     * 将str转换成拼音，如果不是汉字或者没有对应的拼音，则不作转换
     * 如： 明天 转换成 MINGTIAN
     *
     * @param str：要转化的汉字
     * @param spera：转化结果的分割符
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String toPinYin(String str, String spera, HanyuPinyinCaseType type) {
        HanyuPinyinOutputFormat format = null;

        if (str == null || str.trim().length() == 0)
            return "";

        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(type);

        StringBuilder sb = new StringBuilder();
        String temp = "";
        String[] t = null;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((int) c <= 128)
                sb.append(c);
            else {
                try {
                    t = PinyinHelper.toHanyuPinyinStringArray(c, format);
                } catch (BadHanyuPinyinOutputFormatCombination ex) {
                    throw new ApplicationException(ex.getMessage());
                }
                if (t == null)
                    sb.append(c);
                else {
                    temp = t[0];
                    sb.append(temp + (i == str.length() - 1 ? "" : spera));
                }
            }
        }
        return sb.toString().trim();
    }

}
