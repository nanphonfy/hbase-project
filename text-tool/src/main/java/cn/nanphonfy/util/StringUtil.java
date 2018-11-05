package cn.nanphonfy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 把字符型数字转换成整型.
     *
     * @param str 字符型数字
     * @return int 返回整型值。如果不能转换则返回默认值defaultValue.
     */
    public static int getInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        if (isInt(str)) {
            return Integer.parseInt(str);
        } else {
            return defaultValue;
        }
    }

    public static double getDouble(String str, double defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static float getFloat(String str, float defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long getLong(String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getStr(Object str, String defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return String.valueOf(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 判断一个字符串是否为数字
     */
    public static boolean isInt(String str) {
        return str.matches("\\d+");
    }

    /**
     * 判断一个字符串是否空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断一个字符串是否空
     */
    public static String filledZeroStr(int i) {
        String str = "";
        if (i < 10) {
            str += "0" + i;
        } else {
            str += i;
        }
        return str;
    }

    /**
     * 生成序列
     * @return
     */
    public static String getSequenceId(String prefix,int first,int second,int third) {
        String sequenceId = prefix;
        // 一级
        if (first != -1) {
            sequenceId += filledZeroStr(first);
        }
        // 二级
        if (second != -1) {
            sequenceId += filledZeroStr(second);
        }
        // 三级
        if (third != -1) {
            sequenceId += filledZeroStr(third);
        }
        return sequenceId;
    }

    /**
     * 生成序列
     * @return
     */
    public static String getSequenceId(String prefix,int first,int second) {
        String sequenceId = prefix;
        // 一级
        if (first != -1) {
            sequenceId += filledZeroStr(first);
        }
        // 二级
        if (second != -1) {
            sequenceId += filledZeroStr(second);
        }
        return sequenceId;
    }

    /**
     * 剔除数字
     * @param value
     */
    public static String removeDigital(String value){
        Pattern p = Pattern.compile("[\\d]");
        Matcher matcher = p.matcher(value);
        String result = matcher.replaceAll("");
        return result;
    }
}
